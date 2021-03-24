package nyla.solutions.core.patterns.workthread;

import java.util.Collection;

/**
 * 
 * <pre>
 * Supervisor manages a list of workers
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface Supervisor extends SupervisedWorker
{
   /**
    * Add worker to the manage's supervisor
    * @param worker the worker thread
    */
    public void manage(SupervisedWorker worker);

    public Collection<SupervisedWorker> getWorkers();

    
    public WorkQueue getWorkQueue();
    
    
    
}