package nyla.solutions.commas;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of a CommasDAO
 * @author Gregory Green
 *
 */
public class MapCommandFactsDAO implements CommandFactsDAO
{
	
	/**
	 * 
	 * @see nyla.solutions.commas.CommandFactsDAO#findCommandByKey(java.lang.String)
	 */
	public CommandFacts findFactsByKey(String key)
	{
		
		return mapCommandFacts.get(key);
	}// --------------------------------------------------------

	/**
	 * 
	 * @see nyla.solutions.commas.CommandFactsDAO#saveByKey(java.lang.String, nyla.solutions.commas.Command)
	 */
	public CommandFacts saveByKey(String key, CommandFacts value)
	{
		return  mapCommandFacts.put(key, value);
	}// --------------------------------------------------------
	
	@Override
	public Set<String> selectFactKeys()
	{
		return mapCommandFacts.keySet();
	}

	private Map<String,CommandFacts> mapCommandFacts = new HashMap<String,CommandFacts>();
}
