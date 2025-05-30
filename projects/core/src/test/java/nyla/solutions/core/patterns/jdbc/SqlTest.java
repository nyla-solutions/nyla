package nyla.solutions.core.patterns.jdbc;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static nyla.solutions.core.util.Config.settings;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * SqlTest unit testing Sql
 * @author gregory green
 */
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
    void classNotFound() {
        char[] userPassword = null;
        String driver = "INVALID";
        String connectionUrl = "jdbc:h2:mem:test";
        String user = "user";

        try {
            Sql.createConnection(driver, connectionUrl,
                    user, userPassword);
        }
        catch(ConnectionException e)
        {
            assertThat(e.getMessage()).contains(driver);
        }


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
    void given_sqlConnection_when_prepareStatement_then_ConstructPreparedStatement() throws SQLException
    {
        PreparedStatement expected = mock(PreparedStatement.class);
        when(connection.prepareStatement(any())).thenReturn(expected);
        BindVariableInterpreter bindVariableInterpret = mock(BindVariableInterpreter.class);

        PreparedStatement actual = subject.prepareStatement(connection,bindVariableInterpret);

        assertEquals(expected,actual);


    }

    @Test
    void queryForMapWithBinding() throws SQLException
    {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        String sql = "Select * from people";
        Map<String,?> expected = Organizer.toMap("Hello","World");

        when(resultSet.next()).thenReturn(false);
        ResultSetToMapConverter converter = mock(ResultSetToMapConverter.class);

        Map<String,?> actual = subject.queryForMapWithFields(connection,converter,sql,1,2,3);
        verify(resultSet).next();
        verify(preparedStatement,times(3)).setObject(anyInt(),any());
    }

    @Test
    void queryForMapWithBinding_given_DefaultConverter() throws SQLException
    {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        String sql = "Select * from people";
        Map<String,?> expected = Organizer.toMap("Hello","World");

        when(resultSet.next()).thenReturn(false);

        Map<String,?> actual = subject.queryForMapWithFields(connection,sql,1,2,3);
        verify(resultSet).next();
        verify(preparedStatement,times(3)).setObject(anyInt(),any());
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

    @Test
    void given_connectionAndObject_When_executeUpdate_Then_insert() throws SQLException
    {
        Integer expectedCount = 1;

        BindVariableInterpreter bindVariableInterpreter = mock(BindVariableInterpreter.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(bindVariableInterpreter.constructPreparedStatementWithMap(any(),any())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(expectedCount);

        UserProfile expectedData = JavaBeanGeneratorCreator.of(UserProfile.class).create();
        String sql = "insert into table_name(email,firstName) values (:email,:firstName)";

        int actualCount = subject.executeUpdateWithJavaBean(connection,bindVariableInterpreter,expectedData);

        assertEquals(expectedCount,actualCount);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void given_connectionAndObject_When_executeUpdateSqlWithJavaBean_Then_insert() throws SQLException
    {
        Integer expectedCount = 1;

        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(expectedCount);

        UserProfile expectedData = JavaBeanGeneratorCreator.of(UserProfile.class).create();
        String sql = "insert into table_name(email,firstName) values (:email,:firstName)";

        int actualCount = subject.executeUpdateSqlWithJavaBean(connection,sql,expectedData);

        assertEquals(expectedCount,actualCount);
        verify(preparedStatement).executeUpdate();
    }

    public static Connection openConnection()
    {
        String driver = settings().getProperty("test.sql.driver","org.h2.Driver");
        String connectionURL = settings().getProperty("test.sql.connectionURL");
        String user = settings().getProperty("test.sql.user");
        char[] password = settings().getPropertyPassword("test.sql.password");
        Connection connection = Sql.createConnection(driver,connectionURL,user,password);
        return connection;
    }
}