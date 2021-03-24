package nyla.solutions.core.operations.logging;

import nyla.solutions.core.util.Debugger;

public class SystemOutLog implements Log
{
	private static final String DEBUG_LEVEL = "DEBUG";
	private static final String INFO_LEVEL = "INFO";
	private static final String WARN_LEVEL = "WARN";
	private static final String ERROR_LEVEL = "ERROR";
	private static final String FATAL_LEVEL = "FATAL";


	@Override
	public void setLoggingClass(Class<?> aClass)
	{
	}//--------------------------------------------------------

	@Override
	public void debug(Object message)
	{
		print(false,DEBUG_LEVEL,message,null);

	}//--------------------------------------------------------

	@Override
	public void debug(Object message, Throwable t)
	{
		print(false,DEBUG_LEVEL,message,t);

	}//--------------------------------------------------------
	
	/**
	 * 
	 * @param level the log level
	 * @param message the message
	 */
	private void print(boolean useStdError, String level,Object message, Throwable t)
	{
		StringBuilder print = new StringBuilder(level).append(": ").append(String.valueOf(message));
		
		if(t != null)
		{
			print.append(" stacktrace:").append(Debugger.stackTrace(t));
		}
		
		if(!useStdError)
			System.out.println(print.toString());
		else
			System.err.println(print.toString());
	}//--------------------------------------------------------

	@Override
	public void info(Object message)
	{
		print(false,INFO_LEVEL,message,null);

	}

	@Override
	public void info(Object message, Throwable t)
	{
		print(false,INFO_LEVEL,message,t);

	}

	@Override
	public void error(Object message)
	{
		print(true,ERROR_LEVEL,message,null);

	}

	@Override
	public void error(Object message, Throwable t)
	{
		print(true,ERROR_LEVEL,message,t);
	}

	@Override
	public void fatal(Object message)
	{
		print(true,ERROR_LEVEL,message,null);

	}

	@Override
	public void fatal(Object message, Throwable t)
	{
		print(true,FATAL_LEVEL,message,t);

	}//--------------------------------------------------------

	@Override
	public void warn(Object message)
	{
		print(true,WARN_LEVEL,message,null);

	}//--------------------------------------------------------

	@Override
	public void warn(Object message, Throwable t)
	{
		print(true,WARN_LEVEL,message,t);

	}//--------------------------------------------------------

}
