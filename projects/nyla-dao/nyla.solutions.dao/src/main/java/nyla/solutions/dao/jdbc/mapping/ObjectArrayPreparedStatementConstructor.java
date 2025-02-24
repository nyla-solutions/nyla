package nyla.solutions.dao.jdbc.mapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import nyla.solutions.dao.PreparedStatementConstructor;

/**
 * Set prepared statement with arguments for object array
 * @author Gregory Green
 *
 */
public class ObjectArrayPreparedStatementConstructor implements
		PreparedStatementConstructor<Object[]>
{

	public void constructPreparedStatement(Connection connection,
			PreparedStatement preparedStatement, Object[] objs)
			throws SQLException
	{
		if(objs == null || objs.length == 0)
			return;
		
		for (int i = 0; i < objs.length; i++)
		{
			preparedStatement.setObject(i+1, objs[i]);	
		}
	}

}
