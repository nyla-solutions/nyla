package nyla.solutions.core.util.stats;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * @author Gregory Green
 */
public class MathematicStats implements Consumer<Number>
{
    private final int capacity;
    private final Mathematics mathematics;
    private int acceptCount =0;
    private final Number[] values;

    public MathematicStats(int capacity, Mathematics mathematics)
    {
        if(capacity < 1)
            throw new IllegalArgumentException("Expected capacity > 0");

        this.capacity = capacity;
        values = new Number[capacity];
        this.mathematics = mathematics;
    }

    public void accept(Number expected)
    {
        values[acceptCount%capacity]=expected;
        acceptCount++;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public int getAcceptCount()
    {
        return acceptCount;
    }

    public double percentile(double percential)
    {
        return this.mathematics.percentile(percential,values);
    }

    public double mean()
    {
        return this.mathematics.mean(values);
    }

    public double stdDev()
    {
        return this.mathematics.stdDev(values);
    }

    public double min()
    {
        return this.mathematics.min(values);
    }

    /**
     *
     * @return the max of the given values
     */
    public double max()
    {
        return this.mathematics.max(values);
    }
}
