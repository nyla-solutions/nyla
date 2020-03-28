package nyla.solutions.core.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DigitsTest
{

	@Test
	public void testInteger()
	{
		Digits digits = new Digits();
		long generatedId = digits.generateInteger();
		assertNotEquals(generatedId ,digits.generateInteger());
	    generatedId = digits.generateInteger();
		assertTrue( generatedId > 0);
	}
	
	
	public void testGenerateLong()
	{
		Digits digits = new Digits();
		assertNotEquals(digits.generateLong() ,digits.generateLong());
		assertTrue(digits.generateLong() > 0);
	}

}
