package nyla.solutions.core.data.expiration;

import nyla.solutions.core.util.Config;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import static nyla.solutions.core.util.Config.settings;

/**
 * 
 * @author Gregory Green
 *
 * A look hash table with expiring items
 * @param <K> the lookup key class
 * @param <V> the lookup value class
 */
public class ExpiringKeyValueLookup<K,V>
{
	private static int INIT_SIZE = settings().getPropertyInteger(ExpiringKeyValueLookup.class, "INIT_SIZE",20);
	private ConcurrentHashMap<K, ExpiringItem<V>> map = new ConcurrentHashMap<>(INIT_SIZE);
	private final long expirationMilliseconds;
	private long maxSize=0;
	
	/**
	 * Constructor with expiring milliseconds
	 * @param expirationMilliseconds the expiration by milliseconds
	 */
	private ExpiringKeyValueLookup(long expirationMilliseconds)
	{
		this.expirationMilliseconds = expirationMilliseconds; 
	}//------------------------------------------------
	
	/**
	 * 
	 * @param <K> the key class
	 * @param <V> the value class
	 * @param milliseconds the expiration in milliseconds
	 * @return the new create instance
	 */
	public static <K,V> ExpiringKeyValueLookup<K, V> withExpirationMS(long milliseconds)
	{
		return new ExpiringKeyValueLookup<>(milliseconds);
	}//------------------------------------------------
	
	public V getValue(K key)
	{
		ExpiringItem<V> i = this.map.get(key);
		
		if(i == null)
			return null;
		
		if(i.isExpired())
			this.map.remove(key);
		
		return i.value(); //will return value if expired;
	}//------------------------------------------------
	
	public void putEntry(K key, V value)
	{
		if(this.maxSize > 0 && this.size() >= this.maxSize)
		{
			this.map.remove(this.map.keySet().iterator().next());
		}
		ExpiringItem<V> i = new ExpiringItem<V>(value, 
		LocalDateTime.now().plusNanos(1000000*this.expirationMilliseconds));
		
		this.map.put(key, i);
	}


	public long getMaxSize()
	{
		return maxSize;
	}

	public void setMaxSize(long maxSize)
	{
		this.maxSize = maxSize;
	}

	public long size()
	{
		return this.map.size();
	}
}
