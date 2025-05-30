package nyla.solutions.commas.file;
import nyla.solutions.commas.Command;
import nyla.solutions.core.exception.FatalException;
import nyla.solutions.core.util.Debugger;

/**
 * Use debugger to log input.
 * 
 * This object will inspect the input to the execute method 
 * to log the appropriate category of FATAL, ERROR, etc log levels.
 * @author Gregory Green
 *
 */
public class LoggingCommand implements Command<Object, Object>
{
	/**
	 * 
	 * @see nyla.solutions.global.patterns.command.Command#execute(java.lang.Object)
	 */
	@Override
	public Object execute(Object input)
	{
		
		if(input instanceof FatalException)
		{
			Debugger.printFatal(input);
		}
		else if(input instanceof Exception)
		{
			Debugger.printError(input);
		}
		else
		{
			if(defaultInfo)
				Debugger.printInfo(input);
			else
				Debugger.println(input);
		}
		
		return null;
	}// --------------------------------------------------------

	
	/**
	 * @return the defaultInfo
	 */
	public boolean isDefaultInfo()
	{
		return defaultInfo;
	}


	/**
	 * @param defaultInfo the defaultInfo to set
	 */
	public void setDefaultInfo(boolean defaultInfo)
	{
		this.defaultInfo = defaultInfo;
	}


	private boolean defaultInfo;
	

}
