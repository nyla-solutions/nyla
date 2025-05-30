package nyla.solutions.commas;

import java.util.Collection;

public interface Shell 
{
	/**
	 * Crate name
	 */
	public String getName();
	
	/**
	 * 
	 * @param functionName the function to retrieve
	 * @return the function
	 */
	public <ReturnType,InputType> Command<ReturnType,InputType> getCommand(String commandName);
	
	/**
	 * Execute a function call
	 * @param functionName the function to execute
	 * @param request the input argument to the function
	 * @return the return object of the function call
	 */
	public Object executeCommand(String commandName, Object request);
	
	/**
	 * 
	 * @param functionName the function to retrieve
	 * @return the function
	 */
	public <ReturnType,InputType>  Command<ReturnType,InputType>  getCommand(String commmanName, Object context);
	
	/**
	 * 
	 * @return list all functions for this service
	 */
	public Collection<Command<?,?>> getCommands();	
}
