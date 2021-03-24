package nyla.solutions.core.patterns.workthread;

import java.util.Stack;

/**
 * <pre>
 * MemorizedQueue provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class MemorizedQueue implements WorkQueue
{
   public synchronized void add(Runnable tasks)
   {
      
      this.queue.add(tasks);
   }// --------------------------------------------
   public synchronized Runnable nextTask()
   {
         return (Runnable)queue.pop();
   }// --------------------------------------------
   
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
   private Stack<Runnable> queue = new Stack<Runnable>();

}
