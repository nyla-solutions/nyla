package nyla.solutions.core.patterns.batch;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BatchListExecutorTest
{
    @Test
    public void test_process_reader_single_chunk_processor()
    {
        Supplier supplier = mock(Supplier.class);
        Consumer<List<UserProfile>> consumer = mock(Consumer.class);
        Function<UserProfile,UserProfile> processor = mock(Function.class);

        int batchChunkSize = 1;
        UserProfile expected  = new UserProfile();
        when(supplier.get())
                .thenReturn(expected)
                .thenReturn(null);

        BatchListExecutor batchListExecutor = new BatchListExecutor(supplier,consumer,batchChunkSize,
                processor);

        BatchReport batchRecord = batchListExecutor.execute();

        verify(supplier,atLeastOnce()).get();
        verify(consumer,atLeastOnce()).accept(any());
        verify(processor,atLeastOnce()).apply(any());
        assertNotNull(batchRecord);
        assertEquals(1,batchRecord.countInput());
    }

    @Test
    public void test_process_reader_single()
    {
        Supplier supplier = mock(Supplier.class);
        Consumer<UserProfile> consumer = mock(Consumer.class);
         int batchChunkSize = 2;
        UserProfile expected  = new UserProfile();
        when(supplier.get())
                .thenReturn(expected)
                .thenReturn(null);

        BatchListExecutor batchListExecutor = new BatchListExecutor(supplier,consumer,batchChunkSize);

        BatchReport batchRecord = batchListExecutor.execute();

        verify(supplier,atLeastOnce()).get();
        verify(consumer,atLeastOnce()).accept(any());
        assertNotNull(batchRecord);
        assertEquals(1,batchRecord.countInput());
    }

    @Test
    public void test_process_reader_single_with_function()
    {
        Supplier<String> supplier = mock(Supplier.class);
        Consumer<UserProfile> consumer = mock(Consumer.class);
        Function<String,UserProfile> processor = mock(Function.class);

        int batchChunkSize = 2;
        String expectedString = "imani@two.com";

        UserProfile expected  = new UserProfile();
        when(supplier.get())
                .thenReturn(expectedString)
                .thenReturn(null);

        BatchListExecutor batchListExecutor = new BatchListExecutor(supplier,consumer,batchChunkSize,processor);

        BatchReport batchRecord = batchListExecutor.execute();
        verify(processor).apply(anyString());
    }


    @Test
    public void test_process_reader_multiple()
    {
        Supplier supplier = mock(Supplier.class);
        Consumer<UserProfile> consumer = mock(Consumer.class);
        int batchChunkSize = 2;
        UserProfile expected  = new UserProfile();
        when(supplier.get())
                .thenReturn(expected)
                .thenReturn(expected)
                .thenReturn(null);

        BatchListExecutor batchListExecutor = new BatchListExecutor(supplier,consumer,batchChunkSize);

        BatchReport batchRecord = batchListExecutor.execute();

        verify(supplier,atLeast(2)).get();
        verify(consumer,atLeastOnce()).accept(any());
        assertNotNull(batchRecord);
        assertEquals(2,batchRecord.countInput());
    }



    @Test
    public void test_process_reader_multiple_batch()
    {
        Supplier supplier = mock(Supplier.class);
        Consumer<UserProfile> consumer = mock(Consumer.class);
        int batchChunkSize = 2;
        UserProfile expected  = new UserProfile();
        when(supplier.get())
                .thenReturn(expected)
                .thenReturn(expected)
                .thenReturn(expected)
                .thenReturn(null);

        BatchListExecutor batchListExecutor = new BatchListExecutor(supplier,consumer,batchChunkSize);

        BatchReport batchRecord = batchListExecutor.execute();

        verify(supplier,atLeast(2)).get();
        verify(consumer,atLeastOnce()).accept(any());
        assertNotNull(batchRecord);
        assertEquals(3,batchRecord.countInput());
        assertEquals(3,batchRecord.countOutput());
    }


}