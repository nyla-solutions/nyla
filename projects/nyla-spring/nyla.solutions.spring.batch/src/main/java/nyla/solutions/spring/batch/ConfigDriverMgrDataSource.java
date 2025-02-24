package nyla.solutions.spring.batch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import nyla.solutions.core.util.Config;

public class ConfigDriverMgrDataSource extends DriverManagerDataSource
{
	@Override
	public void setDriverClassName(String driverClassName)
	{
		super.setDriverClassName(Config.interpret(driverClassName));
	}// --------------------------------------------------------
	
	/**
	 * @see org.springframework.jdbc.datasource.AbstractDriverBasedDataSource#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url)
	{
		super.setUrl(Config.interpret(url));
	}

	/**
	 * @see org.springframework.jdbc.datasource.AbstractDriverBasedDataSource#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username)
	{
		// TODO Auto-generated method stub
		super.setUsername(Config.interpret(username));
	}

	/**
	 * 
	 * @see org.springframework.jdbc.datasource.AbstractDriverBasedDataSource#setPassword(java.lang.String)
	 */

	@Override
	public void setPassword(String password)
	{
		super.setPassword(Config.interpret(password));
	}
	
	/**
	 * 
	 * @see org.springframework.jdbc.datasource.DriverManagerDataSource#getConnectionFromDriverManager(java.lang.String, java.util.Properties)
	 */
	@Override
	protected Connection getConnectionFromDriverManager(String url,
			Properties props) throws SQLException
	{
		// TODO Auto-generated method stub
		Connection connection = super.getConnectionFromDriverManager(url, props);
		
		if(this.readOnly)
			connection.setReadOnly(readOnly);
		
		return connection;
	}// --------------------------------------------------------
	
	
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}


	private boolean readOnly = false;
	    
}
