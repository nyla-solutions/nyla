package nyla.solutions.commas.file;

import java.io.File;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.FatalException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SummaryException;
import nyla.solutions.core.patterns.observer.Subject;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.patterns.observer.Topic;
import nyla.solutions.core.patterns.workthread.Boss;
import nyla.solutions.core.patterns.workthread.MemorizedQueue;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;


/**
 * Implementation of the file executable based on the worker thread design pattern
 * @author Gregory Green
 *
 */
public class WorkerThreadFileExecutable extends FileExecutable implements SubjectObserver
{
	public WorkerThreadFileExecutable()
	{
		//
		errorSubject = new Topic();
		errorSubject.add(this);
	}//---------------------------------------------
	/**
	 * 
	 */
	public Integer execute(Environment env)
	{
		super.execute(env);
		
		Boss boss = new Boss(workQueue);
		
		boss.startWorkers(workerCount);
		
		if(!summaryException.isEmpty())
			throw summaryException;
		
		return 0;
	}//---------------------------------------------
	/**
	 * Created a file runner worker thread
	 */
	protected void processDocument(File file)
	{		
		if(this.workQueue == null)
			throw new RequiredException("this.workQueue in ThreadedFileExecutable");
		
		workQueue.add(new FileCommandRunner(super.getFileCommand(),file,this.errorSubject));
	}//---------------------------------------------
	/**
	 * Implements the subject observer interface
	 * Add exceptions to the summary exception object
	 */
	public void update(Subject subject, Object data)
	{
		synchronized (summaryException) 
		{
			if(data instanceof Throwable)
			{
				summaryException.addException(new FatalException(Debugger.stackTrace((Throwable)data)));
			}
		}
		 
	}//---------------------------------------------
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}//---------------------------------------------
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	private static SummaryException summaryException = new SummaryException();
	private String id = this.getClass().getName();
	private Subject errorSubject = null;
	private MemorizedQueue workQueue = new MemorizedQueue();
	private int workerCount = Config.getPropertyInteger(this.getClass(),"workerCount").intValue();
}
