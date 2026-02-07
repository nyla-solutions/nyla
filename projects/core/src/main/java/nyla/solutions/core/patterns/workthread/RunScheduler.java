package nyla.solutions.core.patterns.workthread;

import java.io.Closeable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Gregory Green
 */
public class RunScheduler implements Closeable {
    private final ScheduledExecutorService scheduler;
    private final long initialDelayMs;
    private final long timeBetweenMs;
    private final long awaitTimeoutSeconds;

    public RunScheduler(ScheduledExecutorService scheduledExecutorService, long initialDelayMs, long timeBetweenMs, long awaitTimeoutSeconds) {
        this.scheduler = scheduledExecutorService;
        this.initialDelayMs = initialDelayMs;
        this.timeBetweenMs = timeBetweenMs;
        this.awaitTimeoutSeconds = awaitTimeoutSeconds;
    }

    public static SchedulerBuilder builder() {
        return new SchedulerBuilder();
    }

    public void schedule(Runnable runner) {

        scheduler.scheduleAtFixedRate(runner,
                initialDelayMs, timeBetweenMs, TimeUnit.MILLISECONDS);

    }

    public void close() {
        if(!scheduler.isShutdown()) {
            try {
                scheduler.awaitTermination(awaitTimeoutSeconds, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class SchedulerBuilder {

        private long initialDelayMs;
        private long timeBetweenMs;
        private ScheduledExecutorService schedule;
       private final static int defaultThreadPoolSize = 2;
        private long awaitTimeoutSeconds = 1;

       public SchedulerBuilder initialDelayMs(long initialDelayMs) {
            this.initialDelayMs = initialDelayMs;
            return this;
        }
        public SchedulerBuilder schedule(ScheduledExecutorService schedule) {
            this.schedule = schedule;
            return this;
        }

        public SchedulerBuilder awaitTimeoutSeconds(long awaitTimeoutSeconds) {
           this.awaitTimeoutSeconds = awaitTimeoutSeconds;
            return this;
        }

        public SchedulerBuilder timeBetweenMs(long timeBetweenMs) {
            this.timeBetweenMs = timeBetweenMs;
            return this;
        }

        public RunScheduler build() {
            if(this.schedule == null)
                this.schedule = java.util.concurrent.Executors.newScheduledThreadPool(defaultThreadPoolSize);

            return new RunScheduler(this.schedule, initialDelayMs,timeBetweenMs,awaitTimeoutSeconds);
        }
    }
}
