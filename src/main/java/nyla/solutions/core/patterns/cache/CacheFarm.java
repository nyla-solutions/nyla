package nyla.solutions.core.patterns.cache;

import java.util.*;

/**
 * Implementation of the Cache interface
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Gregory Green
 */
public class CacheFarm<K, V> implements Cache<K, V>, Cloneable
{
    private static Cache<Object, Object> cache = null;
    private final WeakHashMap<K, V> map;

	public CacheFarm(WeakHashMap<K, V> map)
	{
		this.map = map;
	}

	public CacheFarm()
	{
		this(new WeakHashMap<>());
	}

	/**
     * @see java.util.Hashtable#clear()
     */
    public void clear()
    {
        map.clear();
    }

    /**
     * @return cloned object
     * @see java.util.Hashtable#clone()
     */
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * @param value
     * @return boolean if value exists
     * @see java.util.Hashtable#contains(java.lang.Object)
     */
    public boolean contains(Object value)
    {
        return map.containsValue(value);
    }

    /**
     * @param key the key to check
     * @return boolean if key exists
     * @see java.util.Hashtable#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    /**
     * @param value
     * @return boolean if value exists
     * @see java.util.Hashtable#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }

    /**
     * @return enumeration of elements
     * @see java.util.Hashtable#elements()
     */
    public Enumeration<V> elements()
    {
        return new Hashtable<K,V>(this.map).elements();
    }

    /**
     * @return set of entries
     * @see java.util.Hashtable#entrySet()
     */
    public Set<Entry<K, V>> entrySet()
    {
        return map.entrySet();
    }// --------------------------------------------------------

    public boolean equals(Object o)
    {
        return map.equals(o);
    }

    /**
     * @param key
     * @return object value
     * @see java.util.Hashtable#get(java.lang.Object)
     */
    public V get(Object key)
    {
        return map.get(key);
    }

    /**
     * @return map hash code
     * @see java.util.Hashtable#hashCode()
     */
    public int hashCode()
    {
        return map.hashCode();
    }

    /**
     * @return is empty boolean
     * @see java.util.Hashtable#isEmpty()
     */
    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    /**
     * @return enumeration of keys
     * @see java.util.Hashtable#keys()
     */
    public Enumeration<K> keys()
    {
        return new Hashtable<K,V>(this.map).keys();
    }

    /**
     * @return the key set for the map
     * @see java.util.Hashtable#keySet()
     */
    public Set<K> keySet()
    {
        return map.keySet();
    }

    /**
     * @param key   key
     * @param value value
     * @return previous value
     * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
     */
    public V put(K key, V value)
    {
        return map.put(key, value);
    }

    /**
     * @param m the map
     * @see java.util.Hashtable#putAll(java.util.Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m)
    {
        this.map.putAll(m);
    }

    /**
     * @param key
     * @return the removed value
     * @see java.util.Hashtable#remove(java.lang.Object)
     */
    public V remove(Object key)
    {
        return map.remove(key);
    }

    /**
     * @return the map size
     * @see java.util.Hashtable#size()
     */
    public int size()
    {
        return map.size();
    }

    public String toString()
    {
        return map.toString();
    }

    /**
     * @return the map values
     * @see java.util.Hashtable#values()
     */
    public Collection<V> values()
    {
        return map.values();
    }//---------------------------------------------

    /**
     * @param <K> the key
     * @param <V> the value
     * @return singleton instance cache
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache()
    {
        if (cache == null)
            cache = new CacheFarm<>();

        return (Cache<K, V>) cache;

    }//---------------------------------------------
}
