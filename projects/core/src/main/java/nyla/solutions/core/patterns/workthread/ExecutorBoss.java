package nyla.solutions.core.patterns.workthread;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.core.util.Debugger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static nyla.solutions.core.util.Config.settings;


/**
 * 
 * <pre>
 * <b>Boss</b> work thread controls
 * <h2>Sample code with Callables<h2>
 * {@code
 * ArrayList<Callable<Serializable>> callQueue = new ArrayList<Callable<Serializable>>();
				 
				 for (String location : locations)
				 {
					 remoteCommand = RMI.lookup(new URI(location));
					 
					 callQueue.add(new RemoteCommandProcessor<Serializable,Envelope<Serializable>>(remoteCommand, env));
				 }
				 
				 //start all processing
				 
				 ExecutorBoss boss = new ExecutorBoss(callQueue.size());
				 
				Collection<Serializable> results = boss.startWorking(callQueue);
 * 
 * <h2>Sample code with Runnables</h2>
 * MemorizedQueue queue = new MemorizedQueue();
      Runnable task1 = new Runnable()
      {
         public void run()
         {
            try{ Thread.sleep(100); } catch(Exception e){}
            
            System.out.println("I LOVE Queen Sheba task1");
         }   
      };
      
      queue.add(task1);

      
      Runnable task2 = new Runnable()
      {
         public void run()
         {
            try{ Thread.sleep(200); } catch(Exception e){}
            System.out.println("I LOVE Queen Sheba task2");
         }   
      };
      
       queue.add(task2);
      
      Runnable task3 = new Runnable()
      {
         public void run()
         {
            try{ Thread.sleep(300); } catch(Exception e){}
            System.out.println("I LOVE Queen Sheba task3");
         }   
      };

       queue.add(task3);
     
      
      ExecutorBoss boss = new ExecutorBoss(3);
      
      boss.startWorking(queue);
    }
   </pre>
 * @author Gregory Green
 *
 */
public class ExecutorBoss implements Disposable
{

    private final ExecutorService executor;
    private final int workerCount;

  private static ExecutorBoss instance = null;
	/**
	 * DEFAULT_WORK_COUNT = Config.getPropertyInteger(ExecutorBoss.class,"DEFAULT_WORK_COUNT",10).intValue()
	 */
	public static final int DEFAULT_WORK_COUNT = settings().getPropertyInteger(ExecutorBoss.class,"DEFAULT_WORK_COUNT",10).intValue();

	public ExecutorBoss(int workerCount)
	{
		try
		{
			executor = Executors.newFixedThreadPool(workerCount);
			this.workerCount = workerCount;
		}
		catch(IllegalArgumentException e)
		{
			throw new RequiredException("workerCount:"+workerCount+" ERROR"+e.getMessage());
		}
	}// --------------------------------------------------------
	/**
	 * Start the array of the callables
	 * @param <T> the type class
	 * @param callables of callables
	 * @return the collection of returned object from the callables
	 */
	 public <T> Collection<T> startWorking(Callable<T>[] callables)
	 {
		    List<Future<T>> list = new ArrayList<Future<T>>();

		    for (int i = 0; i < callables.length; i++)
		    {
		    	list.add(executor.submit(callables[i]));
		    }


		    ArrayList<T> resultList = new ArrayList<T>(callables.length);

		    // Now retrieve the result
		    T output;
		    for (Future<T> future : list)
		    {
		      try
		      {
		    	  output = future.get();
		    	  if(output != null)
		    		resultList.add(output);
		      }
		      catch (InterruptedException e)
		      {
		        throw new SystemException(e);
		      }
		      catch (ExecutionException e)
		      {
		    	  throw new SystemException(e);
		      }
		    }


		  return resultList;
	 }// --------------------------------------------------------

		 @SuppressWarnings("unchecked")
		public <T,I> Collection<T> startWorking(Collection<Callable<I>> callables)
		 {
			    List<Future<I>> list = new ArrayList<Future<I>>();

			    for (Callable<I> callable : callables)
			    {
			    	list.add(executor.submit(callable));
			    }


			    ArrayList<T> resultList = new ArrayList<T>(callables.size());

			    // Now retrieve the result
			    I output;
			    for (Future<I> future : list)
			    {
			      try
			      {
			    	  output = future.get();

			    	  if(output != null)
			    		  resultList.add((T)output);
			      }
			      catch (InterruptedException e)
			      {
			        throw new SystemException(e);
			      }
			      catch (ExecutionException e)
			      {
			    	  Throwable cause =  e.getCause();
			    	  if(cause == null)
			    		  cause = e;
			    	  if(cause instanceof RuntimeException)
			    		  throw (RuntimeException)cause;

			    	  throw new SystemException(cause);
			      }
			    }

			  return resultList;
		 }// --------------------------------------------------------

		 /**
		  * The start the work threads in foreground
		  *  @param queue the queue
		  *  @return the collection of futures
		 */
		 public Collection<Future<?>> startWorking(WorkQueue queue)
		 {
			 return startWorking(queue,false);
		 }

	 /**
	  * The start the work threads
	  *  @param queue the queue
	  *  @param background determine to while for futures to complete
	  *  @return the collection of futures
	 */
	 public Collection<Future<?>> startWorking(WorkQueue queue, boolean background)
	 {
		 ArrayList<Future<?>> futures = new ArrayList<Future<?>>(queue.size());

		    while(queue.hasMoreTasks())
		    {

		    	futures.add(executor.submit(queue.nextTask()));
			}

		    if(background)
		    	return futures;

		    try
			{
				for (Future<?> future : futures)
				{
					future.get(); //join submitted thread

				}
				return futures;
			}
			catch (InterruptedException e)
			{
				throw new SystemException(e);
			}
			catch (ExecutionException e)
			{
				throw new SystemException(e);
			}

   }// --------------------------------------------------------
	public Future<?> startWorking(Worker worker)
    {
	   return executor.submit(worker);
	 }// --------------------------------------------------------

	/**
	 * Start working the runnable in the pool
	 * @param runnable the run implementation
	 * @return the future of the submitted execution
	 */
	public Future<?> startWorking(Runnable runnable)
	{
		return executor.submit(runnable);
	}
	/**
	 * Shutdown executor
	 * @see nyla.solutions.core.patterns.Disposable#dispose()
	 */
	public void dispose()
	{
	    // This will make the executor accept no new threads
	    // and finish all existing threads in the queue
	    try{ executor.shutdown(); } catch(Exception e){Debugger.printWarn(e);}
	}


   /**
	 * @return the workerCount
	 */
	public int getWorkerCount()
	{
		return workerCount;
	}

	/**
	 *
	 * @return the singleton executor boss
	 */
	public static synchronized  ExecutorBoss getBoss()
	{
		if(instance == null)
			instance = new ExecutorBoss(DEFAULT_WORK_COUNT);

		return instance;
	}


}
