package nyla.solutions.core.util;


import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.reflection.Mirror;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectReflectorTest
{
	/**
	 * Test the new instance for class name method
	 * @throws Exception
	 */
	@Test
	public void testNewInstanceForClassName()
	throws Exception
	{
		Object obj = ClassPath.newInstance(SampleObject.class);
		
		assertNotNull(obj);
		
		assertTrue(obj instanceof SampleObject);
		
		
		Mirror mirror = Mirror.newInstanceForClassName(SampleObject.class.getName());
		
		mirror.setField("integer", "35");
		
		
		SampleObject sampleObject = (SampleObject)mirror.getObject();
		
		assertTrue(sampleObject.getInteger().equals(Integer.valueOf(35)),"35="+sampleObject.getInteger());
	}// -----------------------------------------------

	public static class SampleObject
	{
		public SampleObject(String text)
		{
			this.text = text;
		}// -----------------------------------------------
		public SampleObject(Integer integer)
		{
			this.integer = integer;
		}// -----------------------------------------------
		
		/**
		 * @return the integer
		 */
		public Integer getInteger()
		{
			return integer;
		}
		/**
		 * @param integer the integer to set
		 */
		public void setInteger(Integer integer)
		{
			this.integer = integer;
		}
		/**
		 * @return the text
		 */
		public String getText()
		{
			return text;
		}
		/**
		 * @param text the text to set
		 */
		public void setText(String text)
		{
			this.text = text;
		}


		private Integer integer = null;
		private String text =  null;
	}// -----------------------------------------------
}
