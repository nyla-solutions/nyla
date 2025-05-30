package nyla.solutions.core.patterns.loadbalancer;

import java.util.Collection;

/**
 * Implement round robin/other balancing for call distributions and routing.
 * 
 * @author Gregory Green
 *
 * @param <K>
 * @param <V>
 */
public interface LoadBalanceRegistry<K,V>
{
	/**
	 * Lookup item registered with key or return new register previously registered based on balancing
	 * @param key the object key
	 * @return the object of the 
	 */
	V lookup(K key);
	
	void register(K key, V associated );
	
	void register(V associated );
	
	/**
	 * 
	 * @return all registered locations
	 */
	Collection<V> listRegistered();
	
	/**
	 * Remote all keys and registration for the association
	 * @param associated
	 */
	void unregister(V associated);
	
	/**
	 * 
	 * @return next available location
	 */
	V next();
}
