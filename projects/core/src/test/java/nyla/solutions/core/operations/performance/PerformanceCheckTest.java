package nyla.solutions.core.operations.performance;

import nyla.solutions.core.patterns.decorator.TextDecorator;
import nyla.solutions.core.util.stats.MathematicStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class PerformanceCheckTest
{
    private BenchMarker marker;
    private int capacity = 10;
    private TextDecorator<MathematicStats> decorator;
    private MathematicStats stats;
    private PerformanceCheck subject;


    @BeforeEach
    void setUp()
    {
        decorator = mock(TextDecorator.class);
        marker = mock(BenchMarker.class);
        stats = mock(MathematicStats.class);

        subject = new PerformanceCheck(marker,stats, decorator);
    }

    @Test
    void verify_decorator_set()
    {
        verify(decorator).setTarget(stats);
    }

    @Test
    void perfCheck() throws InterruptedException
    {
        Runnable r = () -> {};
        subject.perfCheck(r);
        verify(marker).measure(any(),any());
    }

    @Test
    void real_test()
    {
        System.out.println(Double.NaN);
        BenchMarker marker = BenchMarker.builder()
                                        .loopCount(2L)
                .threadCount(2)
                .threadSleepMs(1)
                .rampUPSeconds(1).threadLifeTimeSeconds(2L)
                .build();

        subject = new PerformanceCheck(marker, capacity);

        Runnable r = () -> {
            try{ Thread.sleep(100);} catch (Exception e){ e.printStackTrace();}
        };

        subject.perfCheck(r);

        System.out.println("report:"+subject.getReport());
    }

    @Test
    void getText()
    {
        String expected = "expected";
        when(decorator.getText()).thenReturn(expected);
        String actual = subject.getReport();
        verify(decorator).getText();
        assertEquals(expected,actual);
    }
}