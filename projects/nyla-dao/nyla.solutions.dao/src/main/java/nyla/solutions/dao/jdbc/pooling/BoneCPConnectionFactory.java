package nyla.solutions.dao.jdbc.pooling;
import javax.sql.DataSource;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.util.Config;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * The BONE connection pooling connection factory 
 * @author Gregory Green
 *
 */
public class BoneCPConnectionFactory implements JdbcConnectionFactory
{
	/**
	 * 
	 * @see nyla.solutions.dao.jdbc.pooling.JdbcConnectionFactory#getDataSource()
	 */
	public DataSource getDataSource()
	throws ConnectionException
	{
		try
		{
			BoneCPDataSource dataSource = null;
 
			 // setup the connection pool
			BoneCPConfig config = new BoneCPConfig();
			
			config.setJdbcUrl(this.connectionUrl);
			
			if(this.driver == null || this.driver.length() == 0)
						throw new IllegalArgumentException("this.driver");
			Class.forName(this.driver);
			
			if(this.userName != null)
				config.setUsername(this.userName); 
			
			if(this.password != null)
			{
				config.setPassword(String.valueOf(this.password));
			}
			
			config.setMinConnectionsPerPartition(this.minConnectionsPerPartition);
			config.setMaxConnectionsPerPartition(this.maxConnectionsPerPartition);
			config.setPartitionCount(this.partitionCount);
			
				
			dataSource = new BoneCPDataSource(config); // setup the connection pool
			
			return dataSource;
		}
		catch (ClassNotFoundException e)
		{
			throw new SetupException("Unable to load JDBC driver class:"+this.driver+" ERROR:"+e.getMessage(),e);
		}
		
	}// --------------------------------------------------------
	
	
	
	/**
	 * @return the userName
	 */
	public String getUserName()
	{
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public char[] getPassword()
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(char[] password)
	{
		this.password = password;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setConnectionUrl(String jdbcUrl)
	{
		this.connectionUrl = jdbcUrl;
	}
	/**
	 * @return the minConnectionsPerPartition
	 */
	public int getMinConnectionsPerPartition()
	{
		return minConnectionsPerPartition;
	}
	/**
	 * @param minConnectionsPerPartition the minConnectionsPerPartition to set
	 */
	public void setMininumConnections(int minConnectionsPerPartition)
	{
		this.minConnectionsPerPartition = minConnectionsPerPartition;
	}
	/**
	 * @return the maxConnectionsPerPartition
	 */
	public int getMaxConnectionsPerPartition()
	{
		return maxConnectionsPerPartition;
	}
	/**
	 * @param maxConnectionsPerPartition the maxConnectionsPerPartition to set
	 */
	public void setMaximumConnections(int maxConnectionsPerPartition)
	{
		this.maxConnectionsPerPartition = maxConnectionsPerPartition;
	}
	/**
	 * @return the partitionCount
	 */
	public int getPartitionCount()
	{
		return partitionCount;
	}
	/**
	 * @param partitionCount the partitionCount to set
	 */
	public void setPartitionCount(int partitionCount)
	{
		this.partitionCount = partitionCount;
	}// --------------------------------------------------------

	/**
	 * @return the driver
	 */
	public String getDriver()
	{
		return driver;
	}// --------------------------------------------------------
	
	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver)
	{
		this.driver = driver;
	}




	private String userName;
	private char[] password;
	private String 	connectionUrl;
	private String 	driver;
	private int minConnectionsPerPartition = Config.getPropertyInteger(BoneCPConnectionFactory.class,"minConnectionsPerPartition",1).intValue();
	private int maxConnectionsPerPartition = Config.getPropertyInteger(BoneCPConnectionFactory.class,"maxConnectionsPerPartition",10).intValue();
	private int partitionCount = Config.getPropertyInteger(BoneCPConnectionFactory.class,"partitionCount",1).intValue();
	
	
	
	
}
