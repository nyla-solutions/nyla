package nyla.solutions.core.patterns.workthread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * <b>Boss</b> work thread controls
 * <p>
 * Sample code
 * MemorizedQueue queue = new MemorizedQueue();
 * Runnable task1 = new Runnable()
 * {
 * public void run()
 * {
 * try{ Thread.sleep(100); } catch(Exception e){}
 * <p>
 * System.out.println("I LOVE Queen Sheba task1");
 * }
 * };
 * <p>
 * <p>
 * Runnable task2 = new Runnable()
 * {
 * public void run()
 * {
 * try{ Thread.sleep(200); } catch(Exception e){}
 * System.out.println("I LOVE Queen Sheba task2");
 * }
 * };
 * <p>
 * <p>
 * Runnable task3 = new Runnable()
 * {
 * public void run()
 * {
 * try{ Thread.sleep(300); } catch(Exception e){}
 * System.out.println("I LOVE Queen Sheba task3");
 * }
 * };
 * <p>
 * queue.add(task1);
 * queue.add(task2);
 * queue.add(task3);
 * <p>
 * <p>
 * Boss boss = new Boss(queue);
 * <p>
 * WorkerThread worker1 = new WorkerThread(boss);
 * WorkerThread worker2 = new WorkerThread(boss);
 * WorkerThread worker3 = new WorkerThread(boss);
 * <p>
 * boss.manage(worker1);
 * boss.manage(worker2);
 * boss.manage(worker3);
 * <p>
 * StartState startState = new StartState();
 * boss.setWorkState(startState);
 *
 * @author Gregory Green
 */
public class Boss implements Supervisor
{
    private WorkState workState = null;
    private WorkQueue workQueue = null;
    private String name = getClass().getName();
    private Collection<SupervisedWorker> workers = new HashSet<SupervisedWorker>();

    /**
     * Constructor for Boss initializes internal
     * data settings.
     */
    public Boss()
    {
    }// --------------------------------------------

    /**
     * @param workQueue
     */
    public Boss(WorkQueue workQueue)
    {
        this.setWorkQueue(workQueue);
    }// --------------------------------------------

    /**
     * @see nyla.solutions.core.patterns.workthread.Supervisor#getWorkers()
     */
    public Collection<SupervisedWorker> getWorkers()
    {
        return new ArrayList<SupervisedWorker>(this.workers);
    }// --------------------------------------------

    /**
     * Manage a number of default worker threads
     *
     * @param workersCount the worker count
     */
    public void manage(int workersCount)
    {
        this.workers.clear();

        WorkerThread worker = null;
        for (int i = 0; i < workersCount; i++)
        {
            worker = new WorkerThread(this); //TODO expensive use thread pool
            this.manage(worker);
        }

    }// --------------------------------------------

    /**
     * Add thread to managed list
     *
     * @param workers the supervised workers
     */
    public void manage(Collection<SupervisedWorker> workers)
    {
        if (workers == null)
            return;

        this.workers.clear();

        SupervisedWorker worker = null;
        for (Iterator<SupervisedWorker> i = workers.iterator(); i.hasNext(); )
        {
            worker = i.next();

            this.manage(worker);
        }

    }// --------------------------------------------

    /**
     * Start workers by setting the start to "StartState"
     *
     * @param workCount the work count
     */
    public void startWorkers(int workCount)
    {
        manage(workCount);
        this.setWorkState(new StartState());
    }// --------------------------------------------

    /**
     * Start workers by setting the start to "StartState"
     */
    public void startWorkers()
    {
        this.setWorkState(new StartState());
    }// --------------------------------------------

    /**
     * Stop workers by setting the state to "StopState"
     */
    public void stopWorkers()
    {
        this.setWorkState(new StopState());
    }// --------------------------------------------

    /**
     * Add thread to managed list
     *
     * @param worker the supervised worker
     */
    public void manage(SupervisedWorker worker)
    {
        if (worker == null)
            throw new IllegalArgumentException("worker required in Boss.manage");

        worker.setSupervisor(this);

        this.workers.add(worker);
    }// --------------------------------------------

    public WorkQueue getWorkQueue()
    {
        return this.workQueue;
    }// --------------------------------------------


    /**
     * call workstate.adverse(workers)
     *
     * @see nyla.solutions.core.patterns.workthread.Supervisor#setWorkState(nyla.solutions.core.patterns.workthread.WorkState)
     */
    public void setWorkState(WorkState workState)
    {
        this.workState = workState;

        SupervisedWorker worker = null;
        for (Iterator<SupervisedWorker> i = workers.iterator(); i.hasNext(); )
        {
            worker = (SupervisedWorker) i.next();
            workState.advise(worker);
        }

        //threads
        Thread workerThread = null;

        for (Iterator<SupervisedWorker> i = workers.iterator(); i.hasNext(); )
        {
            worker = i.next();
            workerThread = worker.getThread();
            if (workerThread != null)
            {
                try
                {
                    workerThread.join();
                }
                catch (Exception e)
                {
                }
            }
        }
    }// --------------------------------------------

    /**
     * @see nyla.solutions.core.patterns.workthread.SupervisedWorker#setSupervisor(nyla.solutions.core.patterns.workthread.Supervisor)
     */
    public void setSupervisor(Supervisor supervisor)
    {
    }// --------------------------------------------


    /**
     * @see nyla.solutions.core.patterns.workthread.SupervisedWorker#getSupervisor()
     */
    public Supervisor getSupervisor()
    {
        return null;
    }// --------------------------------------------


    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }// --------------------------------------------


    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        if (name == null)
            name = "";

        this.name = name;
    }// --------------------------------------------

    /**
     * @see nyla.solutions.core.patterns.workthread.SupervisedWorker#getThread()
     */
    public Thread getThread()
    {
        return Thread.currentThread();
    }// --------------------------------------------

    /**
     * @param workQueue the workQueue to set
     */
    public void setWorkQueue(WorkQueue workQueue)
    {
        if (workQueue == null)
            throw new IllegalArgumentException(
                    "workQueue required in Boss.setWorkQueue");

        this.workQueue = workQueue;
    }// --------------------------------------------

    public void run()
    {
    }// --------------------------------------------

    /**
     * @see nyla.solutions.core.patterns.workthread.SupervisedWorker#getWorkState()
     */
    public WorkState getWorkState()
    {
        return workState;
    }// --------------------------------------------


}
