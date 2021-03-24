package nyla.solutions.core.patterns.jdbc.batch;

import nyla.solutions.core.patterns.jdbc.SqlTest;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class ResultSetSupplierTest
{

    @Test
    void executeFromConnection() throws SQLException
    {
        String ddl = "CREATE TABLE Persons (\n" +
                "    PersonID int,\n" +
                "    LastName varchar(255)\n" +
                ");";

        String sql = "select 1";
        Connection connection = SqlTest.openConnection();
        Statement statement = connection.createStatement();
        statement.execute(ddl);

        String insertSql = "insert into Persons(PersonID,LastName) values (1, 'Test');";
        statement.execute(insertSql);

        ResultSet expected = statement.executeQuery(sql);

        ResultSetSupplier subject = new ResultSetSupplier(expected);
        ResultSet actual = subject.get();
        assertNotNull(actual);

        actual = subject.get();
        assertNull(actual);

        statement.close();
        connection.close();

    }

    @Test
    void get_return_null_when_no_results()
    {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetSupplier subject = new ResultSetSupplier(resultSet);
        assertNull(subject.get());

    }

    @Test
    void get_return_when_results() throws SQLException
    {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true);
        ResultSetSupplier subject = new ResultSetSupplier(resultSet);
        assertNotNull(subject.get());

    }

    @Test
    void close_given_resultSet() throws SQLException
    {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetSupplier subject = new ResultSetSupplier(resultSet);
        subject.close();
        verify(resultSet).close();
    }

}