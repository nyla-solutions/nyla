package nyla.solutions.core.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NumberedPropertyTest
{

	@Test
	public void testNumberedPropertyStringInt()
	{
		NumberedProperty np = new NumberedProperty("test",1);
		assertEquals(np.getNumber(), 1);
		
	}

}
