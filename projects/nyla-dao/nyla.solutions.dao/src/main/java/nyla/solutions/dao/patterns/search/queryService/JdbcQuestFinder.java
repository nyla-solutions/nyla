package nyla.solutions.dao.patterns.search.queryService;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import javax.sql.DataSource;

import nyla.solutions.dao.DAOFactory;
import nyla.solutions.dao.PreparedStatementConstructor;
import nyla.solutions.dao.ResultSetObjectCreator;
import nyla.solutions.dao.SQL;
import nyla.solutions.dao.jdbc.mapping.DataRowMapper;
import nyla.solutions.dao.jdbc.mapping.ObjectArrayPreparedStatementConstructor;
import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.iteration.PagingCollection;
import nyla.solutions.core.patterns.search.queryService.QuestCriteria;
import nyla.solutions.core.patterns.search.queryService.QuestFinder;
import nyla.solutions.core.patterns.search.queryService.QuestKey;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

/**
 * The Quest finder query service implementation based on the JDBC
 * @author Gregory Green
 *
 */
public class JdbcQuestFinder implements QuestFinder
{
	
	/**
	 * Callable call to get results
	 * @see java.util.concurrent.Callable#call()
	 */
	public Collection<DataRow> call() throws Exception
	{
		return getResults();
	}// --------------------------------------------------------
	/**
	 * 
	 * @see solutions.global.patterns.search.queryService.QuestFinder#setCriteria(solutions.global.patterns.search.queryService.QuestCriteria)
	 */
	public void assignCriteria(QuestCriteria questCriteria,String source)
	{
		this.questCriteria = questCriteria;
		this.source = source;
		
		try
		{
			if(this.questCriteria != null && sqlDAO == null && source != null)
			{
				DataSource ds = DAOFactory.getDataSource(source);
				
				this.sqlDAO = SQL.connect(ds.getConnection());
			}
		}
		catch (SQLException e)
		{
			throw new ConnectionException("Error with DS:"+source+" ERROR:"+e.getMessage(),e);
		}
			
	}// --------------------------------------------------------
	
	
	/**
	 * 
	 * @see solutions.global.patterns.search.queryService.QuestFinder#getResults()
	 */
	public Paging<DataRow> getResults()
	{
		
		if(this.questCriteria == null)
			return null;
		
		//Get query
		String queryName = this.questCriteria.getQuestName();
		
		if(queryName == null)
					throw new RequiredException("questCriteria.questName");
		
		//Format configuration property name
		String propertyName = new StringBuilder(JdbcQuestFinder.class.getName()).append(".").append(this.source).append(".")
				.append(queryName).append(".").append("sql").toString();
		
		//Get SQL query from configuration property
		String sql = Config.getProperty(propertyName,"");

		//check if SQL exist
		if(sql == null || sql.length() == 0)
			throw new ConfigException(propertyName+"  property not found");
	
		
		
		//get connection
		//SQL sqlDAO = null;
		try
		{			
			
			QuestKey[] qks = this.questCriteria.getQuestKeys();
			
			//Create inputs
			Object[] inputs = null;
			if(qks != null && qks.length > 0)
			{
				inputs = new Object[qks.length];
				
				for (int i = 0; i < qks.length; i++)
				{
					inputs[i] = Text.toObject(qks[i].getValue(), qks[i].getType());
					
				}	
			}
			
			Debugger.println(this,"INPUTS:"+Debugger.toString(inputs));
			
			//Handle sorting
			String sort = this.questCriteria.getSort();
			Collection<DataRow> collection;		
			
			
			if(sort == null || sort.length() == 0)
			{
				collection = new ArrayList<DataRow>();
			}
			else
			{
				Comparator<DataRow> comparator = ClassPath.newInstance(sort);
				
				collection = new TreeSet<DataRow>(comparator);
			}
			
			//Create paging base on page criteria and sorting collection
			PageCriteria pageCriteria = this.questCriteria.getPageCriteria();
			
			Paging<DataRow> paging = new PagingCollection<DataRow>(collection, pageCriteria);
			
			sqlDAO.selectPaging(sql, inputs, objectArraySetter, dataRowMapper, paging);
			
			if(paging.isEmpty())
				return null;
			
			return paging;
		}
		catch (SQLException e)
		{
			throw new SystemException("SQL:"+sql+" ERROR:"+e.getMessage(),e);
		}
		catch (ParseException e)
		{
			throw new SystemException("Input argument conversion issue ERROR:"+e.getMessage()
					+" inputs criteria:"+this.questCriteria,e);
		}

	}// --------------------------------------------------------

	/**
	 * 
	 * @param questName the quest name
	 * @param propertyName the from the configuration property
	 * @return configuration property
	 */
	/*private String determineProperty(String questName, String propertyName)
	{
		return Config.getProperty(formatPropertyName( questName,  propertyName),"");
	}*/
	// --------------------------------------------------------
	/**
	 * 
	 * @param questName the quest name
	 * @param propertyName the property name
	 * @return new StringBuilder(questName).append(".").append(propertyName)
	 */
	/*private String formatPropertyName(String questName, String propertyName)
	{
		return new StringBuilder(JdbcQuestFinder.class.getName()).append(".")
				.append(questName).append(".").append(propertyName).toString();
	}*/// --------------------------------------------------------
	/**
	 * @see solutions.global.patterns.Disposable#dispose()
	 */
	public void dispose()
	{
		if(this.sqlDAO != null)
			try{ sqlDAO.dispose();} catch(Exception e){}
		
		sqlDAO = null;
	}
	private String source = null;
	private SQL sqlDAO = null;
	private PreparedStatementConstructor<Object[]> objectArraySetter = new ObjectArrayPreparedStatementConstructor();
	private ResultSetObjectCreator<DataRow> dataRowMapper =  new DataRowMapper();
	private QuestCriteria questCriteria;
}
