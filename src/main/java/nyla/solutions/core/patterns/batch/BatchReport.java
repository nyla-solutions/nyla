package nyla.solutions.core.patterns.batch;

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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"inputCount\":").append(inputCount);
        sb.append(",\"outputCount\":").append(outputCount);
        sb.append('}');
        return sb.toString();
    }
}
