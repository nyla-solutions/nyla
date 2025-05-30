package nyla.solutions.commas;

import java.util.Collection;

/**
 * 
 * @author Gregory Green
 *
 */
public interface Catalog
{
	/**
	 * The information form all commands in a commas service
	 * @param commasName the service
	 * @return the commas info
	 */
	public CommasInfo getCommasInfo(String commasName);
	
	public Collection<CommasInfo> getCommasInfos(String commasName);

	/**
	 * Get the facts
	 *  @param commandName the command name
	 * @return the command facts
	 */
	public CommandFacts getCommandFacts(String commandName);
	
	/**
	 * Get the facts
	 * @param commasName service/commas name
	 * @param commandName the command name
	 * @return the command facts
	 */
	public CommandFacts getCommandFacts(String commasName,String commandName);
}
