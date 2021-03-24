package nyla.solutions.core.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * utilize for the numbers
 * @author Gregory Green
 *
 */
public class Digits
{
	private Random random = new Random(System.currentTimeMillis());
	
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
	
	public double generateDouble()
	{
		return random.nextDouble();
	}
	
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
}
