package nyla.solutions.core.patterns.batch;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.conversion.Converter;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <code>
 * Handles reading, processing and writing records in a batch fashion.
 *
 * Sample Usage
 *
 *   BatchJob batchJob = BatchJob.builder().supplier(supplier)
 *                                         .consumer(consumer)
 *                                         .batchChunkSize(batchChunkSize).processor(
 *                             processor).build();
 *
 *             BatchReport batchRecord = batchJob.execute()
 * </code>
 *
 * @author Gregory Green
 */
public class BatchJob<InputType, OutputType>
{
    private final Supplier<InputType> supplier;
    private final Consumer<List<OutputType>> consumer;
    private final int batchChunkSize;
    private final Function<InputType, OutputType> processor;


    public BatchJob(Supplier<InputType> supplier, Consumer<List<OutputType>> consumer, int batchChunkSize)
    {
        this(supplier, consumer, batchChunkSize, (Function)((item) -> (OutputType)item));
    }
    public BatchJob(Supplier<InputType> supplier, Consumer<List<OutputType>> consumer,
                    int batchChunkSize, Converter<InputType, OutputType> processor)
    {
        this.supplier = supplier;
        this.consumer = consumer;
        this.batchChunkSize = batchChunkSize;
        this.processor = (input) -> processor.convert(input);
    }
    public BatchJob(Supplier<InputType> supplier, Consumer<List<OutputType>> consumer,
                    int batchChunkSize, Function<InputType, OutputType> processor)
    {
        this.supplier = supplier;
        this.consumer = consumer;
        this.batchChunkSize = batchChunkSize;
        this.processor = processor;
    }

    public static BatchJobBuilder builder()
    {
        return new BatchJobBuilder();
    }


    public BatchReport execute()
    {
        BatchReport batchReport = new BatchReport();
        InputType item;
        ArrayList<OutputType> outputList = new ArrayList<OutputType>(batchChunkSize);
        while ((item = supplier.get()) != null)
        {
            outputList.add(this.processor != null ? this.processor.apply(item) : (OutputType)item);

            batchReport.incrementInput();

            if (outputList.size() >= batchChunkSize - 1)
            {
                this.consumer.accept(outputList);
                batchReport.incrementOutput(outputList.size());
                outputList.clear();
            }
        }

        if(supplier instanceof Closeable)
        {
            try {
                ((Closeable) supplier).close();
            }
            catch (IOException e) {
                throw new ConnectionException("Unable to close supplier ERROR:"+e.getMessage(),e);
            }
        }

        if(consumer instanceof Closeable)
        {
            try {
                ((Closeable) consumer).close();
            }
            catch (IOException e) {
                throw new ConnectionException("Unable to close consumer ERROR:"+e.getMessage(),e);
            }
        }

        if(!outputList.isEmpty())
        {
            this.consumer.accept(outputList);
            batchReport.incrementOutput(outputList.size());
        }

        return batchReport;
    }

    public static class BatchJobBuilder<InputType, OutputType>
    {
        private Supplier supplier = null;
        private Consumer<List<OutputType>> consumer = null;
        private int batchChunkSize = 1000;
        private Function<InputType, OutputType> processor = null;

        public BatchJobBuilder supplier(Supplier<InputType> supplier)
        {
            this.supplier = supplier;
            return this;
        }

        public BatchJobBuilder consumer(Consumer<List<OutputType>> consumer)
        {
            this.consumer = consumer;
            return this;
        }

        public BatchJobBuilder batchChunkSize(int batchChunkSize)
        {
            this.batchChunkSize = batchChunkSize;
            return this;
        }

        public BatchJobBuilder processor(Function<InputType, OutputType> processor)
        {
            this.processor = processor;
            return this;
        }

        public BatchJob build()
        {
            if(consumer == null)
                throw new RequiredException("consumer is missing");

            if(supplier == null)
                throw new RequiredException("supplier is missing");

            return new BatchJob(supplier,consumer,batchChunkSize,processor);
        }
    }
}
