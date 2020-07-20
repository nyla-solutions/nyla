package nyla.solutions.core.patterns.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Gregory Green
 */
public class BatchListExecutor<InputType, OutputType>
{
    private final Supplier<InputType> supplier;
    private final Consumer<List<OutputType>> consumer;
    private final int batchChunkSize;
    private final Function<InputType, OutputType> processor;


    public BatchListExecutor(Supplier<InputType> supplier, Consumer<List<OutputType>> consumer, int batchChunkSize)
    {
        this(supplier, consumer, batchChunkSize, (item) -> (OutputType)item);
    }

    public BatchListExecutor(Supplier<InputType> supplier, Consumer<List<OutputType>> consumer,
                             int batchChunkSize, Function<InputType, OutputType> processor)
    {
        this.supplier = supplier;
        this.consumer = consumer;
        this.batchChunkSize = batchChunkSize;
        this.processor = processor;
    }


    public BatchReport execute()
    {
        BatchReport batchReport = new BatchReport();
        InputType item;
        ArrayList<OutputType> outputList = new ArrayList<OutputType>(batchChunkSize);
        while ((item = supplier.get()) != null)
        {
            outputList.add(this.processor.apply(item));

            batchReport.incrementInput();

            if (outputList.size() >= batchChunkSize - 1)
            {
                this.consumer.accept(outputList);
                batchReport.incrementOutput(outputList.size());
                outputList.clear();
            }
        }

        return batchReport;
    }
}
