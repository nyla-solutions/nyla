package nyla.solutions.core.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberedPropertyTest
{

	@Test
	public void testNumberedPropertyStringInt()
	{
		NumberedProperty np = new NumberedProperty("test",1);
		assertEquals(np.getNumber(), 1);
		
	}

}
