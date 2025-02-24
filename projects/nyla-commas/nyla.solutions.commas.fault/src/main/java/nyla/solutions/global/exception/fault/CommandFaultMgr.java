package nyla.solutions.global.exception.fault;


import nyla.solutions.commas.Command;
import nyla.solutions.core.exception.fault.FaultException;
import nyla.solutions.core.exception.fault.FaultMgr;
import nyla.solutions.core.exception.fault.FaultService;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.email.EmailFaultCommand;


/**
 * By default the EmailFaultCommand is used.
 * 
 * @author Gregory Green
 *
 */
public class CommandFaultMgr extends FaultMgr implements FaultService
{
	/**
	 * Default constructore
	 */
	public CommandFaultMgr()
	{
	}// --------------------------------------------------------

	private void init() 
	throws IllegalAccessException, InstantiationException
	{
		if(this.command == null)
			command = getCommand();
	}// --------------------------------------------------------
	/**
	 * Throw the exception with wrapped information
	 * @param e the raise to wrap
	 */
	@Override
	public FaultException raise(Throwable e)
	{
	   
		FaultException faultException = super.raise(e);
		
		try
		{
			
			
			if(this.command == null)
				   init();
			
			this.command.execute(faultException);
			
			return faultException;
		}
		catch (Exception initException)
		{
			
			Debugger.printWarn(initException);
			return faultException;
		}
	}// --------------------------------------------------------
	/**
	 * Wrapper the exception with needed information
	 * @param e the exception to wrap
	 * @param argument the arguments
	 * 
	 */
	@Override
	public FaultException raise(Throwable e, Object argument)
	{
		   
		FaultException faultException = super.raise(e, argument);
		
		try
		{
			if(this.command == null)
				   init();
			  
			   this.command.execute(faultException);
		}
		
		catch (Exception e1)
		{
			// print warning
			Debugger.printWarn(e1);
		}
		  
		return faultException;
	}// --------------------------------------------------------

	public static Command<Object,Exception> getCommand() 
	throws IllegalAccessException, InstantiationException
	{
		return ClassPath.newInstance(COMMAND_CLASS_NAME);
	}// --------------------------------------------------------
	
	private static final String COMMAND_CLASS_NAME = Config.getProperty(CommandFaultMgr.class,"COMMAND_CLASS_NAME",EmailFaultCommand.class.getName());
	private Command<Object, Exception> command;

}
