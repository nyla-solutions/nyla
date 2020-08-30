package nyla.solutions.core.util;

import nyla.solutions.core.data.Named;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JavaBeanTest
{

	@BeforeEach
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void test_getPropertyNames()
	{
		UserProfile bean = new UserProfile();
		
		assertNull(JavaBean.getPropertyNames(null));
		Set<String> names = JavaBean.getPropertyNames(bean);
		assertTrue(names != null && names.contains("email"));
		assertEquals(names,JavaBean.getPropertyNames(UserProfile.class));
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testNested()
	throws Exception
	{
		Named nyla = new Named();
		
		nyla.setName("Nyla Gianni");
		
		Named josiah = new Named();
		
		josiah.setName("Josiah Gregory");
	
		ArrayList<Named> list = new ArrayList<Named>();
		list.add(nyla);
		list.add(josiah);
		
		Object results = JavaBean.getNestedProperty(list, "name");
		
		
		assertTrue(results instanceof Collection<?>);
		
		Collection<String> collection = (Collection<String>)results;
		
		for (String string : collection)
		{
			System.out.println(string);
		}
		
		assertEquals(null,JavaBean.getNestedProperty(null, "name"));
		
		ComplexObject complexObject = new ComplexObject();
		
		complexObject.setName(list);
		complexObject.setState("NJ");
		
	    collection = (Collection<String>)JavaBean.getNestedProperty(complexObject, "name.name");
		
		for (String string : collection)
		{
			System.out.println(string);
		}
		complexObject.setName(null);

		assertNull(JavaBean.getNestedProperty(complexObject, "name.name"));
		
	}
	
	public static class ComplexObject
	{
		
		
		/**
		 * @return the state
		 */
		public String getState()
		{
			return state;
		}
		/**
		 * @param state the state to set
		 */
		public void setState(String state)
		{
			this.state = state;
		}

		/**
		 * @return the name
		 */
		public Collection<Named> getName()
		{
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(Collection<Named> name)
		{
			this.name = name;
		}

		private String state;
		private Collection<Named> name;
	}

}
