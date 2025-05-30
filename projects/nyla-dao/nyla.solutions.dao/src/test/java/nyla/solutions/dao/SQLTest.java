package nyla.solutions.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Ignore;

import nyla.solutions.dao.PreparedStatementConstructor;
import nyla.solutions.dao.ResultSetObjectCreator;
import nyla.solutions.dao.SQL;
import nyla.solutions.core.exception.DataException;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.patterns.iteration.PagingCollection;
import junit.framework.TestCase;

@Ignore
public class SQLTest extends TestCase
{
	/**
	 * 
	 * @throws Exception
	 */
	public void testPaging() throws Exception
	{
		SQL sqlDAO = null;
		
		String sql = "select * from inventory";
		try
		{
			sqlDAO = SQL.getInstance();
			
			PageCriteria pageCriteria = new PageCriteria();
			pageCriteria.setSize(2);
			pageCriteria.setSavePagination(true);
			
			PagingCollection<Object> paging = new PagingCollection<Object>(new ArrayList<Object>(), pageCriteria);
			Pagination pagination = sqlDAO.selectPaging(sql, null, new PreparedStatementConstructorUT(), new ResultSetObjecCreatorUT(), paging);
			
			assertNotNull(pagination);
			
			assertTrue(!paging.isEmpty());
			
					
		}
		finally
		{
			
			if(sqlDAO != null)
				try{ sqlDAO.dispose(); } catch(Exception e){}
		}
		
	}// --------------------------------------------------------
	class ResultSetObjecCreatorUT implements ResultSetObjectCreator<Object>
	{

		public Object create(ResultSet rs, int index)
		{
			try
			{
			
				return rs.getString(1);
			}
			catch(SQLException e)
			{
				throw new DataException(e.getMessage(),e);
			}
		}
		
	}// --------------------------------------------------------
	class PreparedStatementConstructorUT implements PreparedStatementConstructor<Object>
	{

		public void constructPreparedStatement(Connection connection,
				PreparedStatement preparedStatement, Object obj)
				throws SQLException
		{
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
