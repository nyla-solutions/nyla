package nyla.solutions.core.patterns.conversion;

import java.util.Map;

/**
 * Used to get a fixed key from a map
 * @author Gregory Green
 *
 */
public class NamedMappedEntryByKeyConverter implements NameableConverter<Map<?,?>,Object>
{

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public Object convert(Map<?, ?> sourceMap)
	{
		if(sourceMap == null)
			return null;
		
		return sourceMap.get(key);
	}
	
	/**
	 * @return the key
	 */
	public Object getKey()
	{
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Object key)
	{
		this.key = key;
	}

	private String name;
	private Object key;

}
	
