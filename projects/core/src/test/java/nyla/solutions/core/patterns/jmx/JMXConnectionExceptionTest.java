package nyla.solutions.core.patterns.jmx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JMXConnectionExceptionTest
{
    @Test
    void constructor()
    {
        assertDoesNotThrow(()-> new JMXConnectionException());
    }

    @Test
    void constructor_message_cause()
    {
        String message = "hey";
        Throwable cause = new RuntimeException();
        JMXConnectionException subject = new JMXConnectionException(message, cause);
        assertEquals(message,subject.getMessage());
        assertEquals(cause,subject.getCause());
    }

    @Test
    void constructor_cause()
    {
        Throwable cause = new RuntimeException();
        JMXConnectionException subject = new JMXConnectionException( cause);
        assertEquals(cause,subject.getCause());
    }

    @Test
    void constructor_message()
    {
        String message = "hey";
        JMXConnectionException subject = new JMXConnectionException(message);
        assertEquals(message,subject.getMessage());
    }
}