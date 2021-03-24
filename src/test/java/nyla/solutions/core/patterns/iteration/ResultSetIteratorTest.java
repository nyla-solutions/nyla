package nyla.solutions.core.patterns.iteration;

import nyla.solutions.core.exception.CommunicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultSetIteratorTest
{
    private ResultSet mockResultSet;
    private ResultSetIterator subject;

    @BeforeEach
    public void setUp()
    {
        mockResultSet = mock(ResultSet.class);
        subject = new ResultSetIterator(mockResultSet);
    }

    @Test
    void hasNext_null_false()
    {
        subject = new ResultSetIterator(null);
        assertFalse(subject.hasNext());
    }
    @Test
    void hasNext_returns_false()
    {
        assertFalse(subject.hasNext());
    }
    @Test
    void hasNext_throws_CommunicationException()
    throws SQLException
    {
        when(mockResultSet.next()).thenThrow(new SQLException());

        assertThrows(CommunicationException.class, () -> subject.hasNext());
    }



    @Test
    void hasNext_closing_resultSet()
    throws SQLException
    {
        subject.hasNext();
        verify(mockResultSet).close();
    }

    @Test
    void hasNext_true()
    throws SQLException
    {
        when(mockResultSet.next()).thenReturn(true);

        assertTrue(subject.hasNext());
    }

    @Test
    void next_returns_resultSet()
    throws SQLException
    {
       ResultSet actual = subject.next();
       assertNotNull(actual);
    }
    @Test
    void next_null_resultSet_throwsException()
    throws SQLException
    {
        subject = new ResultSetIterator(null);

        assertThrows(NoSuchElementException.class,
                () -> subject.next());

    }

    @Test
    void dispose()
    throws SQLException
    {
        subject.dispose();

        verify(mockResultSet).close();
    }

    @Test
    void remove()
    {
        subject.remove();
    }
}