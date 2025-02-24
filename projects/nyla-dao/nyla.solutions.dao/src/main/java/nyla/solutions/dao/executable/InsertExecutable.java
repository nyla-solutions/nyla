package nyla.solutions.dao.executable;

import java.sql.SQLException;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;

/**
 * @author Gregory Green
 * @version 1.0
 *  * @deprecated use commands
 *
 * <b>InsertExecutable</b>  represents an executed insert statement
 * 
 * 
 */
@Deprecated
public class  InsertExecutable extends AbstractDaoOperation
//implements Executable, Command<Object, Environment>
{
	/**
	 * Execute an insert statement
	 */
	public void execute(Environment env, String[] args)
	{		
		 if(this.getSql() == null || this.getSql().length() == 0)
	         throw new ConfigException("Property \"sqlQuery\"  not setin "+this.getClass().getName());
	      
	      SQL sql = null;
	      try
	      {
	         sql = this.connect();   
	         
	         sql.insert(getSql());
	         
	         sql.commit();
	      }
	      catch(SQLException e)
	      {
	         sql.rollback();
	         
	         throw new SystemException(e);
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
	public Object execute(Environment source)
	{
		execute(null,null);
		
		return null;
	}
}
