package nyla.solutions.core.patterns.iteration;

import java.util.Collection;

public interface Paging<T> extends Collection<T>
{
	/**
	 * @return the pageCriteria
	 */
	public PageCriteria getPageCriteria();

	
	/**
	 * 
	 * @return true if this is the last page
	 */
	public boolean isLast();
	
	/**
	 * 
	 * @param last is last records
	 */
    public void setLast(boolean last);
    
	/**
	 * 
	 * @return true if this is the first page
	 */
	public boolean isFirst();
	
	/**
	 * @param first boolean if this is the first page
	 */
	public void setFirst(boolean first);
	

}