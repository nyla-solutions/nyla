package nyla.solutions.dao;

import java.sql.SQLException;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SystemException;

/**
 * Query represents a database query with results.
 * 
 * @author Gregory Green
 *
 */
public class Query extends AbstractDaoOperation implements DaoOperation
{
	  /**
	    * 
	    * Execute the sqlQuery and return XML results in the format DataResultSet
	    */
	   public DataResultSet getResults()
	   throws NoDataFoundException
	   {
	      if(this.getSql() == null || this.getSql().length() == 0)
	         throw new ConfigException("Property \"sqlQuery\"  not setin "+this.getClass().getName());
	      
	      SQL sql = null;
	      try
	      {
	         sql = connect();    
	         
	         return sql.selectDataResultSet(this.getSql());
	      }
	      catch(SQLException e)
	      {
	         throw new SystemException(e);
	      }
	      finally
	      {
	         if(sql != null)
	            sql.dispose();
	      }      
	   }//-------------------------------------------- 

	
}
