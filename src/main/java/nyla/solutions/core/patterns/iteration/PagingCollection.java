package nyla.solutions.core.patterns.iteration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Keep items with a fixed size
 * @author Gregory Green
 *
 * @param <T> the Type of the item in the size
 */
public class PagingCollection<T> implements Paging<T>, Serializable, Collection<T>
{
	private final Collection<T> collection;
	private final PageCriteria pageCriteria;
	private static final long serialVersionUID = 7655893737037419650L;
	private boolean last;
	private boolean first;


	/**
	 * Set the collection
	 * @param collection
	 * @param pageCriteria the page criteria
	 */
	public PagingCollection(Collection<T> collection, PageCriteria  pageCriteria)
	{
		if(collection == null)
			collection = new ArrayList<T>();
		
		this.collection = collection;
				
		this.pageCriteria = pageCriteria;

		//Set last
		if(pageCriteria == null || pageCriteria.getBeginIndex() <= 1)
			this.first = true;
	}// --------------------------------------------------------

	
	/**
	 * @return the pageCriteria
	 */
	public PageCriteria getPageCriteria()
	{
		return pageCriteria;
	}
	
	/**
	 * @return the last
	 */
	public boolean isLast()
	{
		return last;
	}

	/**
	 * @param last the last to set
	 */
	public void setLast(boolean last)
	{
		this.last = last;
	}

	/**
	 * @return the first
	 */
	public boolean isFirst()
	{
		return first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(boolean first)
	{
		this.first = first;
	}

	
	/**
	 * @param value the 
	 * @return boolean if the object were added
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(T value)
	{
		if(value == null)
			return false;

		if(this.pageCriteria != null && 
		   this.size() > this.pageCriteria.getSize())
		{
			this.last = false;
			return false; // do not add
			
		}
		
		return collection.add(value);
	}// --------------------------------------------------------
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addAll(Collection<? extends T> values)
	{
		boolean wasAllAdded = true;
		for (Iterator iterator = values.iterator(); iterator.hasNext();)
		{
			if(!add((T)iterator.next()))
				wasAllAdded = false;
			
		}
		
		return wasAllAdded;
	}

	/**
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear()
	{
		collection.clear();
	}//------------------------------------------------
	
	public boolean contains(Object arg0)
	{
		return collection.contains(arg0);
	}//------------------------------------------------
	
	public boolean containsAll(Collection<?> arg0)
	{
		return collection.containsAll(arg0);
	}//------------------------------------------------
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagingCollection<?> other = (PagingCollection<?>) obj;
		if (collection == null)
		{
			if (other.collection != null)
				return false;
		}
		else if (!collection.equals(other.collection))
			return false;
		if (first != other.first)
			return false;
		if (last != other.last)
			return false;
		if (pageCriteria == null)
		{
			if (other.pageCriteria != null)
				return false;
		}
		else if (!pageCriteria.equals(other.pageCriteria))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + (first ? 1231 : 1237);
		result = prime * result + (last ? 1231 : 1237);
		result = prime * result + ((pageCriteria == null) ? 0 : pageCriteria.hashCode());
		return result;
	}
	
	public boolean isEmpty()
	{
		if(collection == null)
			return true;
		
		return collection.isEmpty();
	}//------------------------------------------------
	public Iterator<T> iterator()
	{
		if(this.collection == null)
			return null;
		
		return collection.iterator();
	}//------------------------------------------------
	
	public boolean remove(Object arg0)
	{
		return collection.remove(arg0);
	}//------------------------------------------------
	
	public boolean removeAll(Collection<?> arg0)
	{
		return collection.removeAll(arg0);
	}//------------------------------------------------
	public boolean retainAll(Collection<?> arg0)
	{
		return collection.retainAll(arg0);
	}

	/**
	 * the size of the pages counts
	 * @return pagination size
	 */
	public int size()
	{
		return collection.size();
	}

	/**
	 * @return java.util.Collection#toArray()
	 */
	public Object[] toArray()
	{
		return collection.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] arg0)
	{
		return collection.toArray(arg0);
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PagingCollection [collection=" + collection + ", pageCriteria="
				+ pageCriteria + ", last=" + last + ", first=" + first + "]";
	}



}
