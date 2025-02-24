package nyla.solutions.dao.executable;

import java.sql.SQLException;
import java.sql.Statement;

import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;

/**
 * @author Gregory Green
 * @version 1.0
 * @deprecated use commands
 *
 *<pre>
 * <b>SqlExecutable</b>  represents an executed SQL statement
 * 
 * <i>Sample Spring Configuration</i>
 * 
 *     &lt;bean id="cleanUpMonitoringExecutable" class="nyla.solutions.dao.executable.BatchSqlExecutable"
		singleton="false"&gt;
		
		&lt;property name="sqls"&gt;
		     &lt;list&gt;
		         &lt;value&gt;&lt;![CDATA[DELETE FROM JVM_MEMORY_USAGES WHERE CAPTURE_DATE &lt; trunc(add_months(sysdate,-12),'DAY')]]&gt;&lt;/value&gt;
		         &lt;value&gt;&lt;![CDATA[DELETE FROM JVM_THREAD_COUNTS WHERE CAPTURE_DATE &lt; trunc(add_months(sysdate,-12),'DAY')]]&gt;&lt;/value&gt;
		         &lt;value&gt&lt;![CDATA[DELETE FROM MONITORING_SIZES WHERE CAPTURE_DATE &lt; trunc(add_months(sysdate,-12),'DAY')]]&gt;&lt;/value&gt;
		      &lt;/list&gt;
		&lt;/property&gt;
	&lt;/bean&gt;
 * </pre>
 */
@Deprecated
public class  BatchSqlExecutable extends AbstractDaoOperation
//implements Executable, Command<Object, Environment>
{
	/**
	 * Execute an Batch Sqls statement
	 */
	public void execute(Environment env, String[] args)
	{		
	   String sqlQuery = this.getSql();
	   
	   if(sqlQuery ==null || sqlQuery.length() == 0 && this.sqls == null)
	   {
		throw new RequiredException("sql or sqls" );
	   }
		
	      
	   SQL sql = null;
	    Statement stmt = null;
	    try
	    {
		 //connect
	       sql = this.connect();  
	         
	       //create statement  
	       stmt = sql.createStatement();
	         
	         
	         if(sqlQuery != null && sqlQuery.length() > 0)
	         {
	      	Debugger.println(this,"executing="+sqlQuery);
	      	stmt.addBatch(sqlQuery);
	         }
	      	
	         
	         for(int i =0; i < sqls.length; i++)
	         {
	      	Debugger.println(this,"executing sql["+i+"]="+sqls[i]);
	      	
	      	//add batch for SQL
	      	stmt.addBatch(sqls[i]);
	         }
	         
	         //execute batch
	         stmt.executeBatch();
	         
	         sql.commit();
	      }
	      catch(SQLException e)
	      {
	         sql.rollback();
	         
	         throw new SystemException(e+" sqls="+Debugger.toString(sqls)+ " i="+0);
	      }
	      finally
	      {
	         if(stmt != null)
	      	try{ stmt.close();} catch(Exception e){}
	      	
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
	
	/**
	 * @return the SQL(s)
	 */
	public String[] getSqls()
	{
	   return sqls;
	}

	/**
	 * @param sqls the SQL(s) to set
	 */
	public void setSqls(String[] sqls)
	{
	   this.sqls = sqls;
	}

	private String[] sqls = null;
}
