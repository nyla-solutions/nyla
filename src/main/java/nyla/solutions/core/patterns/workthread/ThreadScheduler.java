package nyla.solutions.core.patterns.workthread;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Gregory Green
 */
public class ThreadScheduler
{
    public Collection<Thread> startThreads(Runnable... runnables)
    {

        if(runnables == null || runnables.length == 0)
            return null;

        ArrayList<Thread> list = new ArrayList<>(runnables.length);
        Thread thread= null;
        for (Runnable runnable: runnables)
        {
            if(runnable == null)
                continue;

            thread = new Thread(runnable);
            thread.start();
            list.add(thread);
        }

        if(list.isEmpty())
            return null;

        return list;
    }

    public int waitForThreads(Collection<Thread> threads)
    throws InterruptedException
    {
        if(threads == null || threads.isEmpty())
            return 0;

        for (Thread thread: threads)
        {
            thread.join();
        }

        return threads.size();
    }
}
