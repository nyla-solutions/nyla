package nyla.solutions.core.patterns.workthread;

import java.util.ConcurrentModificationException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * <pre>
 * MemorizedQueue provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class MemorizedQueue implements WorkQueue
{
   private final ArrayBlockingQueue<Runnable> queue;

   public MemorizedQueue(int capacity){
      queue = new ArrayBlockingQueue<>(capacity);
   }
   public MemorizedQueue(Runnable... runners){
      this(runners.length);
      this.add(runners);
   }

   public synchronized void add(Runnable... tasks)
   {
      for (Runnable runner:tasks) {
         try {
            this.queue.put(runner);
         } catch (InterruptedException e) {
            throw new ConcurrentModificationException(e);
         }
      }
   }

   public synchronized Runnable nextTask()
   {
         return queue.poll();
   }

   /**
    * 
    * @see nyla.solutions.core.patterns.workthread.WorkQueue#hasMoreTasks()
    */
   public boolean hasMoreTasks()
   {
    
      return !queue.isEmpty();
   }// --------------------------------------------

   /**
    * @return stack size
    * @see nyla.solutions.core.patterns.workthread.WorkQueue#size()
    */
   public int size()
	{
		return queue.size();
	}


}
