package nyla.solutions.core.patterns.workthread;

import nyla.solutions.core.io.watcher.FileWatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RunSchedulerTest {

    private RunScheduler subject;

    @BeforeEach
    void setUp() {
        subject = RunScheduler
                .builder()
                .initialDelayMs(0L)
                .awaitTimeoutSeconds(1L)
                .timeBetweenMs(100L).build();
    }



    @Test
    void schedulingLoops() throws InterruptedException {

        var counter = new AtomicLong(0L);
        subject = RunScheduler
                .builder()
                .initialDelayMs(0L)
                .awaitTimeoutSeconds(1L)
                .timeBetweenMs(100L).build();

        subject.schedule(() -> {;
            System.out.println("Running scheduled task");
            counter.getAndIncrement();
        });

        Thread.sleep(5000);

        assertThat(counter.get()).isGreaterThan(0);
    }

    @AfterEach
    void tearDown() {
        subject.close();
    }
}