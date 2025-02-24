package nyla.solutions.dao.patterns.command;

import java.io.IOException;
import java.sql.SQLException;

import nyla.solutions.commas.Command;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;


/**
 *  
 * @author Gregory Green
 * @version 1.0
 *
 * <pre>
 * <b>SqlFileExecuteCommand</b> execute SQL statement in a file
 * indicate by an input argument or the getSql property.
 * 
 * 

 * 
 */
public class  SqlFileExecuteCommand extends AbstractDaoOperation
implements Command<Boolean, String>
{
	/**
	 * Execute a single SQL statement
	 */
	public Boolean execute(String filePath)
	{		
		
		if(filePath == null || filePath.length() == 0)
			filePath = this.getSql();
		
		
		 if(filePath== null || filePath.length() == 0)
	         throw new ConfigException("File path not provide and a default is not on Property \"filePath\" of "+this.getClass().getName());
	      
	      SQL sql = null;
	      
	      //read SQL from file
	      String sqlText = null;
	     
	      
	      try
	      {
	    	  sqlText = IO.readFile(filePath);
	    	  
	         sql = this.connect();
	         
	         boolean results = sql.execute(sqlText);
	         sql.commit();
	         
	         return new Boolean(results);
	      }
	      catch(SQLException e)
	      {
	         sql.rollback();
	         throw new SystemException("Sql="+sqlText+" ERROR:"+e.getMessage(),e);
	      }
	      catch(IOException e)
	      {
	    	  throw new SetupException("File path "+filePath+" ERROR:"+e.getMessage(),e);
	      }
	      finally
	      {
	         if(sql != null)
	            sql.dispose();
	      }      
	}//---------------------------------------------
	

}
