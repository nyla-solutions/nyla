package nyla.solutions.dao.jdbc;

import java.sql.Connection;

import nyla.solutions.dao.Connectable;

public interface JdbcConnectable extends Connectable
{
	/**
	 * 
	 * @param connection the connection to establish
	 */
	void setConnection(Connection connection);
}
