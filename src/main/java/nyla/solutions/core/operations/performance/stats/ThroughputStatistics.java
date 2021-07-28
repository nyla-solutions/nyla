package nyla.solutions.core.operations.performance.stats;

import nyla.solutions.core.util.Scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.LongAdder;

/**
 * <b>ThroughputStatistics</b> class supports calculating throughput statistics
 * @author Gregory Green
 */
public class ThroughputStatistics
{
    private final LongAdder counter = new LongAdder();

    public ThroughputStatistics()
    {
    }

    public ThroughputStatistics(long increment)
    {
        increment(increment);
    }

    public static double throughPutPerSecond(Iterable<ThroughputStatistics> stats, LocalDateTime start, LocalDateTime end)
    {
        long count =0;
        for (ThroughputStatistics stat:stats) {
            count+= stat.count();
        }

        double totalSeconds = Scheduler.durationSeconds(start,end);
        if(totalSeconds == 0)
            return count;

        return count/totalSeconds;
    }

    public void increment()
    {
        counter.increment();
    }

    public long count()
    {
        return counter.longValue();
    }

    public void increment(long l)
    {
        counter.add(l);

    }

    public long throughputPerMs(LocalDateTime start, LocalDateTime end)
    {
        long ms = Scheduler.durationMS(start,end);
        if(ms == 0)
            return counter.longValue();
        else
            return counter.longValue()/ms;
    }

    public double throughputPerSecond(LocalDateTime start, LocalDateTime end)
    {
        double secs = Scheduler.durationSeconds(start,end);
        if(secs == 0)
            return counter.longValue();
        else
            return counter.longValue()/secs;
    }

    /**
     * Reset to 0
     */
    public void reset()
    {
        counter.reset();
    }
}
