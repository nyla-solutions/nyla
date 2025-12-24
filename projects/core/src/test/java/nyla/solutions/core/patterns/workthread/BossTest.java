package nyla.solutions.core.patterns.workthread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BossTest
{

    @Test
    void startRunnable() throws ExecutionException, InterruptedException
    {
        Runnable runnable = null;

        int expectedSize = 3;
        int count = 3;
        Boss subject = new Boss(count);

        ArrayList<String> list = new ArrayList<>();

        Future<?> actual = subject.startWorking(() -> list.add(""));
        assertNotNull(actual);

        actual = subject.startWorking(() -> list.add(""));
        assertNotNull(actual);

        actual = subject.startWorking(() -> list.add(""));
        assertNotNull(actual);

        actual.get();

        assertEquals(expectedSize, list.size());

    }

    @Test
    public void startWorkQueue() throws ExecutionException, InterruptedException
    {
        int expectedSize = 3;
        int count = 3;
        Boss subject = new Boss(count);

        ArrayList<String> list = new ArrayList<>();

        MemorizedQueue queue = new MemorizedQueue(3);
        queue.add(() -> list.add(""));
        queue.add(() -> list.add(""));
        queue.add(() -> list.add(""));

        ArrayList<Future<?>> futures = (ArrayList<Future<?>>) subject.startWorking(queue, false);
        assertNotNull(futures);
        assertEquals(3, futures.size());

        for (Future<?> future : futures)
        {
            future.get();
        }

        assertEquals(expectedSize, list.size());

    }


    @Test
    void startWorkingByadding() throws ExecutionException, InterruptedException {

        Boss boss = new Boss(3);

        AtomicInteger counter = new AtomicInteger(0);
        var out = boss.startWorking(() -> {
            // Task 1
            counter.incrementAndGet();
        });

        out.get();

        assertThat(counter.get()).isEqualTo(1);


    }
}