package nyla.solutions.core.patterns.workthread;

/**
 * 
 * <pre>
 * Worker interface for an object managed by the supervisor thread.
 * 
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface SupervisedWorker extends Worker
{
   /**
    * 
    * @return the worker's thread
    */
   public Thread getThread();
   
   /**
    * 
    * @param workState work state
    */
   public void setWorkState(WorkState workState);
   
   
   /**
    * 
    * @return the work state
    */
   public WorkState getWorkState();

   /**
    * 
    * @return the supervisor of the worker
    */
   public Supervisor getSupervisor();
   
   
   /**
    * 
    * @param supervisor the supervisor 
    */
   public void setSupervisor(Supervisor supervisor);
}
