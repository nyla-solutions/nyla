package nyla.solutions.dao.executable;

import java.sql.SQLException;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;

/**
 *  * 
 *  
 * @author Gregory Green
 * @version 1.0
 *
 * <pre>
 * <b>SqlExecutable</b>  represents an executed SQL statement
 * 
 */
public class  SqlExecutable extends AbstractDaoOperation
implements Executable
{

	/**
	 * Execute a single SQL statement
	 */
	public Integer execute(Environment env)
	{		
		 if(this.getSql() == null || this.getSql().length() == 0)
	         throw new ConfigException("Property \"sqlQuery\"  not setin "+this.getClass().getName());
	      
	      SQL sql = null;
	      try
	      {
	         sql = this.connect();
	         
	         sql.execute(this.getSql());
	         sql.commit();
	         
	         return 1;
	      }
	      catch(SQLException e)
	      {
	         sql.rollback();
	         throw new SystemException(e+" sql="+sql);
	      }
	      finally
	      {
	         if(sql != null)
	            sql.dispose();
	      }      
	}//---------------------------------------------
	
	/**
	 * Wrapper execution for a command
	 * @source
	 * @see solutions.global.patterns.command.Command#execute(java.lang.Object)
	 */
	///public Object execute(Environment source)
	//{
	//	execute(null,null);
		
	//	return null;
	//}
}
