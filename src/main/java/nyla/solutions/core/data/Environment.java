package nyla.solutions.core.data;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Environment
{
	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		map.clear();
	}

	/**
	 * @param key
	 * @return boolean if key is in environment
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	/**
	 * @param value
	 * @return boolean if value is in environment
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}

	/**
	 * @param o the other object
	 * @return true if objects are equal
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		return map.equals(o);
	}

	/**
	 * @param <T> the type of teh key
	 * @param key
	 * @return the object by the key
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key)
	{
		return (T)map.get(key);
	}

	/**
	 * @return map.hashCode()
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode()
	{
		return map.hashCode();
	}

	/**
	 * @return boolean if empty
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	/**
	 * @return the key set
	 * @see java.util.Map#keySet()
	 */
	public Set<Object> keySet()
	{
		return map.keySet();
	}

	/**
	 * @param key the key
	 * @param value the value
	 * @return previous object
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value)
	{
		return map.put(key, value);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<Object,Object> m)
	{
		map.putAll(m);
	}

	/**
	 * @param key the key
	 * @return the removed object
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key)
	{
		return map.remove(key);
	}

	/**
	 * @return the size of the environments
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return map.size();
	}

	/**
	 * @return map.values();
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values()
	{
		return map.values();
	}

	
	/**
	 * @return the map
	 */
	public Map<Object,Object> getMap()
	{
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<Object,Object> map)
	{
		this.map = map;
	}

	

	public String[] getArgs()
	{
		if(args == null)
			return null;
		
		return args.clone();
	}

	public void setArgs(String[] args)
	{
		if(args == null)
			this.args = null;
		else
			this.args = args.clone();
	}



	private Map<Object,Object> map = new Hashtable<Object,Object>();
	private String[] args;
}
