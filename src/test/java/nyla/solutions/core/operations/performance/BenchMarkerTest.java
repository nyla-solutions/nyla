package nyla.solutions.core.operations.performance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

class BenchMarkerTest
{
    private int threadCount = 2;
    private int rampUPSeconds = 1;
    private Long loopCount = 1L;
    private Long threadLifeTimeSeconds = null;
    private BenchMarker subject;
    private ExecutorService mockExecutiveService;
    private Runnable mockRunner;
    private Consumer<Long> mockConsumer;
    private long threadSleepMs = 0L;

    @BeforeEach
    public void setUp()
    {
        subject =
                BenchMarker
                        .builder()
                        .threadCount(threadCount)
                        .threadSleepMs(threadSleepMs)
                        .rampUPSeconds(rampUPSeconds)
                        .loopCount(loopCount)
                        .threadLifeTimeSeconds(threadLifeTimeSeconds)
                        .build();

        mockRunner = mock(Runnable.class);
        mockConsumer = mock(Consumer.class);
        mockExecutiveService = mock(ExecutorService.class);
        subject.setExecutorService(mockExecutiveService);
    }

    @Test
    void measure()
    throws InterruptedException
    {

        assertNotNull(subject);
        assertEquals(threadCount, subject.getThreadCount());
        assertEquals(rampUPSeconds, subject.getRampUPSeconds());
        assertEquals(loopCount, subject.getLoopCount());
        assertEquals(threadLifeTimeSeconds, subject.getThreadLifeTimeSeconds());
        assertEquals(threadSleepMs, subject.getThreadSleepMs());


        subject.measure(mockRunner, mockConsumer);

        int expectedTimes = Long.valueOf(threadCount * loopCount)
                .intValue();

        verify(mockExecutiveService, times(expectedTimes)).execute(any(Runnable.class));
        verify(mockExecutiveService, never()).shutdown();
        verify(mockExecutiveService, never()).awaitTermination(anyLong(), any());

    }

//    @Test
//    void measure_threadLifeTimeSeconds()
//    throws InterruptedException
//    {
//
//        threadLifeTimeSeconds = 20L;
//
//        subject =
//                BenchMarker
//                        .builder()
//                        .threadCount(threadCount)
//                        .rampUPSeconds(rampUPSeconds)
//                        .loopCount(loopCount)
//                        .threadLifeTimeSeconds(threadLifeTimeSeconds)
//                        .build();
//        ExecutorService mock = mock(ExecutorService.class);
//        subject.setExecutorService(mock);
//        MathematicStats mockMath = mock(MathematicStats.class);
//
//
//        subject.measure(mockRunner,
//                (mockMath));
//
//
//        verify(mock).shutdown();
//        verify(mock).awaitTermination(anyLong(), any());
//
//    }

    @Test
    void executeBenchMark()
    throws InterruptedException
    {
        Runnable mockRunner = mock(Runnable.class);
        Consumer<Long> mockConsumer = mock(Consumer.class);
        subject.executeBenchMark(mockRunner,mockConsumer);

        verify(mockRunner).run();
        verify(mockConsumer).accept(any());
    }
}