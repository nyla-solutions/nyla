package nyla.solutions.core.patterns.conversion.transformation;

/**
 * Statistics for the batch transformations executor
 * @author Gregory Green
 */
public class BatchReport
{
    private long inputCount = 0;
    private long outputCount = 0;
    public long countInput()
    {
        return inputCount;
    }

    public long countOutput()
    {
        return outputCount;
    }

    public void incrementInput()
    {
        inputCount++;
    }


    public void incrementOutput(int size)
    {
        outputCount += size;
    }
}
