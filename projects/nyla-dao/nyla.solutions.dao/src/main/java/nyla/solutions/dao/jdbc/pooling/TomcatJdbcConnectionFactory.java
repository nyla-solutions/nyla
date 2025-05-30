package nyla.solutions.dao.jdbc.pooling;
import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.util.Config;

/**
 * The Tomcat JDBC connection pooling connection factory 
 * @author Gregory Green
 *
 */
public class TomcatJdbcConnectionFactory implements JdbcConnectionFactory
{
	/**
	 * 
	 * @see nyla.solutions.dao.jdbc.pooling.JdbcConnectionFactory#getDataSource()
	 */
	public DataSource getDataSource()
	throws ConnectionException
	{
			PoolProperties p = new PoolProperties();
            p.setUrl(this.connectionUrl);
            p.setDriverClassName(this.driver);
            
            if(this.userName != null && this.userName.length() > 0)
            	p.setUsername(this.userName);
            
            if(this.password != null && this.password.length > 0)
            	p.setPassword(String.valueOf(password));
            
            //p.setJmxEnabled(this.jmxEnabled);
            //p.setTestWhileIdle(false);
            //p.setTestOnBorrow(true);
            ////p.setValidationQuery("SELECT 1");
            //p.setTestOnReturn(false);
            //p.setValidationInterval(30000);
            //p.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
            
            /*p.setMinIdle(this.mininumConnections);
            p.setMaxIdle(this.maximumConnections);
            p.setMaxActive(this.maximumConnections);
            p.setInitialSize(this.mininumConnections);
            p.setMaxWait(this.maxWait);
            */
            
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(100);
            p.setInitialSize(10);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(10);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            
            //p.setRemoveAbandonedTimeout(this.removeAbandonedTimeout);
            //p.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
            //p.setMinIdle(this.minIdle);
            //p.setLogAbandoned(this.logAbandoned);
            //p.setRemoveAbandoned(this.removeAbandoned);
            //p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
            //  "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
            datasource.setPoolProperties(p); 
            
            return datasource;

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
	public void setMaximumConnections(int maxConnections)
	{
		this.maximumConnections =  maxConnections;
		
	}

	public void setMininumConnections(int minConnections)
	{
		this.mininumConnections = minConnections;
	}
	

	/**
	 * @return the maxWait
	 */
	public int getMaxWait()
	{
		return maxWait;
	}


	/**
	 * @param maxWait the maxWait to set
	 */
	public void setMaxWait(int maxWait)
	{
		this.maxWait = maxWait;
	}





	/**
	 * @return the connectionUrl
	 */
	public String getConnectionUrl()
	{
		return connectionUrl;
	}





	/**
	 * @return the maximumConnections
	 */
	public int getMaximumConnections()
	{
		return maximumConnections;
	}





	/**
	 * @return the mininumConnections
	 */
	public int getMininumConnections()
	{
		return mininumConnections;
	}


	private String userName;
	private char[] password;
	private String 	connectionUrl;
	private String 	driver;
	
	private int maximumConnections = Config.getPropertyInteger(TomcatJdbcConnectionFactory.class,"maximumConnections",10).intValue();
	private int  mininumConnections = Config.getPropertyInteger(TomcatJdbcConnectionFactory.class,"mininumConnections",1).intValue();
	private int maxWait = Config.getPropertyInteger(TomcatJdbcConnectionFactory.class,"maxWait",10000).intValue();

	
}
