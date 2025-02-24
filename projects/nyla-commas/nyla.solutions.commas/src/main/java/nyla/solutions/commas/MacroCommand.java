package nyla.solutions.commas;



import java.util.Collection;
import java.util.Iterator;

import nyla.solutions.core.util.Debugger;

/**
 * This macro implementation for the command pattern
 * @author Gregory Green
 *
 */
public class MacroCommand<ReturnType,InputType> implements Command<ReturnType,InputType>
{	
	/**
	 * Default constructor
	 */
	public MacroCommand()
	{
	}// --------------------------------------------------------
	/**
	 * Default constructor
	 * @param collection the collection of commands
	 */
	public MacroCommand(Collection<Command<ReturnType,InputType>> collection)
	{
		this.setCommands(collection);
	}// --------------------------------------------------------
	/**
	 * Loop thru the commands and return the end results.
	 * Each the argument to each execute of the 
	 * @param source the input object to transform
	 * @return the altered object
	 */
	public ReturnType execute(InputType input)
	{
		if(commands == null || commands.isEmpty())
		{
			Debugger.printInfo("Source is commands empty. Returning null.");
			return null;
		}	    
		
		//loop through transformers
		ReturnType result = null;
		Command<ReturnType,InputType> cmd = null;
		for(Iterator<Command<ReturnType,InputType>> i = commands.iterator();i.hasNext();)
		{
			cmd = i.next();
			result = cmd.execute(input);
		}
		return result;
	}// --------------------------------------------
	

	/**
	 * @return the commands
	 */
	public Collection<Command<ReturnType, InputType>> getCommands()
	{
		return commands;
	}


	/**
	 * @param commands the commands to set
	 */
	public void setCommands(Collection<Command<ReturnType, InputType>> commands)
	{
		this.commands = commands;
	}


	private Collection<Command<ReturnType,InputType>> commands = null;
}
