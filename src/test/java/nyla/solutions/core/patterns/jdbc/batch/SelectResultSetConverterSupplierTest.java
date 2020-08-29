package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SelectResultSetConverterSupplierTest
{
    private Supplier<Connection> connections;
    private SelectResultSetConverterSupplier subject;
    private Converter<ResultSet, UserProfile> converter = mock(Converter.class);
    private String sql = "";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private UserProfile expected;
    @BeforeEach
    void setUp() throws SQLException
    {
        converter = mock(Converter.class);
        connections = mock(Supplier.class);
        connection = mock(Connection.class);
        statement = mock(Statement.class);
        resultSet = mock(ResultSet.class);
        expected = new UserProfile();
        when(connections.get()).thenReturn(connection);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(connection.createStatement()).thenReturn(statement);

        subject = new SelectResultSetConverterSupplier(connections,
                converter, sql);

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
        verify(connection).createStatement();
        verify(statement).executeQuery(sql);
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