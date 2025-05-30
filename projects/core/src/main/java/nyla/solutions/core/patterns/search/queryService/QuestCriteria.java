package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.patterns.iteration.PageCriteria;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Search criteria for a quest
 * @author Gregory Green
 *
 */
public class QuestCriteria implements Serializable
{

	/**
	 * @return the pageCriteria
	 */
	public PageCriteria getPageCriteria()
	{
		return pageCriteria;
	}
	/**
	 * @param pageCriteria the pageCriteria to set
	 */
	public void setPageCriteria(PageCriteria pageCriteria)
	{
		this.pageCriteria = pageCriteria;
	}
	/**
	 * @return the dataSources
	 */
	public String[] getDataSources()
	{
		if(dataSources == null)
			return null;
		
		return dataSources.clone();
	}
	/**
	 * @param dataSources the dataSources to set
	 */
	public void setDataSources(String... dataSources)
	{
		if(dataSources == null)
			this.dataSources = null;
		else
			this.dataSources = dataSources.clone();
	}
	
	/**
	 * @return the questName
	 */
	public String getQuestName()
	{
		return questName;
	}
	/**
	 * @param questName the questName to set
	 */
	public void setQuestName(String questName)
	{
		this.questName = questName;
	}
	
	
	
	/**
	 * @return the sort
	 */
	public String getSort()
	{
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort)
	{
		this.sort = sort;
	}
	/**
	 * @return the filter
	 */
	public String getFilter()
	{
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter)
	{
		this.filter = filter;
	}

	
	/**
	 * @return the questKeys
	 */
	public QuestKey[] getQuestKeys()
	{
		if(questKeys == null)
			return null;
		
		return questKeys.clone();
	}
	/**
	 * @param questKeys the questKeys to set
	 */
	public void setQuestKeys(QuestKey[] questKeys)
	{
		if(questKeys == null)
			this.questKeys = null;
		else
			this.questKeys = questKeys.clone();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "QuestCriteria [questKeys=" + Arrays.toString(questKeys)
				+ ", pageCriteria=" + pageCriteria + ", dataSources="
				+ Arrays.toString(dataSources) + ", questName=" + questName
				+ ", sort=" + sort + ", filter=" + filter 
				+ "]";
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(dataSources);
		result = prime * result + ((filter == null) ? 0 : filter.hashCode());
		result = prime * result
				+ ((pageCriteria == null) ? 0 : pageCriteria.hashCode());
		result = prime * result + Arrays.hashCode(questKeys);
		result = prime * result
				+ ((questName == null) ? 0 : questName.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
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
		QuestCriteria other = (QuestCriteria) obj;
		if (!Arrays.equals(dataSources, other.dataSources))
			return false;
		if (filter == null)
		{
			if (other.filter != null)
				return false;
		}
		else if (!filter.equals(other.filter))
			return false;
		if (pageCriteria == null)
		{
			if (other.pageCriteria != null)
				return false;
		}
		else if (!pageCriteria.equals(other.pageCriteria))
			return false;
		
		if (!Arrays.equals(questKeys, other.questKeys))
			return false;
		if (questName == null)
		{
			if (other.questName != null)
				return false;
		}
		else if (!questName.equals(other.questName))
			return false;
		if (sort == null)
		{
			if (other.sort != null)
				return false;
		}
		else if (!sort.equals(other.sort))
			return false;
		return true;
	}

	private QuestKey[] questKeys;
	private PageCriteria pageCriteria;
	private String[] dataSources;
	private String questName;
	private String sort;
	private String filter;
	
	//private input
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2358069477226859151L;
}
