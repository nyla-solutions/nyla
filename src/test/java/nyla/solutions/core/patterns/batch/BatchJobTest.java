package nyla.solutions.core.patterns.batch;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BatchJobTest
{

    @Nested
    class WhenExecute
    {
        @DisplayName("Given single record Then read/process/write 1 record")
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

            BatchJob batchJob = BatchJob.builder().supplier(supplier)
                                        .consumer(consumer).batchChunkSize(batchChunkSize).processor(
                            processor).build();

            BatchReport batchRecord = batchJob.execute();

            verify(supplier,atLeastOnce()).get();
            verify(consumer,atLeastOnce()).accept(any());
            verify(processor,atLeastOnce()).apply(any());
            assertNotNull(batchRecord);
            assertEquals(1,batchRecord.countInput());
        }

        @Test
        void when_procesor_is_null_consumer_is_still_called()
        {
            UserProfile expectedUser = new JavaBeanGeneratorCreator<>(UserProfile.class)
                    .randomizeAll().create();

            Supplier<UserProfile> supplier = mock(Supplier.class);
            when(supplier.get()).thenReturn(expectedUser).thenReturn(null);

            Consumer<List<UserProfile>> consumer = mock(Consumer.class);
            BatchJob batchJob = BatchJob.builder().supplier(supplier)
                                        .consumer(consumer)
                                       .build();

            BatchReport batchRecord = batchJob.execute();
            verify(consumer).accept(anyList());
        }
        @Test
        void when_consumer_throws_Exception()
        {
            Supplier<UserProfile> supplier = mock(Supplier.class);

            assertThrows(RequiredException.class, () -> BatchJob.builder().supplier(supplier)
                                                   .build());

        }
        @Test
        void when_supplier_throws_Exception()
        {
            Consumer<UserProfile> consumer = mock(Consumer.class);

            assertThrows(RequiredException.class, () -> BatchJob.builder().consumer(consumer)
                                                                .build());

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

            BatchJob batchJob = new BatchJob(supplier,consumer,batchChunkSize);

            BatchReport batchRecord = batchJob.execute();

            verify(supplier,atLeastOnce()).get();
            verify(consumer,atLeastOnce()).accept(any());
            assertNotNull(batchRecord);
            assertEquals(1,batchRecord.countInput());
        }

        @Nested
        @DisplayName("Given Closeable Supplier")
        class CloseableSupplierQa implements Supplier<UserProfile>, java.io.Closeable
        {
            private boolean isclosed=false;

            @Test
            public void given_closeable_then_close()
            {
                CloseableSupplierQa supplier = new CloseableSupplierQa();
                Consumer<UserProfile> consumer = mock(Consumer.class);
                int batchChunkSize = 2;
                BatchJob batchJob = new BatchJob(supplier,consumer,batchChunkSize);
                batchJob.execute();
                assertTrue(supplier.isclosed);
            }

            @Override
            public void close() throws IOException
            {
                isclosed = true;

            }

            @Override
            public UserProfile get()
            {
                return null;
            }
        }

        @Nested
        @DisplayName("Given Closeable Consumer")
        class CloseableConsumerQa implements Consumer<List<UserProfile>>, java.io.Closeable
        {
            private boolean isclosed=false;

            @Test
            public void given_closeable_then_close()
            {
                Supplier supplier = mock(Supplier.class);
                CloseableConsumerQa consumer = new CloseableConsumerQa();
                int batchChunkSize = 2;
                BatchJob batchJob = new BatchJob(supplier,consumer,batchChunkSize);
                batchJob.execute();
                assertTrue(consumer.isclosed);
            }

            @Override
            public void close() throws IOException
            {
                isclosed = true;

            }

            @Override
            public void accept(List<UserProfile> list)
            {
            }
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

            BatchJob batchJob = new BatchJob(supplier,consumer,batchChunkSize,processor);

            BatchReport batchRecord = batchJob.execute();
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

            BatchJob batchJob = new BatchJob(supplier,consumer,batchChunkSize);

            BatchReport batchRecord = batchJob.execute();

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

            BatchJob batchJob = new BatchJob(supplier,consumer,batchChunkSize);

            BatchReport batchRecord = batchJob.execute();

            verify(supplier,atLeast(2)).get();
            verify(consumer,atLeastOnce()).accept(any());
            assertNotNull(batchRecord);
            assertEquals(3,batchRecord.countInput());
            assertEquals(3,batchRecord.countOutput());
        }


    }


}