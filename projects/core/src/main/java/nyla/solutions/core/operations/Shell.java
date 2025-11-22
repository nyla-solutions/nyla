package nyla.solutions.core.operations;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Wrapper class to execute environment shell scripts
 * @author Gregory Green
 *
 */
public class Shell
{
	/**
	 * Constructor
	 */
	public Shell()
	{
		this((File)null,(File)null);
	}
	/**
	 * Constructor
	 * @param workingDirectory the working directory for script executions
	 * @param logFile output will be redirected to this file
	 */
	public Shell(String workingDirectory, File logFile)
	{
		this(Paths.get(workingDirectory).toFile(),
			logFile);
	}//------------------------------------------------
	/**
	 * Constructor
	 * @param workingDirectory the working directory
	 * @param logFile output will be redirected to this file
	 */
	public Shell(String workingDirectory, String logFile)
	{
		this(Paths.get(workingDirectory).toFile(),
			Paths.get(logFile).toFile());
	}//------------------------------------------------
	/**
	 * Constructor
	 * @param workingDirectory the working directory
	 * @param logFile output will be redirected to this file
	 */	
	public Shell(File workingDirectory, File logFile)
	{
		this.log = logFile;
		this.workingDirectory = workingDirectory;
	}
	/**
	 * 
	 * @param commands the commands to execute
	 * @return the process information
	 * @throws IOException
	 */
	public ProcessInfo execute(List<String>  commands)
	throws IOException
	{
		return execute(false,commands);
	}//------------------------------------------------
	/**
	 * 
	 * @param commands the commands to execute
	 * @return the process information
	 * 	 * @param background execute in background
	 * @throws IOException
	 */
	public ProcessInfo execute(boolean background,List<String>  commands)
	throws IOException
	{
		 ProcessBuilder pb = new ProcessBuilder();
		 
		 pb.command(commands);

		return executeProcess(background,pb);
	}
	/**
	 * Executes a giving shell command
	 * @param command the commands to execute
	 * @return process information handle
	 */
	public ProcessInfo execute(String...  command)
	{
	
		return execute(false,command);
		
	}
	/**
	 * Executes a giving shell command
	 * @param command the commands to execute
	 * @param background execute in background
	 * @return process information handle
	 */
	public ProcessInfo execute(boolean background,String...  command)
	{
		 ProcessBuilder pb = new ProcessBuilder(command);

		 
		return executeProcess(background,pb);
		
	}
	/**
	 * Executes a process
	 * 
	 * @param background if starting as background process
	 * @param pb the process builder
	 * @return the process information
	 * @throws IOException
	 */
	private ProcessInfo executeProcess(boolean background,ProcessBuilder pb) 
	{
		try
		{
			
			 pb.directory(workingDirectory);
			 pb.redirectErrorStream(false);
			
			if(log != null)
				pb.redirectOutput(Redirect.appendTo(log));
			
			pb.environment().putAll(envMap);
			Process p = pb.start();

			String out = null;
			String error = null;
			
			if(background)
			{
				out = IO.reader().readText(p.getInputStream());
				
				error = IO.reader().readText(p.getErrorStream());
			}
			else
			{
				out = IO.reader().readText(p.getInputStream());
				
				error = IO.reader().readText(p.getErrorStream());
				
			}
				
  	
			if(background)
				return new ProcessInfo(0, out, error);
			
			return new ProcessInfo(p.waitFor(),out,error);
		}
		catch (Exception e)
		{
			return new ProcessInfo(-1, null, Debugger.stackTrace(e));
		}
	}
	/**
	 * Hold executed process information
	 * @author Gregory Green
	 *
	 */
	public static final class ProcessInfo
	{
		
		public ProcessInfo(int exitValue, String output, String error)
		{
			super();
			this.exitValue = exitValue;
			this.output = output;
			this.error = error;
		}
		
		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("ProcessInfo [exitValue=").append(exitValue)
					.append(", output=").append(output).append(", error=")
					.append(error).append("]");
			return builder.toString();
		}
		

		/**
		 * 
		 * @return true is stand error or
		 */
		public boolean hasError()
		{
			return this.exitValue != 0 || (this.error != null && this.error.length() > 0);
		}
		public final int exitValue;
		public final String output, error; 
		
	}
	
    /**
     * Default value 20
	 * @return the defaultBackgroundReadSize
	 */
	public int getDefaultBackgroundReadSize()
	{
		return defaultBackgroundReadSize;
	}
	/**
	 * Default value 20
	 * @param defaultBackgroundReadSize the defaultBackgroundReadSize to set
	 */
	public void setDefaultBackgroundReadSize(int defaultBackgroundReadSize)
	{
		this.defaultBackgroundReadSize = defaultBackgroundReadSize;
	}
	/**
	 * Set an environment variable
	 * @param key the ENV variable name to set
	 * @param value the ENV variable values
	 */
	public void setEnvProperty(String key,String value)
	{
		this.envMap.put(key, value);
	}//------------------------------------------------
	/**
	 * 
	 * @param key the environment key to retrieve
	 * @return the value
	 */
	public String getEnvProperty(String key)
	{
		return this.envMap.get(key);
	}
	
	private final File workingDirectory;
    private final File log;
    private int defaultBackgroundReadSize = 20;
    private final HashMap<String,String> envMap = new HashMap<String,String>();
    
}
