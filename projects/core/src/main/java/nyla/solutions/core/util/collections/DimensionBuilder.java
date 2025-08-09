package nyla.solutions.core.util.collections;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * @author gregory green
 * @param <T> the Dimension list typle
 */
public class DimensionBuilder<T> {
    private final T[] inputs;
    private int length;
    private T fillValue;

    /**
     *
     * @param inputs the input array
     */
    private DimensionBuilder(T[] inputs) {
        this.inputs = inputs;
    }

    /**
     * Factory method to create builder
     * @param inputs the input
     * @return the builder instanace
     * @param <T> the list type to builder
     */
    public static <T> DimensionBuilder builder(T[] inputs) {
        return new DimensionBuilder(inputs);
    }

    public static <T> DimensionBuilder builder(double[] inputs) {
        return new DimensionBuilder(
                DoubleStream.of(inputs).boxed().toArray());
    }

    /**
     * Set the fixed lenght of the dimension
     * @param size the fixed lenght of the dimension
     * @return the builder
     */
    public DimensionBuilder<T> length(int size) {
        this.length = size;

        return this;
    }

    /**
     * Set the intiial fill value for empty dimensions
     * @param fillValue the value to set
     * @return the builder instance
     */
    public DimensionBuilder<T> fillValue(T fillValue) {
        this.fillValue = fillValue;
        return this;
    }


    /**
     *
     * @return the product of built elements
     */
    public List<T> build() {

        if(this.length  == 0)
            return  Arrays.asList(inputs);
        else if(this.length < inputs.length)
        {
            return Arrays.asList(Arrays.copyOf(inputs,this.length));
        }

        var newArray = Arrays.copyOf(inputs,this.length);
        Arrays.fill(newArray,inputs.length,newArray.length, fillValue);
        return  Arrays.asList(newArray);
    }
}
