package nyla.solutions.commas;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.FatalException;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;


/**
 * Main executable to run one or more tasks.
 * 
 * Set the JVM properties -Dsolutions.global.patterns.command.MainExecutable=beanName
 * @author Gregory Green
 *
 */
public class MainExecutable implements Executable
{
	/**
	 * Execute executables with name that matches this class
	 * @param args the first argument may contain the 
	 */
	public Integer execute(Environment env)
	{	
	   String executableName = (String)env.get(MainExecutable.class.getSimpleName());
	   if(executableName == null || executableName.length() == 0)
		   executableName = mainExecutable;
	   
		try
		{
		   Debugger.println(this,"executableName="+executableName);
		   
			Executable executable = (Executable)ServiceFactory.getInstance().create(executableName);
			
			executable.execute(env);
			
			return 0;
		} 
		catch (RuntimeException e)
		{			
			Debugger.printError(e);
			throw e;
		}
	}//---------------------------------------------
	public static void main(String[] args)
	{
		try 
		{
			MainExecutable program = new MainExecutable();
			
			Environment env = new Environment();
			
			env.setArgs(args);
			
			env.putAll(System.getProperties());
			
			program.execute(env);
			
		} 
		catch (Exception e) 
		{
			Debugger.printError(e);
			throw new FatalException(e);
			
		}		
	}//---------------------------------------------
	
	/**
	 * @return the mainExecutable
	 */
	public String getMainExecutable()
	{
		return mainExecutable;
	}//---------------------------------------------
	/**
	 * @param mainExecutable the mainExecutable to set
	 */
	public void setMainExecutable(String mainExecutable)
	{
		this.mainExecutable = mainExecutable;
	}//---------------------------------------------

	private String mainExecutable = Config.getProperty(this.getClass(),"mainExecutable",this.getClass().getSimpleName());

}
