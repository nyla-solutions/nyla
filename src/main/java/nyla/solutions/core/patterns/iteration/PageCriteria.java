package nyla.solutions.core.patterns.iteration;

import nyla.solutions.core.data.Identifier;

import java.io.Serializable;

/**
 *   @author Gregory Green
 */
public class PageCriteria implements Identifier, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5876945310432037628L;

	private String id = null;
	private int beginIndex = 1;
	private int size;
	private boolean savePagination;
	private String className;

	/**
	 * Default constructor
	 */
	public PageCriteria()
	{
	}// --------------------------------------------------------


	public PageCriteria(int beginIndex, int size)
	{
		super();
		if(beginIndex < 1)
			throw new IllegalArgumentException("beginIndex:"+beginIndex+" < 1s");
		if(size < 1)
			throw new IllegalArgumentException("size:"+size+" < 0");

		this.beginIndex = beginIndex;
		this.size = size;
	}

	public PageCriteria(String id)
	{
		this.id = id;
	}
	/**
	 * @return the beginIndex
	 */
	public int getBeginIndex()
	{
		return beginIndex;
	}// --------------------------------------------------------
	/**
	 * @param beginIndex the beginIndex to set
	 */
	public void setBeginIndex(int beginIndex)
	{
		if(beginIndex < 1)
			throw new IllegalArgumentException("Begin index must be greater than 1");
		
		this.beginIndex = beginIndex;
	}// --------------------------------------------------------
	/**
	 * Set this.beginIndex = 1
	 */
	public void first()
	{
		this.beginIndex = 1;
	}// --------------------------------------------------------
	/**
	 * 
	 * @return this.beginIndex + size;
	 */
	public int getEndIndex()
	{
		return this.beginIndex + size;
	}
	/**
	 * @return the size
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size)
	{
		this.size = size;
	}
	/**
	 * Increment the page criteria

	 */
	public void incrementPage()
	{
		incrementPage(this);
	}// --------------------------------------------------------

	/**
	 * Increment the page criteria
	 * @param pageCriteria the page to increment
	 */
	public static void incrementPage(PageCriteria pageCriteria)
	{
		pageCriteria.beginIndex = pageCriteria.beginIndex + pageCriteria.size;
	}// --------------------------------------------------------


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PageCriteria [id=" + id + ", beginIndex=" + beginIndex
				+ ", size=" + size + ", savePagination=" + savePagination
				+ ", className=" + className + "]";
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + beginIndex;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (savePagination ? 1231 : 1237);
		result = prime * result + size;
		return result;
	}


	/**
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
		PageCriteria other = (PageCriteria) obj;
		if (beginIndex != other.beginIndex)
			return false;
		if (className == null)
		{
			if (other.className != null)
				return false;
		}
		else if (!className.equals(other.className))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (savePagination != other.savePagination)
			return false;
		if (size != other.size)
			return false;
		return true;
	}


	/**
	 *
	 * @return true is id is populated
	 */
	public boolean hasIdentifier()
	{
		return this.id != null && this.id.length() > 0;
	}// --------------------------------------------------------


	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	/**
	 * @return the savePagination
	 */
	public boolean isSavePagination()
	{
		return savePagination;
	}


	/**
	 * @param savePagination the savePagination to set
	 */
	public void setSavePagination(boolean savePagination)
	{
		this.savePagination = savePagination;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}


	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}



}
