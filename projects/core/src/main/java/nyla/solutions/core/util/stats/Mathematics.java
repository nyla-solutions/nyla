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
    private NullableNumberComparator comparator = new NullableNumberComparator();

	public double variance(Number... a) {
      
        if (a.length == 0) return Double.NaN;
        double avg = mean(a);
        double sum = 0.0;

        int count =  0;
        for (int i = 0; i < a.length; i++) {
            if(a[i] == null)
                continue;
            else
                count++;

            sum += (a[i].doubleValue() - avg) * (a[i].doubleValue() - avg);
        }
        return sum / (count - 1);
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
        return sum / nonNullCount(a);
    }

    private long nonNullCount(Number[] a)
    {
        long count = 0;
        for (int i = 0; i < a.length; i++) {
            if(a[i] != null)
                count++;
        }
        return count;
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
        int count =0;
        for (int i = 0; i < a.length; i++) {
            if(a[i] == null)
                continue;
            else
                count++;

            sum += (a[i].doubleValue() - avg) * (a[i].doubleValue() - avg);
        }
        return sum / count;
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

            if(a[i] == null)
                continue;

            sum += a[i].doubleValue();
        }
        return sum;
    }

    public double percentile(double percentile, Number... a)
    {
        if(a.length == 0)
            return Double.NaN;

        long total= nonNullCount(a);

        int n = Math.round(Double.valueOf(total * (percentile/100)).floatValue());

        Number[] sortCopy = Arrays.copyOf(a,a.length);

        Arrays.sort(sortCopy,comparator);


        return sortCopy[n-1].doubleValue();


    }

    private void fixNulls(Number[] sortCopy, Number valueForNull)
    {
        for (int i = 0; i < sortCopy.length; i++) {
            if(sortCopy[i] == null)
                sortCopy[i] = valueForNull;
        }
    }


    public double min(Number... a)
    {
        if(a.length == 0)
            return 0;

        double min = Double.NaN;

        if(a[0] != null)
            min = a[0].doubleValue();

        double current;
        for (int ktr = 0; ktr < a.length; ktr++) {
            if(a[ktr] == null)
                continue;

            current = a[ktr].doubleValue();
            if (Double.isNaN(min)  || current< min) {
                min = current;
            }
        }
        return min;
    }

    public double max(Number... a)
    {
        if(a.length == 0)
            return 0;

        double max = Double.NaN;

        if(a[0] != null)
            max = a[0].doubleValue();

        double current;
        for (int ktr = 0; ktr < a.length; ktr++) {

            if(a[ktr] == null)
                continue;

            current = a[ktr].doubleValue();

            if (Double.isNaN(max)  || current> max) {
                max = current;
            }
        }
        return max;
    }

}
