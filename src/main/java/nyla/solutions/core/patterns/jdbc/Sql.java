package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Cryption;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;

import java.sql.*;
import java.util.Map;

/**
 * Sql, utility object for executing SQL statements
 * @author Gregory Green
 */
public class Sql
{

    public static String formatString(String associations)
    {
        if (associations == null)
            return "NULL";

        return "'" + associations.replace("'", "''") + "'";
    }

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

    /**
     *
     * @param targetConnection the target connection
     * @param sql the SQL for the query
     * @param columnIndex the column index starting at 1
     * @param returnClass the Class Type of the return object
     * @param <T> the return object class type
     * @return the mapped object
     * @throws SQLException when connection/SQL issues occurs
     */
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

    /**
     * Execute query to map returned
     * @param connection the connection
     * @param converter the converse reuslts to map
     * @param sql the SQL to executed
     * @return the Map
     * @throws SQLException
     */
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

    /**
     * query for data to results as Map. The method will also use prepared statements setting the appropriate fields
     * @param connection the connection
     * @param sql the SQL to execute
     * @param fields the field for prepared statements
     * @return the ResultSetToMapConverter converted based on the SQL
     * @throws SQLException
     */
    public Map<String, ?> queryForMapWithFields(Connection connection, String sql, Object... fields) throws SQLException
    {
        return queryForMapWithFields(connection,new ResultSetToMapConverter(),sql,fields);
    }

    /**
     * query for data to results as Map. The method will also use prepared statements setting the appropriate fields
     * @param connection the connection
     * @param converter the convert to map object
     * @param sql the SQL to execute
     * @param fields the field for prepared statements
     * @return the ResultSetToMapConverter converted based on the SQL
     * @throws SQLException
     */
    public Map<String, ?> queryForMapWithFields(Connection connection, ResultSetToMapConverter converter, String sql, Object... fields) throws SQLException
    {
        try(PreparedStatement stmt = connection.prepareStatement(sql))
        {
            if(fields!= null && fields.length > 0 )
            {
                //Initial prepared Statement
                for (int i = 0;i< fields.length;i++)
                {
                    stmt.setObject(i+1,fields[i]);
                }
            }

            try(ResultSet resultSet = stmt.executeQuery())
            {
                if(!resultSet.next())
                    return null;

                return converter.convert(resultSet);

            }
        }
    }

    /**
     * Execute a SQL query and return Map
     * @param connection the conneciton
     * @param sql the SQL
     * @return the Map to return
     * @throws SQLException the exception
     */
    public Map<String,?> queryForMap(Connection connection, String sql) throws SQLException
    {
        return queryForMap(connection,new ResultSetToMapConverter(),sql);
    }

    /**
     * Convert the results for a set into a Map
     * @param resultSet the results
     * @return the map with columns and values from Result Set
     */
    public Map<String, ?> toMap(ResultSet resultSet)
    {
        return toMap(resultSet, new ResultSetToMapConverter());
    }

    protected Map<String, ?> toMap(ResultSet resultSet, ResultSetToMapConverter converter)
    {

        return converter.convert(resultSet);

    }

    /**
     * Execute a SQL statement in a connection
     * @param connection the conneciton
     * @param sql the SQL
     * @throws SQLException
     */
    public void execute(Connection connection, String sql) throws SQLException
    {
        try(Statement stmt = connection.createStatement())
        {
            stmt.execute(sql);
        }
    }

    /**
     *  Execute an SQL with the given bind variables using values for a Java Bean
     * @param connection the JDBC connection
     * @param bindVariableInterpreter the bind interpreter that understand variable and has the SQL statement
     * @param expectedData the Java Bean
     * @param <T> the type Java Bean
     * @return the update count
     * @throws SQLException when an SQL error occurs
     */
    public <T> int executeUpdateWithJavaBean(Connection connection, BindVariableInterpreter bindVariableInterpreter, T expectedData) throws SQLException
    {
        Map<?,?> map = JavaBean.toMap(expectedData);
        try(PreparedStatement preparedStatement = bindVariableInterpreter.constructPreparedStatementWithMap(connection,map))
        {
            return preparedStatement.executeUpdate();
        }
    }

    /**
     * Execute an SQL with the given SQL with bind variables using values for a Java Bean
     * @param connection the JDBC connection
     * @param sqlWithBindVariables ex: insert into TARGET_Persons(email,firstName) values (:email,:firstName)
     * @param expectedData the java bean
     * @param <T> the java bean type
     * @return the return for a prepared statement executeUpdate
     * @throws SQLException when an SQL error occurs
     */
    public <T> int executeUpdateSqlWithJavaBean(Connection connection, String sqlWithBindVariables,  T expectedData) throws SQLException
    {
        return executeUpdateWithJavaBean(connection,new BindVariableInterpreter(sqlWithBindVariables),expectedData);
    }

    /**
     *
     * @param connection the connection
     * @param bindVariableInterpret the interpret with the statement
     * @return the prepared statement
     */
    public PreparedStatement prepareStatement(Connection connection, BindVariableInterpreter bindVariableInterpret) throws SQLException
    {
        return connection.prepareStatement(bindVariableInterpret.toPreparedStmtSql());
    }
}
