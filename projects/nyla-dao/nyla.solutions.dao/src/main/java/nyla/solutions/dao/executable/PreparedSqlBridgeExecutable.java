/**
 * 
 */
package nyla.solutions.dao.executable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;

/**
 *  * @deprecated use commands
 * <pre>
 * This object selects results from a bridge query and feeds each result into the SQL prepared Statement.
 *  This object is typical used for insert data from one database to another.
 *  
 *  
 *  Example
 *  
 *  sql
 *  [
 *    insert into monitoring_connections(username) values(?)
 *  ]
 *  
 *  bridgeQuerySql
 *  [
 *     select username from v$session
 *  ]
 *  
 *  
 *  </pre>
 * @author Gregory Green
 *
 */
@Deprecated
public class PreparedSqlBridgeExecutable 
extends  AbstractDaoOperation implements Executable
{
   
   /**
	 * Execute an insert statement
	 */
	public Integer execute(Environment env)
	{		
		 if(this.getSql() == null || this.getSql().length() == 0)
	         throw new ConfigException("Property \"sqlQuery\"  not setin "+this.getClass().getName());
	      
		 if(bridgeQuerySql == null || bridgeQuerySql.length() == 0)
			   throw new RequiredException("this.bridgeQuerySql");
		 
	      SQL targetSQL = null;
	      SQL bridgeSQL = null;
	      
	      PreparedStatement targetPS  = null;
	      Statement bridgeStatement = null;
	      ResultSet bridgeRS = null;
	      try
	      {
	         targetSQL = this.connect();
	         
	         
	         if(bridgeDataSource != null)
	         {
	        	 bridgeSQL  = SQL.connect(this.bridgeDataSource.getConnection());
	         }
	         else
		         bridgeSQL = SQL.connect(this.bridgeQueryJdbcDriver,
		      	   				this.bridgeQueryConnectionURL, 
		      	   				this.bridgeQueryDbUserName, 
		      	   				this.bridgeQueryDbPassword);
		         
	         
	         //Create prepared SQL
	         targetPS = targetSQL.prepareStatement(this.getSql());
	         	         
	         ///create bridge SQL statement
	         bridgeStatement =  bridgeSQL.createStatement();
	         
	         //select results from bridge
	         Debugger.println("Executing SQL "+bridgeQuerySql);
	         
	         bridgeRS =bridgeStatement.executeQuery(this.bridgeQuerySql);
	         
	         //get number of columns
	         
	         ResultSetMetaData metaData =  bridgeRS.getMetaData();
	         int bridgeColumnCount = metaData.getColumnCount();
	         
	         if(bridgeColumnCount == 0)
	      	throw new SystemException("bridgeColumnCount is 0");
	         
	         //Loop thru results
	         while(bridgeRS.next())
	         {
	      	//Debugger.println(this," processing records");
	      	//initial prepare input in row
	      	for(int i=1; i <= bridgeColumnCount; i++)
	      	{
	      	   targetPS.setObject(i,bridgeRS.getObject(i), metaData.getColumnType(i));   
	      	}
	      	
	      	//execute statement
	      	targetPS.execute();
	      	
	      	//clear parameters
	      	targetPS.clearParameters();
	      	
	         }//----------------------------------------------
	         	         
	         
	         targetSQL.commit();
	         
	         return 1;
	      }
	      catch(SQLException e)
	      {
	         targetSQL.rollback();
	         
	         throw new SystemException(e);
	      }
	      finally
	      {
	      	
	         if(targetPS != null )
		      	try{ targetPS.close();} catch(Exception e){}
		      	
	         if(targetSQL != null)
	      	try{ targetSQL.dispose(); } catch(Exception e){}
	      	
		   if(bridgeRS != null )
		      	try{ bridgeRS.close();} catch(Exception e){}
		      	
	         if(bridgeStatement != null)
		      try{ bridgeStatement.close(); } catch(Exception e){}
		      
		   if(bridgeSQL != null)
			try{ bridgeSQL.dispose(); } catch(Exception e){}	      
		      
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
	
	/**
    * @return the bridgeQuerySql
    */
   public String getBridgeQuerySql()
   {
      return bridgeQuerySql;
   }


   /**
    * @param bridgeQuerySql the bridgeQuerySql to set
    */
   public void setBridgeQuerySql(String bridgeQuerySql)
   {
      this.bridgeQuerySql = bridgeQuerySql;
   }
   
   /**
    * @return the bridgeQueryJdbcDriver
    */
   public String getBridgeQueryJdbcDriver()
   {
      return bridgeQueryJdbcDriver;
   }

   /**
    * @param bridgeQueryJdbcDriver the bridgeQueryJdbcDriver to set
    */
   public void setBridgeQueryJdbcDriver(String bridgeQueryJdbcDriver)
   {
      this.bridgeQueryJdbcDriver = bridgeQueryJdbcDriver;
   }

   /**
    * @return the bridgeQueryConnectionURL
    */
   public String getBridgeQueryConnectionURL()
   {
      return bridgeQueryConnectionURL;
   }

   /**
    * @param bridgeQueryConnectionURL the bridgeQueryConnectionURL to set
    */
   public void setBridgeQueryConnectionURL(String bridgeQueryConnectionURL)
   {
      this.bridgeQueryConnectionURL = bridgeQueryConnectionURL;
   }

   /**
    * @return the bridgeQueryDbUserName
    */
   public String getBridgeQueryDbUserName()
   {
      return bridgeQueryDbUserName;
   }

   /**
    * @param bridgeQueryDbUserName the bridgeQueryDbUserName to set
    */
   public void setBridgeQueryDbUserName(String bridgeQueryDbUserName)
   {
      this.bridgeQueryDbUserName = bridgeQueryDbUserName;
   }

   /**
	 * @return the bridgeDataSource
	 */
	public DataSource getBridgeDataSource()
	{
		return bridgeDataSource;
	}
	/**
	 * @param bridgeDataSource the bridgeDataSource to set
	 */
	public void setBridgeDataSource(DataSource bridgeDataSource)
	{
		this.bridgeDataSource = bridgeDataSource;
	}
	
	private DataSource bridgeDataSource = null;
   private String bridgeQueryJdbcDriver = Config.getProperty(PreparedSqlBridgeExecutable.class,"bridgeQueryJdbcDriver");
   private String bridgeQueryConnectionURL = Config.getProperty(PreparedSqlBridgeExecutable.class,"bridgeQueryConnectionURL");
   private String bridgeQueryDbUserName = Config.getProperty(PreparedSqlBridgeExecutable.class,"bridgeQueryDbUserName");
   private char bridgeQueryDbPassword[] = Config.getProperty(PreparedSqlBridgeExecutable.class,"bridgeQueryDbPassword").toCharArray();

   private String bridgeQuerySql =Config.getProperty(PreparedSqlBridgeExecutable.class,"bridgeQuerySql","");

}
