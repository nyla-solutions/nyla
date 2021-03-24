package nyla.solutions.core.data;

import java.util.Map;

public class MapEntry<K, V> implements Map.Entry<K, V>
{

	public MapEntry(K key, V value)
	{
		super();
		this.key = key;
		this.value = value;
	}//------------------------------------------------
	
	/**
	 * @return the key
	 */
	public K getKey()
	{
		return key;
	}
	/**
	 * @return the value
	 */
	public V getValue()
	{
		return value;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(K key)
	{
		this.key = key;
	}

	/**
	 * @param value the value to set
	 * @return the set value
	 */
	public V setValue(V value)
	{
		this.value = value;
		
		return this.value;
	}

	private  K key;
	private  V value;

}
