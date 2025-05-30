package nyla.solutions.dao;

public interface DaoOperation
{

	/**
	 * @return the sql
	 */
	public abstract String getSql();
	
	/**
	 * @param sqlQuery the sqlQuery to set
	 */
	public abstract void setSql(String sql);
	
	/**
	 * @return the jdbcDriver
	 */
	public abstract String getJdbcDriver();

	/**
	 * @param jdbcDriver the jdbcDriver to set
	 */
	public abstract void setJdbcDriver(String jdbcDriver);

	/**
	 * @return the connectionURL
	 */
	public abstract String getConnectionURL();

	/**
	 * @param connectionURL the connectionURL to set
	 */
	public abstract void setConnectionURL(String connectionURL);

	/**
	 * @return the dbUserName
	 */
	public abstract String getDbUserName();

	/**
	 * @param dbUserName the dbUserName to set
	 */
	public abstract void setDbUserName(String dbUserName);
	
	/**
	 * @return the dbPassword
	 */
	public abstract char[] getDbPassword();
	
	/**
	 * @param dbPassword the dbPassword to set
	 */
	public abstract void setDbPassword(char[] dbPassword);

}