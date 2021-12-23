package nyla.solutions.core.util;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility for the numbers
 * @author Gregory Green
 *
 */
public class Digits
{
	private Random random = new Random();
	
	/**
	 * 
	 * @return the generated integer
	 */
	public int  generateInteger()
	{
		return Math.abs(random.nextInt()+1);
	}//------------------------------------------------
	/**
	 * 
	 * @return the generated long
	 */
	public long generateLong()
	{
		return Math.abs(random.nextLong()+1);
	}

	/**
	 * Generate a double
	 * @return the generated double
	 */
	public double generateDouble()
	{
		return random.nextDouble()+ generateInteger();
	}


	/**
	 *
	 * @return the generate random float
	 */
	public float generateFloat()
	{
		return random.nextFloat();
	}

	public short generateShort()
	{
		return Integer.valueOf(random.nextInt()).shortValue();
	}

	public BigDecimal generateBigDecimal()
	{

		return BigDecimal.valueOf(random.nextDouble());
	}

    public int generateInteger(int min, int max)
    {
    	return this.random.nextInt(max - min + 1) + min;
    }

	public double generateDouble(Double low, Double high)
	{
		Double bound =  high - low + 1;
		return random.nextInt(bound.intValue())+low;
	}

	public double generateDouble(Double bound)
	{
		return random.nextInt(bound.intValue())+ (random.nextDouble()/2);
	}
}
