package nyla.solutions.core.patterns.loadbalancer;

import java.util.*;

/**
 * 
 * Use the next method to retrieve items in a round robin fashion
 * 
 * @author Gregory Green
 *
 * @param <T> the type to round robin
 */
public class RoundRobin<T>
{
	private final Deque<T> dq;

	public RoundRobin()
	{
		// Initialize the Deque. This might be at your class constructor.

		dq = new ArrayDeque<T>();
	}// --------------------------------------------------------
	/**
	 * 
	 * @param item the item to add 
	 * @return whether the item was removed
	 */
	public boolean add(T item)
	{
		if(item == null)
			return false;

		if(dq.contains(item))
			return false;
		
		return dq.add(item);
	}// --------------------------------------------------------
	public boolean addAll(Collection<T> items)
	{
		boolean wasAllAdded = true;
		for (T item : items)
		{
			if(!add(item))
				wasAllAdded = false;
		}
		
		return wasAllAdded;
	}// --------------------------------------------------------
	/**
	 * 
	 * @return next item based on round robin schedule
	 */
	public T next()
	{
		if(dq == null || dq.isEmpty())
			return null;
					
		T item = dq.removeFirst(); // Remove the host from the top
		   
		 dq.addLast(item); // Put the host back at the end
		    
		    return item;
	}// --------------------------------------------------------
	/**
	 * 
	 * @return the collection
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> toCollection()
	{
		HashSet<Object> set = new HashSet<Object>();
		
		set.addAll((Collection<?>)Arrays.asList(dq.toArray()));
		
		 return (Collection<T>)set;
	}// --------------------------------------------------------

	/**
	 *
	 * @param item the item to remove
	 * @return true if item was removed
	 */
	public boolean remove(T item)
	{
		return this.dq.remove(item);
	}// --------------------------------------------------------

	public boolean addAll(T... items)
	{
		if(items == null)
			return false;

		return this.addAll(Arrays.asList(items));
	}
}
