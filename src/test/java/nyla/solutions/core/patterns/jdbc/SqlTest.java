package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class SqlTest
{
    private Sql subject;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @Test
    void formatString()
    {
        assertEquals("'hello'",Sql.formatString("hello"));
        assertEquals("'he''llo'",Sql.formatString("he'llo"));

        assertEquals("''",Sql.formatString(""));
        assertEquals("NULL",Sql.formatString(null));
    }

    @BeforeEach
    void setUp() throws SQLException
    {
        subject = new Sql();
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        resultSet = mock(ResultSet.class);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
    }

    @Test
    void queryForMap() throws SQLException
    {
        String sql = "Select * from people";
        Map<String,Object> expected = new HashMap<>();
        expected.put("hello","world");

        ResultSetToMapConverter converter = mock(ResultSetToMapConverter.class);
        Map<String,?> actual = subject.queryForMap(connection,converter,sql);
        verify(converter,atMostOnce()).convert(any());
    }

    @Test
    void queryForMap_withDefaultConverter() throws SQLException
    {
        String sql = "Select * from people";
        Map<String,Object> expected = new HashMap<>();
        expected.put("hello","world");

        when(resultSet.next()).thenReturn(false);
        Map<String,?> actual = subject.queryForMap(connection,sql);
        verify(resultSet).next();

    }

    @Test
    void execute() throws SQLException
    {
        String insert = "insert into";
        subject.execute(connection,insert);
        verify(connection).createStatement();
        verify(statement).execute(anyString());
    }

    @Test
    void queryForColumn() throws SQLException
    {
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

    @Test
    void toMap() throws SQLException
    {
        Sql subject = new Sql();
        ResultSetToMapConverter converter = mock(ResultSetToMapConverter.class);
        final Map<String, ?> stringMap = subject.toMap(resultSet, converter);
        verify(converter).convert(any());
        verify(resultSet,never()).next();

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