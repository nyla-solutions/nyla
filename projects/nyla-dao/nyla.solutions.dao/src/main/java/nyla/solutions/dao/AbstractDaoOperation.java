package nyla.solutions.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import nyla.solutions.dao.jdbc.JdbcConstants;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Cryption;
import nyla.solutions.core.util.Debugger;

/**
 * <pre>
 * Abstract class for data access operations
 * 
 * <i>Configuration properties</i>
 * 
	#JDBC Connection
	jdbc.user=asap
	jdbc.password=ABC
	jdbc.driver=oracle.jdbc.OracleDriver
	jdbc.connection.url=jdbc:oracle:thin:host:1521:xe
 * </pre>
 * @author Gregory Green
 *
 */
public abstract class AbstractDaoOperation implements DaoOperation
{	
	/**
	 * @return the jdbcDriver
	 */
	public String getJdbcDriver()
	{
		return jdbcDriver;
	}
	/**
	 * @param jdbcDriver the jdbcDriver to set
	 */
	public void setJdbcDriver(String jdbcDriver)
	{
		this.jdbcDriver = jdbcDriver;
	}
	/**
	 * @return the connectionURL
	 */
	public String getConnectionURL()
	{
		return connectionURL;
	}
	/**
	 * @param connectionURL the connectionURL to set
	 */
	public void setConnectionURL(String connectionURL)
	{
		this.connectionURL = connectionURL;
	}
	/**
	 * @return the dbUserName
	 */
	public String getDbUserName()
	{
		return dbUserName;
	}
	/**
	 * @param dbUserName the dbUserName to set
	 */
	public void setDbUserName(String dbUserName)
	{
		this.dbUserName = dbUserName;
	}
	/**
	 * @return the dbPassword
	 */
	public char[] getDbPassword()
	{
		return dbPassword;
	}
	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(char[] dbPassword)
	{
	   //check if encrypted
	   if(!Cryption.isEncrypted(dbPassword))
	   {
		Debugger.printWarn(this,"DB PASSWORD IS NOT ENCRYPTED!!!");
	   }
	    
	   this.dbPassword = Cryption.interpret(dbPassword);
	}
	/**
	 * @return the sql
	 */
	public String getSql()
	{
		return sql;
	}
	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql)
	{
		this.sql = sql;
	}// ----------------------------------------------
	
	/**
	 * @return SQL connection
	 */
	protected SQL connect()
	{
	   
	   try
		{
			if(this.dataSource != null)
				   return SQL.connect(dataSource.getConnection());
				else
					 return SQL.connect(this.getJdbcDriver(),this.getConnectionURL(),this.getDbUserName(), this.getDbPassword());
		}
		catch (SQLException e)
		{
			throw new ConnectionException(e);
		}

	}// ----------------------------------------------
	
	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	private DataSource dataSource = null;
	private String jdbcDriver = Config.getProperty(getClass(),"jdbcDriver",Config.getProperty(JdbcConstants.JDBC_DRIVER_PROP,""));
	private String connectionURL = Config.getProperty(getClass(),"connectionURL",Config.getProperty(JdbcConstants.JDBC_CONNECTION_URL_PROP,""));
	private String dbUserName =  Config.getProperty(getClass(),"dbUserName",Config.getProperty(JdbcConstants.JDBC_USER_PROP,""));
	private char[] dbPassword = Config.getPropertyPassword(getClass(),"dbPassword",Config.getPropertyPassword(JdbcConstants.JDBC_PASSWORD_PROP,""));
	private String sql =  Config.getProperty(getClass(),"sql",Config.getProperty(getClass().getName()+".sql",""));

}
