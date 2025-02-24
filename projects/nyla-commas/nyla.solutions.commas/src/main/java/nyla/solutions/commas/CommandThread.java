/**
 * 
 */
package nyla.solutions.commas;

import nyla.solutions.core.exception.RequiredException;

/**
 * @author Gregory Green
 *
 */
public class CommandThread<ReturnType,InputType> extends Thread implements Runnable, Command<ReturnType,InputType>
{
	public CommandThread()
	{
		
	}// ------------------------------------------------
	public CommandThread(Command<ReturnType,InputType> cmd, InputType argument)
	{
		this.command = cmd;
		this.argument =  argument;
	}// ------------------------------------------------

	/* (non-Javadoc)
	 * @see solutions.global.patterns.command.Command#execute(java.lang.Object)
	 */
	public ReturnType execute(InputType source)
	{
		if (this.command == null)
			throw new RequiredException("this.command");
		
		return this.command.execute(this.argument);
	}// ------------------------------------------------

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		if(this.command == null)
			    	throw new RequiredException("this.command");

		results = this.command.execute(argument);
	}// ------------------------------------------------
	
	/**
	 * @return the results
	 */
	public Object getResults()
	{
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(ReturnType results)
	{
		this.results = results;
	}

	/**
	 * @return the argument
	 */
	public Object getArgument()
	{
		return argument;
	}

	/**
	 * @param argument the argument to set
	 */
	public void setArgument(InputType argument)
	{
		this.argument = argument;
	}

	/**
	 * @return the command
	 */
	public Command<ReturnType,InputType> getCommand()
	{
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(Command<ReturnType,InputType> command)
	{
		this.command = command;
	}

	private ReturnType results = null;
	private InputType argument = null;
	private Command<ReturnType,InputType> command = null;

}
