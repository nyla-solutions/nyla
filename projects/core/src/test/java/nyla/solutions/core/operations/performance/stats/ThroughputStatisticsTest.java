package nyla.solutions.core.operations.performance.stats;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThroughputStatisticsTest
{
    @Test
    void reset()
    {
        ThroughputStatistics subject = new ThroughputStatistics();
        subject.increment(1000);

        LocalDateTime now = LocalDateTime.now();
        assertTrue(subject.throughputPerSecond(now, now.plusSeconds(1))>0);

        subject.reset();
        assertEquals(0,subject.throughputPerSecond(now, now.plusSeconds(1)));

    }

    @Test
    void increment_by_one()
    {
        ThroughputStatistics subject = new ThroughputStatistics();
        assertEquals(0L,subject.count());

        subject.increment();
        assertEquals(1L,subject.count());

        subject.increment();
        assertEquals(2L,subject.count());

    }

    @Test
    void increment_count()
    {
        ThroughputStatistics subject = new ThroughputStatistics();
        subject.increment(10L);
        assertEquals(10L,subject.count());
        subject.increment(30L);
        assertEquals(40L,subject.count());
    }

    @Test
    void throughputPerSeconds()
    {
        ThroughputStatistics subject = new ThroughputStatistics();

        long expected = 1000;
        LocalDateTime now = LocalDateTime.now();
        subject.increment(expected);

        assertEquals(Double.valueOf(expected),subject.throughputPerSecond(now,now.plusSeconds(1)));

    }
    @Test
    void throughputPerSeconds_GreaterThan1Secs()
    {
        ThroughputStatistics subject = new ThroughputStatistics();

        long expected = 1000;
        LocalDateTime now = LocalDateTime.now();
        subject.increment(expected);

        assertEquals(expected/2.0,subject.throughputPerSecond(now,now.plusSeconds(2)));

    }


    @Test
    void throughputPerMs() throws InterruptedException
    {
        ThroughputStatistics subject = new ThroughputStatistics();

        long expected = 1000;
        subject.increment(expected);

        LocalDateTime now = LocalDateTime.now();
        assertEquals(expected,subject.throughputPerMs(now,now.plus(1, ChronoUnit.MILLIS)));

    }

    @Test
    void throughputPerMs_GreaterThan1Ms() throws InterruptedException
    {
        ThroughputStatistics subject = new ThroughputStatistics();

        long expected = 1000;
        subject.increment(expected);

        LocalDateTime now = LocalDateTime.now();
        assertEquals(expected/2.0,subject.throughputPerMs(now,now.plus(2, ChronoUnit.MILLIS)));

    }

    @Test
    void aggregateThroughput()
    {
        List<ThroughputStatistics> stats = Arrays.asList(
                new ThroughputStatistics(1L),
                new ThroughputStatistics(2L),
                new ThroughputStatistics(3L));
        LocalDateTime now = LocalDateTime.now();
        assertEquals(6L,ThroughputStatistics.throughPutPerSecond(stats,now,now.plusSeconds(1)));
    }
}