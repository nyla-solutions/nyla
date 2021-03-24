package nyla.solutions.core.data.expiration;

import java.time.LocalDateTime;

/**
 * 
 * @author Gregory Green
 *
 * Holds an item that can expire
 * @param <V> the class value of the expiring data
 */
public class ExpiringItem<V>
{

	public ExpiringItem(V value, LocalDateTime expiration)
	{
		this.expiration = expiration;
		this.value = value;
	}

	public boolean isExpired()
	{
		if(LocalDateTime.now().isAfter(expiration))
			return true;
		
		return false;
	}
	
	public V value()
	{
		if(this.isExpired())
			return null;
		
		return value;
	}

	private final LocalDateTime expiration;
	private final V value;
}
