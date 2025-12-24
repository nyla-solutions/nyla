package nyla.solutions.core.patterns.workthread;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BossTest
{
    private Boss subject;
    private final int expectedSize = 3;

    @BeforeEach
    void setUp() {
        subject = new Boss(expectedSize);
    }

    @Test
    void startWorking_CallableNull_Removed() {
        Collection<Callable<Object>> callables = List.of(
                ()-> null,
                () -> "MustExist"

        );
        var actual = subject.startWorking(callables);

        assertThat(actual.size()).isEqualTo(1);
    }

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
    void dispose() {
        assertDoesNotThrow(() -> subject.dispose());
    }

    @Test
    void getBoss() {
        var boss = Boss.getBoss();

        assertThat(boss).isNotNull();
        assertThat(boss.getWorkerCount())
                .isEqualTo(Boss.DEFAULT_WORK_COUNT);

    }

    @Test
    void getWorkerCount() {
        assertThat(subject.getWorkerCount()).isEqualTo(expectedSize);
    }

    @Test
    public void startWorkQueue() throws ExecutionException, InterruptedException
    {

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
    void startWorkingListOfCallables() {
        Object expected ="expected";
        Callable<?> callable = () -> expected;

        var actual = subject.startWorking(List.of(callable));

        assertThat(actual).isEqualTo(List.of(expected));
    }


    @Test
    void startWorkingArrayOfCallables() {
        Object expected ="expectedArray";
        Callable<Object> callable = () -> expected;

        var actual = subject.startWorking(new Callable[]{callable});

        assertThat(actual).isEqualTo(List.of(expected));
    }


    @Test
    void startWorkingByWithRunnable() throws ExecutionException, InterruptedException {

        AtomicInteger counter = new AtomicInteger(0);
        var out = subject.startWorking(() -> {
            // Task 1
            counter.incrementAndGet();
        });

        out.get();

        assertThat(counter.get()).isEqualTo(1);

    }

    @Test
    public void startThreads_null()
    {

        assertNull(subject.startThreads());
    }


    @Test
    public void startThreads_with_only_1()
    {
        Collection<Thread> actual = subject.startThreads(() -> System.out.println("hello"));
        assertEquals(1,actual.size());
    }


    @Test
    public void startThreads_all_started()
            throws InterruptedException
    {

        ArrayList<String> list = new ArrayList<>();

        Collection<Thread> actual = subject.startThreads(() -> list.add("hello"),
                () -> list.add("world"));
        Thread.sleep(5);
        assertEquals(2,actual.size());
    }


    @Test
    public void waitForThreads_null()
            throws InterruptedException
    {
        assertEquals(0,subject.waitForThreads(null));
    }
}