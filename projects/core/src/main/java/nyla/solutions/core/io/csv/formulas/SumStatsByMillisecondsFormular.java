package nyla.solutions.core.io.csv.formulas;

import nyla.solutions.core.exception.MissingFileException;
import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.io.csv.CsvReader.DataType;
import nyla.solutions.core.util.stats.Mathematics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * <pre>
 * Reports min, max, average.
 * 
 * 
 * You can run this class a standalone java application.
 * 
 * Usage:
 * 
 * java ...SumStatsByMillisecondsFormular <file> <msSecColumn> <calculateCol> <sumByMillisec>
 * 
 * 
 * 
 * Example summarize client.csv where first column has time, seconds a count by 1000milliseconds = 1 sec
 * 
 * java nyla.solutions.core.io.csv.formulas.SumStatsByMillisecondsFormular client.csv 0 1 1000
 * </pre>
 * @author Gregory Green
 *
 */
public class SumStatsByMillisecondsFormular implements CsvFormula
{
	private Mathematics mathematics=  new Mathematics();
	public SumStatsByMillisecondsFormular(int timeMsIndex, int calculateColumn, long milliseconds)
	{
		this.timeMsIndex = timeMsIndex;
		this.calculateColumn = calculateColumn;
		this.milliseconds = milliseconds;
	}//------------------------------------------------
	
	public void calc(CsvReader reader)
	{
		if(reader == null || reader.isEmpty())
			return;
		
		init();
		
		reader.sortRowsForIndexByType(timeMsIndex, DataType.Long);

		long tsMS, startTsMS = -1;
		int size = reader.size();
		
		for(int i=0; i < size;i++)
		{
			tsMS = reader.get(i,timeMsIndex, DataType.Long);

			this.currentVal = reader.get(i,calculateColumn, DataType.Long);
			
			//check if this is within a second
			if(startTsMS == -1)
			{
				startTsMS = tsMS;
				
				addInMillsec();
			}
			else if ((tsMS - startTsMS) <= milliseconds)
			{
				addInMillsec();
			}
			else if( (tsMS - startTsMS) > milliseconds)
			{
			
				this.incrementStat();
				startTsMS = tsMS;
			}
		}
		
		this.incrementStat();
	}
	
	private void addInMillsec()
	{
		sumByMillisec = sumByMillisec.add(BigInteger.valueOf(this.currentVal));
	}

	private void incrementStat()
	{
		count++;
		
		//sum end
		sum = sum.add(sumByMillisec);
	
		
		
		max = max != -1 ? Math.max(sumByMillisec.longValue(), max) :sumByMillisec.longValue();
		min = min != -1 ? Math.min(sumByMillisec.longValue(), min) : sumByMillisec.longValue();
		avg = avg != -1 ? sum.divide(BigInteger.valueOf(count)).longValue(): sumByMillisec.longValue();		

		
		averages.add(avg);
		
		sumByMillisec = BigInteger.valueOf(currentVal);
		
	}
	
	
	/**
	 * @return the avg (-1 is return for empty entries)
	 */
	public double getAvg()
	{
		return avg;
	}

	/**
	 * @return the max (-1 is return for empty entries)
	 */
	public long getMax()
	{
		return max;
	}

	/**
	 * @return the min (-1 is return for empty entries)
	 */
	public long getMin()
	{
		return min;
	}
	
	public double getStdDev()
	{
		return mathematics
				.stdDev(this.averages);

	}//------------------------------------------------

	private void init()
	{

		sum = BigInteger.ZERO;
		sumByMillisec = BigInteger.ZERO;
		currentVal = -1;
		count =0;
		avg = -1;
		max = -1;
		min = -1;
	}
	
	/**
	 * Usages file msSecColumn calculateCol sumByMillisec
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length !=  4)
		{
			System.err.println("Usage java "+SumStatsByMillisecondsFormular.class.getName()+" file msSecColumn calculateCol sumByMillisec");
			System.exit(-1);
		}
		
		File file = Paths.get(args[0]).toFile();
		
		try
		{
			if(!file.exists())
			{
				throw new MissingFileException(file.getAbsolutePath());
			}
			
			int timeMsIndex = Integer.parseInt(args[1]);
			int calculateColumn = Integer.parseInt(args[2]);
			long milliseconds = Long.parseLong(args[3]);
			
			SumStatsByMillisecondsFormular formular = new SumStatsByMillisecondsFormular(timeMsIndex, calculateColumn, milliseconds);
			
			new CsvReader(file).calc(formular);
			
			System.out.println(formular);
		}
		catch (NumberFormatException e)
		{
			System.err.println("Checks timeMsIndex:"+args[1]+",calculateColumn:"+args[2]+" and milliseconds:"+args[3]+" are valids numbers error:"+e);
		}
		
		
	}
	
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SumStatsByMillisecondsFormular [milliseconds=").append(milliseconds).append(", timeMsIndex=").append(timeMsIndex).append(", calculateColumn=")
				.append(calculateColumn).append(", avg=").append(avg).append(", max=").append(max).append(", min=")
				.append(min).append(", stdDev=").append(this.getStdDev()).append("]");
		return builder.toString();
	}


	private final int timeMsIndex;
	private final int calculateColumn;
	private BigInteger sum = BigInteger.ZERO;
	private BigInteger sumByMillisec = BigInteger.ZERO;
	private final long milliseconds;
	private long currentVal = -1;
	private long count =0;
	private double avg = -1;
	
	private ArrayList<Double> averages = new ArrayList<Double>();
	private long max = -1;
	private long min = -1;

}
