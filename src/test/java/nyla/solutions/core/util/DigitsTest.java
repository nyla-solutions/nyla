package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DigitsTest
{
	@Test
	public void testInteger_Start_to_End()
	{
		Digits subject = new Digits();
		int id = subject.generateInteger(1,1);
		assertEquals(1,id);
		id = subject.generateInteger(2,2);
		assertEquals(2,id);
		id = subject.generateInteger(0,2);
		assertThat(id).isBetween(0,2);
	}

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
