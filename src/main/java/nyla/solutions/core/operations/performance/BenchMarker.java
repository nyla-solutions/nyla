package nyla.solutions.core.operations.performance;

import nyla.solutions.core.util.stats.MathematicStats;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Gregory Green
 */
public class BenchMarker
{
    private final int threadCount;
    private final long threadSleepMs;
    private final int rampUPSeconds;
    private final Long loopCount;
    private final Long threadLifeTimeSeconds;
    private ExecutorService executorService;

    private BenchMarker(int threadCount, long threadSleepMs, int rampUPSeconds, Long loopCount, Long threadLifeTimeSeconds)
    {
        this.threadCount = threadCount;
        this.threadSleepMs = threadSleepMs;
        this.executorService = Executors.newFixedThreadPool(threadCount);
        this.rampUPSeconds = rampUPSeconds;
        this.loopCount = loopCount;
        this.threadLifeTimeSeconds = threadLifeTimeSeconds;
    }

    protected void setExecutorService(ExecutorService executorService)
    {
        this.executorService = executorService;
    }

    public void setMathematicsStats(MathematicStats mockMath)
    {
    }

    public static class BenchMarkerBuilder
    {
        private int threadCount;
        private long threadSleepMs;
        private int rampUPSeconds;
        private Long loopCount;
        private Long threadLifeTimeSeconds;

        public BenchMarkerBuilder threadCount(int threadCount)
        {
            this.threadCount = threadCount;
            return this;
        }

        public BenchMarkerBuilder rampUPSeconds(int rampUPSeconds)
        {
            this.rampUPSeconds = rampUPSeconds;
            return this;
        }

        public BenchMarkerBuilder loopCount(Long loopCount)
        {
            this.loopCount = loopCount;
            return this;
        }

        public BenchMarkerBuilder threadLifeTimeSeconds(Long threadLifeTimeSeconds)
        {
            this.threadLifeTimeSeconds = threadLifeTimeSeconds;
            return this;
        }

        public BenchMarkerBuilder threadSleepMs(long threadSleepMs)
        {
            this.threadSleepMs = threadSleepMs;
            return this;

        }

        public BenchMarker build()
        {
            return new BenchMarker(threadCount, threadSleepMs,
                    rampUPSeconds, loopCount, threadLifeTimeSeconds);
        }


    }

    public static BenchMarkerBuilder builder()
    {
        return new BenchMarkerBuilder();
    }

    public void measure(Runnable runnable, Consumer<? extends Number>... reporters)
    throws InterruptedException
    {
        for (int i = 0; i < threadCount; i++)
        {
            this.executorService.execute(
                    () ->
                    {

                        try
                        {
                            executeBenchMark(runnable, reporters);

                        }
                        catch (InterruptedException e)
                        {
                            throw new RuntimeException(e.getMessage(), e);
                        }


                    }
            );

            if (this.rampUPSeconds > 0)
                Thread.sleep(this.rampUPSeconds * 1000);
        }

        if (this.threadLifeTimeSeconds != null)
        {
            this.executorService.shutdown();
            this.executorService.awaitTermination(this.threadLifeTimeSeconds,
                    TimeUnit.SECONDS);
        }
    }

    protected void executeBenchMark(Runnable runnable, Consumer<? extends Number>... reporters)
    throws InterruptedException
    {
        for (int loopIndex = 0; loopIndex < this.loopCount; loopIndex++)
        {
            long start = System.nanoTime();
            runnable.run();
            long end = System.nanoTime();
            if (reporters != null)
            {
                for (Consumer reporter : reporters)
                {
                    long out = end - start;
                    reporter.accept(out);
                }
            }


            Thread.sleep(threadSleepMs);
        }
    }

    public int getThreadCount()
    {
        return threadCount;
    }

    public int getRampUPSeconds()
    {
        return rampUPSeconds;
    }

    public Long getLoopCount()
    {
        return loopCount;
    }

    public Long getThreadLifeTimeSeconds()
    {
        return threadLifeTimeSeconds;
    }

    public long getThreadSleepMs()
    {
        return threadSleepMs;
    }
}
