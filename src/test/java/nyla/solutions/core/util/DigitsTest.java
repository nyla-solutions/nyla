package nyla.solutions.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Digits
 * @author Gregory Green
 */
public class DigitsTest
{
	private Digits subject;

	@BeforeEach
	void setUp()
	{
		subject = new Digits();
	}


	@Test
	void generateShorts() {
		short min = 1;
		short max = 56;
		short actual = subject.generateShort(min,max);

		assertThat(actual).isBetween(min,max);
	}

	@Test
	void generateDouble()
	{
		Double actual = 0.0;

		for (int i = 0; i < 3; i++) {
			actual = subject.generateDouble();
			System.out.println(actual);
			assertThat(actual).isGreaterThan(-1);
		}
	}

	@Test
	void generateDouble_WhenRangesProvided() throws InterruptedException
	{
		Double low = 8.21;
		Double high = 17.21;
		Double actual = subject.generateDouble(low,high);
		assertThat(actual).isBetween(low,high);


		for (int i = 0; i < 10; i++) {
//			Thread.sleep(100);
			actual = subject.generateDouble(low,high);
			assertThat(actual).isBetween(low,high);
			System.out.println("actual:"+actual);
		}

	}

	@Test
	void generateDouble_WhenBound_ThenLessThan()
	{
		Double bound = 34.00;
		Double actual = subject.generateDouble(bound);

		System.out.println(actual);
		assertTrue(actual >= 0 && actual <= bound,"actual:"+actual+" between 0 and "+bound);
	}

	@Test
	public void generateInteger_Start_to_End()
	{

		int id = subject.generateInteger(1,1);
		assertEquals(1,id);
		id = subject.generateInteger(2,2);
		assertEquals(2,id);
		id = subject.generateInteger(0,2);
		assertThat(id).isBetween(0,2);
	}

	@Test
	public void generateInteger()
	{
		long generatedId = subject.generateInteger();
		assertNotEquals(generatedId ,subject.generateInteger());
	    generatedId = subject.generateInteger();
		assertTrue( generatedId > 0);
	}
	
	
	public void generateLong()
	{
		assertNotEquals(subject.generateLong() ,subject.generateLong());
		assertTrue(subject.generateLong() > 0);
	}

}
