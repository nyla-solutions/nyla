package nyla.solutions.core.patterns.iteration;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.data.Identifier;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.core.patterns.creational.RowObjectCreator;
import nyla.solutions.core.util.Config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import static nyla.solutions.core.util.Config.settings;


/**
 * The pagination data
 * 
 * @author Gregory Green
 *
 */
public abstract class Pagination implements Identifier
{
	private static final String paginationClassName = settings().getProperty(Pagination.class,"paginationClassName","nyla.solutions.dao.mongodb.MongoPagination");

	private final String id;
	private Map<String, Pagination> paginationMap = new HashMap<String,Pagination>();

	/**
	 * Set id and save pagination
	 * @param id
	 */
	protected Pagination(String id)
	{
		this.id = id;
		this.save();
	}

	/**
	 * Store the given pagination data
	 * @param <ObjectType> the object to store
	 * @param storeObject the object to store
	 * @param pageCriteria the page criteria
	 */
	public abstract <ObjectType> void store(ObjectType storeObject, PageCriteria pageCriteria);



	public abstract <IterationObjectType,CreatedObjectType>
	void constructPaging(Iterator<IterationObjectType> resultSet, PageCriteria pageCriteria,
			RowObjectCreator<CreatedObjectType,IterationObjectType> creator);
	/**
	 * 
	 * Wait for this.getFuture().isDone();
	 */
	public void waitForCompletion()
	{
		try
		{
			Set<Future<?>> futures = this.getFutures();
			
			if(futures  == null)
				return;
			
			for (Future<?> future : futures)
			{
				future.get();
			}
		}
		catch (Exception e)
		{
			throw new SystemException("Error while wating for results "+e.getMessage(),e);
		}
	}// --------------------------------------------------------



	/**
	 *
	 * @param pageCriteria the page criteria
	 * @param pageClass the page Class
	 * @param <ReturnTypes> the return types
	 * @return the paging object
	 */
	public abstract<ReturnTypes> Paging<ReturnTypes> getPaging(PageCriteria pageCriteria, Class<?> pageClass);
	/**
	 * 
	 * @param pageCriteria page criteria
	 * @param <ReturnTypes> the return types
	 * @return the Paging
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <ReturnTypes> Paging<ReturnTypes> getPaging(PageCriteria pageCriteria)
	{
		try
		{	
			String className = pageCriteria.getClassName();
			
			Class classT = Class.forName(className);
			
			return (Paging<ReturnTypes>)getPaging(pageCriteria, classT);
		}
		catch(ClassNotFoundException e)
		{
			throw new SetupException(e.getMessage(),e);
		}
	}// --------------------------------------------------------

	/**
	 *
	 * @param pageCriteria
	 * @return the page counts
	 * @throws NoDataFoundException
	 */
	public abstract long count(PageCriteria pageCriteria);

	/**
	 * Clear previous page data
	 */
	public abstract void clear();


	/**
	 * Total size of pagination
	 * @return total size
	 */
	public abstract long size();


	/**
	 * Cancel the processing
	 */
	public abstract void cancel();

	/**
	 *
	 * @return background paging thread state
	 */
	public abstract Set<Future<?>> getFutures();

	/**
	 *
	 */
	private void save()
	{

		paginationMap.put(this.getId(), this);
	}// --------------------------------------------------------

	protected   Pagination removePaginationById(String id)
	{
		return paginationMap.remove(id);
	}// --------------------------------------------------------
	/**
	 *  @param id the page identifier
	 * @return pagination
	 */
	public Pagination getPaginationById(String id)
	{

		if(id == null)
			return null;

		return paginationMap.get(id);

	}// --------------------------------------------------------

	/**
	 * @return the id
	 */
	public final String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(String id)
	{
		//Cannot change id
	}
}