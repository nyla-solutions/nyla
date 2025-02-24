package nyla.solutions.commas;

import java.util.Set;


/**
 * DAO for Command facts
 * @author Gregory Green
 *
 */
public interface CommandFactsDAO
{
	/**
	 * 
	 * @param key the command's key
	 * @return the located Command
	 */
	public CommandFacts findFactsByKey(String key);

	/**
	 * 
	 * @param key the command key
	 * @param facts the command facts
	 * @return the command facts
	 */
	public CommandFacts saveByKey(String key, CommandFacts facts);

	/**
	 * 
	 * @return collection of all facts
	 */
	public Set<String> selectFactKeys();
	
}
