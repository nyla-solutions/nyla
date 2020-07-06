package nyla.solutions.core.patterns.workthread;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import  org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class ThreadSchedulerTest
{
    ThreadScheduler subject = new ThreadScheduler();
    @Test
    public void test_start_return_null()
    {

        assertNull(subject.startThreads(null));
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