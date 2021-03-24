package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.util.Cryption;
import nyla.solutions.core.util.Debugger;

import java.sql.*;
import java.util.Map;

/**
 * @author Gregory Green
 */
public class Sql
{

    public static Connection createConnection(String aDriver, String aConnectionURL,
                                       String aUser, char[] aPassword)
            throws ConnectionException
    {
        try {
            Connection connection = null;

            Class.forName(aDriver);

            String password = null;
            if (aPassword != null) {
                password = new String(aPassword);
                if (password.indexOf(Cryption.CRYPTION_PREFIX) < 0) {
                    Debugger.printWarn("Provided password is not encrypted!");
                } else {
                    //decrypt password
                    password = Cryption.interpret(password);
                }
            }
            //check if connection is encrypted
            connection = DriverManager.getConnection(
                    aConnectionURL,
                    aUser,
                    password
            );

            return connection;
        }
        catch (ClassNotFoundException e) {
            throw new ConnectionException(e);
        }
        catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }//--------------------------------------------

    public <T> T queryForColumn(Connection targetConnection, String sql, int columnIndex,Class<T> returnClass)
            throws SQLException
    {
        try(Statement statement = targetConnection.createStatement())
        {
            try(ResultSet resultSet = statement.executeQuery(sql))
            {
                if(!resultSet.next())
                    return null;

                Object obj = resultSet.getObject(columnIndex);
                Class<?> objClass = obj.getClass();

                if(!objClass.isAssignableFrom(returnClass))
                    throw new ClassCastException("obj:"+obj+" class:"+objClass+ " not assignable from "+returnClass);

                return (T)obj;
            }
        }

    }

    public Map<String,?> queryForMap(Connection connection, ResultSetToMapConverter converter, String sql)
            throws SQLException
    {
        try(Statement stmt = connection.createStatement())
        {
            try(ResultSet resultSet = stmt.executeQuery(sql))
            {
                if(!resultSet.next())
                    return null;

                return converter.convert(resultSet);

            }
        }
    }

    public Map<String,?> queryForMap(Connection connection, String sql) throws SQLException
    {
        return queryForMap(connection,new ResultSetToMapConverter(),sql);
    }

    public Map<String, ?> toMap(ResultSet resultSet)
    {
        return toMap(resultSet, new ResultSetToMapConverter());
    }

    protected Map<String, ?> toMap(ResultSet resultSet, ResultSetToMapConverter converter)
    {

        return converter.convert(resultSet);

    }

    public void execute(Connection connection, String sql) throws SQLException
    {
        try(Statement stmt = connection.createStatement())
        {
            stmt.execute(sql);
        }
    }
}
