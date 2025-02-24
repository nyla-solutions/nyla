package nyla.solutions.dao.jdbc.pooling;

import javax.sql.DataSource;

import nyla.solutions.core.exception.ConnectionException;

public interface JdbcConnectionFactory
{
	/**
	 * 
	 * @return the data source
	 * @throws ConnectionException
	 */
	DataSource getDataSource() 
	throws ConnectionException;
	
	public void setUserName(String userName);
	
	
	void setPassword(char[] password);
	
	void setConnectionUrl(String jdbcUrl);
	
	void setMaximumConnections(int maxConnections);
	
	void setMininumConnections(int minConnections);
	
	void setDriver(String jdbcDriver);
}
