package nyla.solutions.core.patterns.conversion.transformation;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class BatchListExecutorTest
{

    @Test
    public void test_process_reader_single()
    {
        Supplier supplier = Mockito.mock(Supplier.class);
        Consumer<UserProfile> consumer = Mockito.mock(Consumer.class);
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
        Supplier<String> supplier = Mockito.mock(Supplier.class);
        Consumer<UserProfile> consumer = Mockito.mock(Consumer.class);
        Function<String,UserProfile> processor = Mockito.mock(Function.class);

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
        Supplier supplier = Mockito.mock(Supplier.class);
        Consumer<UserProfile> consumer = Mockito.mock(Consumer.class);
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
        Supplier supplier = Mockito.mock(Supplier.class);
        Consumer<UserProfile> consumer = Mockito.mock(Consumer.class);
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