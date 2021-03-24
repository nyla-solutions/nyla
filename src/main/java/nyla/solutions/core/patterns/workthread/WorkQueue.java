package nyla.solutions.core.patterns.workthread;

public interface WorkQueue
{
   /**
    * 
    * @return next task
    */
    public Runnable nextTask();
    
    
    /**
     * 
     * @return true if there are more tasks in the queue
     */
    public boolean hasMoreTasks();
    
    /**
     * 
     * @return size of queue
     */
    public int size();
}
