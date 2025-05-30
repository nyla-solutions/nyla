package nyla.solutions.commas;

import java.util.Collection;


/**
 * Use CommasServiceFactory
 * @author Gregory Green
 *
 */
public class CatalogCommas implements Catalog
{

	/**
	 * @see nyla.solutions.commas.Catalog#getCommas(java.lang.String)
	 */
	public CommasInfo getCommasInfo(String commasName)
	{
		return CommasServiceFactory.getCommasServiceFactory().getCommasInfo(commasName);
	}
	/**
	 * Default commas INFO
	 * @see nyla.solutions.commas.Catalog#getCommas(java.lang.String)
	 */
	public CommasInfo getCommasInfo()
	{
		return CommasServiceFactory.getCommasServiceFactory().getCommasInfo();
	}// --------------------------------------------------------
	
	/**
	 * @see nyla.solutions.commas.Catalog#getCommasInfos(java.lang.String)
	 */
	public Collection<CommasInfo> getCommasInfos(String commasName)
	{
		
		return CommasServiceFactory.getCommasServiceFactory().getCommasInfos();
	}

	/**
	 * @see nyla.solutions.commas.Catalog#getCommandFacts(java.lang.String)
	 */
	public CommandFacts getCommandFacts(String commandName)
	{
		return CommasServiceFactory.getCommasServiceFactory().getCommandFacts(commandName);
	}

	/**
	 * @see nyla.solutions.commas.Catalog#getCommandFacts(java.lang.String, java.lang.String)
	 */
	public CommandFacts getCommandFacts(String commasName, String commandName)
	{
		
	 return CommasServiceFactory.getCommasServiceFactory().getCommandFacts(commasName, commandName);
	}

}
