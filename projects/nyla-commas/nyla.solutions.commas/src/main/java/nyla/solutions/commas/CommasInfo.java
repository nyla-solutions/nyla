package nyla.solutions.commas;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nyla.solutions.core.exception.SystemException;

/**
 * Commas information holder
 * @author Gregory Green
 *
 */
public class CommasInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1905929717562933034L;

	
	/**
	 * 
	 * @param commasName
	 */
	protected CommasInfo(String commasName )
	{
		this(commasName, new HashMap<String,CommandFacts>());
	}// --------------------------------------------------------
	
	/**
	 * Create the crate with the function and crate name
	 * @param crateName the crate name
	 * @param functionInfos the function information 
	 * 
	 */

	protected CommasInfo(String commasName, Map<String,CommandFacts> commandFactsMap )
	{
		this.commandFacties = commandFactsMap;

		this.commasName = commasName;

	}// -----------------------------------------------

	/**

	 * @return the functionInfos

	 */

	public Collection<CommandFacts> getCommandFacties()
	{
		return this.commandFacties.values();

	}// -----------------------------------------------

	/**
	 * @return the commasName
	 */
	public String getName()
	{
		return commasName;
	}// -----------------------------------------------

	/**
	 * @return the Command fact information for a given name
	 */
	//@Override
	public CommandFacts getFact(String commandName)
	{

		//build function name
		String fullCommandName = CommasServiceFactory.toCommandName(commasName, commandName);
		CommandFacts commandFacts = this.commandFacties.get(fullCommandName);
		

		if(commandFacts == null)
			throw new SystemException("Not found:"+fullCommandName+" keyKey:"+this.commandFacties.keySet());

		return commandFacts;
	}// -----------------------------------------------
	/**
	 * 
	 * @param facts
	 */
	public void addFact(CommandFacts facts)
	{
		commandFacties.put(facts.getName(), facts);
	}
	
	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CommasInfo [commandFacties=" + commandFacties + ", commasName="
				+ commasName + "]";
	}// --------------------------------------------------------



	private Map<String,CommandFacts> commandFacties;
	private String commasName;

}
