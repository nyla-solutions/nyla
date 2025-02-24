package nyla.solutions.commas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.RequiredException;

/**
 * @author Gregory Green
 *
 */
public class ShellCommands implements Shell
{
	/**
	 * 
	 * @param crateName the crate name
	 */
	protected ShellCommands(String shellName,String commandName, Command<?,?> command)
	{
		commandMap = new HashMap<String,Command<?,?>>();
		this.name = shellName;
		this.commandMap.put(commandName,command);
	}// -----------------------------------------------
	/**
	 * 
	 * @param crateName the crate name
	 */
	protected ShellCommands(String shellName, Map<String,Command<?,?>> commandMap)
	{
		if (shellName == null)
			throw new RequiredException("shellName");
		
		this.name = shellName;
		
		this.commandMap = commandMap;
	}// ----------------------------------------------
	/**
	 * Get the function by name and call's execute method
	 * @param request the input function
	 */
	@Override
	public Object executeCommand(String commandName, Object request)
	{
		return this.getCommand(commandName).execute(request);
		
	}// -----------------------------------------------
	/**
	 * @return the crate name
	 */
	@Override
	public String getName()
	{
		return name;
	}// -----------------------------------------------
	
	@Override
	public <ReturnType, InputType> Command<ReturnType, InputType> getCommand(
			String commandName )
	{
		return getCommand(commandName,null);
	}// --------------------------------------------------------
	/**
	 * Get the function by name in the crate
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <ReturnType, InputType> Command<ReturnType,InputType> getCommand(String commandName, Object context)
	{
		if (commandName == null || commandName.length() == 0)
			throw new RequiredException("commandName");
		
		commandName = CommasServiceFactory.toCommandName(this.name,commandName);
		
		Command<ReturnType,InputType> command = (Command<ReturnType,InputType>)commandMap.get(commandName);
		
		if(command == null)
		{
			throw new NoDataFoundException("commandName:"+commandName+" in keySet"+this.commandMap.keySet());
		}

		return command;
	}// -----------------------------------------------
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CrateFunctions [commandMap=" + commandMap + ", name=" + name
				+ "]";
	}// --------------------------------------------------------
	/**
	 * 
	 * @param name
	 * @param command
	 */
	protected void addCommand(String name, Command<?,?> command)
	{
		commandMap.put(name, command);
	}// --------------------------------------------------------

	@Override
	public Collection<Command<?, ?>> getCommands()
	{
		return commandMap.values();
	}

	private final Map<String,Command<?,?>> commandMap;
	private final String name;

}
