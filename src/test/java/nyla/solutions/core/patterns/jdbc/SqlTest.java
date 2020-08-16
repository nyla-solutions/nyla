package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlTest
{
    @Test
    void queryForColumn() throws SQLException
    {
        Sql subject = new Sql();

        Connection connection = mock(Connection.class);

        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        ResultSet resultSet = mock(ResultSet.class);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        int expectedResult = 3;
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getObject(anyInt())).thenReturn(expectedResult);

        int actualCount = subject.queryForColumn(connection,"select 3 from dual",1, Integer.class);
        assertEquals(expectedResult,actualCount);
    }

    @Test
    void createConnection() throws SQLException
    {
        Connection connection = openConnection();

        assertNotNull(connection);
        connection.close();

    }

    public static Connection openConnection()
    {
        String driver = Config.getProperty("test.sql.driver","org.h2.Driver");
        String connectionURL = Config.getProperty("test.sql.connectionURL");
        String user = Config.getProperty("test.sql.user");
        char[] password = Config.getPropertyPassword("test.sql.password");
        Connection connection = Sql.createConnection(driver,connectionURL,user,password);
        return connection;
    }
}