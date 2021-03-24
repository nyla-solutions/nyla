package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SelectResultSetConverterSupplierTest
{
    private Supplier<Connection> connections;
    private SelectResultSetConverterSupplier subject;
    private Converter<ResultSet, UserProfile> converter = mock(Converter.class);
    private String sql = "select * from test where id = ?";
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private UserProfile expected;
    @BeforeEach
    void setUp() throws SQLException
    {
        converter = mock(Converter.class);
        connections = mock(Supplier.class);
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        expected = new UserProfile();
        when(connections.get()).thenReturn(connection);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(sql)).thenReturn(statement);

        subject = new SelectResultSetConverterSupplier(connections,
                converter, sql);

    }

    @Test
    void constructWithParameters() throws SQLException
    {
        Object[] parameters = {"hello"};
        subject = new SelectResultSetConverterSupplier(connections,
                converter, sql,parameters);

        assertThat(parameters).contains("hello");
    }

    @Test
    void instanceofCloseable()
    {
        assertTrue(subject instanceof Closeable);
    }

    @Test
    void get_connects_firsts() throws SQLException
    {
        subject.get();

        verify(connections).get();
    }

    @Test
    void get() throws SQLException
    {
        subject.connect();

        when(resultSet.next()).thenReturn(true).thenReturn(false);

        when(converter.convert(any())).thenReturn(expected);


        assertNotNull(subject.get());
        verify(connections,times(1)).get();
        verify(converter).convert(any());

    }

    @Test
    void connect() throws SQLException
    {
        subject.connect();
        verify(connections).get();
        verify(connection).prepareStatement(sql);
        verify(statement).executeQuery();
    }

    @Test
    void getSetSql()
    {
        String expected = "select * from hello";
        this.subject.setSql(expected);

        assertEquals(expected,this.subject.getSql());
    }

    @Test
    void setParameters_nullDoesNotThrowException() throws SQLException
    {
        this.subject.setParameters(null);
        this.subject.connect();
    }
        @Test
    void setParameters() throws SQLException
    {

        LocalDateTime now = LocalDateTime.now();
        Timestamp expectedTimestamp = Timestamp.valueOf(now);
        String expectedText = "text";
        BigInteger expectedNumber = BigInteger.ONE;
        subject.setParameters(expectedTimestamp,expectedText,expectedNumber);

        Object[] actual = subject.getParameters();
        assertThat(actual).contains(expectedTimestamp);
        assertThat(actual).contains(expectedText);
        assertThat(actual).contains(expectedNumber);

        subject.connect();
        verify(statement).setObject(1,expectedTimestamp);
        verify(statement).setObject(2,expectedText);
        verify(statement).setObject(3,expectedNumber);
    }

    @Test
    void setParameterNotSetIfSqlDoesNotBindVariable() throws SQLException
    {
        sql = "select * from test";
        setUp();

        LocalDateTime now = LocalDateTime.now();
        Timestamp expectedTimestamp = Timestamp.valueOf(now);
        String expectedText = "text";
        BigInteger expectedNumber = BigInteger.ONE;
        subject.setParameters(expectedTimestamp,expectedText,expectedNumber);

        Object[] actual = subject.getParameters();
        assertThat(actual).contains(expectedTimestamp);
        assertThat(actual).contains(expectedText);
        assertThat(actual).contains(expectedNumber);

        subject.connect();
        verify(statement,never()).setObject(1,expectedTimestamp);
        verify(statement,never()).setObject(2,expectedText);
        verify(statement,never()).setObject(3,expectedNumber);

    }

    @Test
    void dispose() throws SQLException
    {
        subject.connect();
        subject.dispose();
        verify(connection).close();
        verify(statement).close();
        verify(resultSet).close();
    }
}