package nyla.solutions.core.util.stats;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * Based on
 * 
 * http://introcs.cs.princeton.edu/java/stdlib/StdStats.java.html
 * 
 *
 */
public class Mathematics
{
	public double variance(Number... a) {
      
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i].doubleValue() - avg) * (a[i].doubleValue() - avg);
        }
        return sum / (a.length - 1);
    }
	
	  /**
     * Returns the average value in the specified array.
     *
     * @param  a the array
     * @return the average value in the array {@code a[]};
     *         {@code Double.NaN} if no such value
     */
    public double mean(Number... a) {

        if (a.length == 0) return Double.NaN;
        double sum = sum(a);
        return sum / a.length;
    }

    /**
     * Returns the population variance in the specified array.
     *
     * @param  a the array
     * @return the population variance in the array {@code a[]};
     *         {@code Double.NaN} if no such value
     */
    public double varp(Number... a) {

        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += (a[i].doubleValue() - avg) * (a[i].doubleValue() - avg);
        }
        return sum / a.length;
    }
    
    /**
     * Returns the population standard deviation in the specified array.
     *
     * @param  a the array
     * @return the population standard deviation in the array;
     *         {@code Double.NaN} if no such value
     */
    public double stdDev(Number... a) {

    	if(a == null || a.length == 0)
    		return -1;
    	
        return Math.sqrt(varp(a));
    }
    public double stdDev(List<? extends Number> averages)
    {
        Number[] numbers = new Number[averages.size()];
        averages.toArray(numbers);
        return stdDev(numbers);
    }
    
    public double sum(Number... a) {

        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i].doubleValue();
        }
        return sum;
    }

    public double percentile(double percentile, Number... a)
    {
        long total= a.length;

        int n = Math.round(Double.valueOf(total * (percentile/100)).floatValue());

        Number[] sortCopy = Arrays.copyOf(a,a.length);
        Arrays.sort(sortCopy);

        return sortCopy[n-1].doubleValue();


    }


}
