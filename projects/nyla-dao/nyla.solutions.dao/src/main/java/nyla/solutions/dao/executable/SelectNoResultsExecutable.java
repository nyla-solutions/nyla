package nyla.solutions.dao.executable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;

/**
 *  * @deprecated use commands
 * <pre>
 * This object simply executes a select statement in the database.
 * The results of the SQL are not captures.
 * This function is primarily used used to database load testings.
 * 
 * Set the Config properties
 * 
 * jdbc.connection.url
 * jdbc.driver
 * jdbc.user
 * jdbc.password
 * 
 * 
 * Set the property setSql(...);
 * </pre>
 * @author Gregory Green
 *
 */
@Deprecated
public class SelectNoResultsExecutable extends AbstractDaoOperation implements Executable
{
	/**
	 * Execute an insert statement
	 */
	public Integer execute(Environment env)
	{		
		 if(this.getSql() == null || this.getSql().length() == 0)
	         throw new ConfigException("Property \"sqlQuery\"  not setin "+this.getClass().getName());
	      
	      SQL sql = null;
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      try
	      {
	         sql = this.connect();    
	         
	         
	         ps = sql.prepareStatement(this.getSql());
	         
	         //long start = System.currentTimeMillis();
	         rs = sql.select(ps);
	         //long duration = System.currentTimeMillis() - start;
	         
	         
	         /*int count = 0;
	         
	         while(rs.next())
	         {
	      	count++;
	         }*/
	         
	         return 1;
	         
	      }
	      catch(Exception e)
	      {
	         throw new SystemException(e);
	      }
	      finally
	      {
	         if(rs != null)
	      	try{ rs.close();} catch(Exception e){}
	      	
	        if(ps != null)
		      	try{ ps.close();} catch(Exception e){}
	         if(sql != null)
	            sql.dispose();
	      }      
	}//---------------------------------------------	
	/**
	 * Wrapper execution for a command
	 * @source
	 * @see solutions.global.patterns.command.Command#execute(java.lang.Object)
	 */
	//public Object execute(Environment source)
	//{
	//	execute(null,null);
		
	//	return null;
	//}

}
