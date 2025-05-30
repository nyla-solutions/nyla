package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.core.patterns.iteration.Paging;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Finder interface for retrieving result set
 * @author Gregory Green
 *
 */
public interface QuestFinder extends Callable<Collection<DataRow>>, Disposable
{
	
	/**
	 * @param source the source name
	 * @param questCriteria the criteria to use
	 */
	void assignCriteria(QuestCriteria questCriteria, String source);
	
	/**
	 * 
	 * @return the execution results
	 */
	Paging<DataRow> getResults();
	
}
