package nyla.solutions.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementConstructor<T>
{
	
	public void constructPreparedStatement(Connection connection, PreparedStatement preparedStatement, T obj)
	throws SQLException;

}
