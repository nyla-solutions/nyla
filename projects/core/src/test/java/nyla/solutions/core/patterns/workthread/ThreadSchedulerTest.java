package nyla.solutions.core.patterns.workthread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ThreadSchedulerTest
{
    ThreadScheduler subject = new ThreadScheduler();
    @Test
    public void test_start_return_null()
    {

        assertNull(subject.startThreads());
    }


    @Test
    public void test_one_start()
    {
        ThreadScheduler subject = new ThreadScheduler();
        Collection<Thread> actual = subject.startThreads(() -> System.out.println("hello"));
        assertEquals(1,actual.size());
    }


    @Test
    public void test_all_started()
    throws InterruptedException
    {

        ArrayList<String> list = new ArrayList<>();

        Collection<Thread> actual = subject.startThreads(() -> list.add("hello"),
                () -> list.add("world"));
        Thread.sleep(5);
        assertEquals(2,actual.size());
    }

//    @Test
//    public void test_waitForThreads()
//    throws InterruptedException
//    {
//        Thread thread = mock(Thread.class);
//        subject.waitForThreads(Collections.singletonList(thread));
//
//        verify(thread).join();
//
//    }

    @Test
    public void test_waitForThreads_null()
    throws InterruptedException
    {
        assertEquals(0,subject.waitForThreads(null));
    }

}