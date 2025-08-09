package nyla.solutions.core.patterns.jmx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JMXSecurityExceptionTest
{


    @Test
    void constructor()
    {
        assertDoesNotThrow(()-> new JMXSecurityException());
    }

    @Test
    void constructor_message_cause()
    {
        String message = "hey";
        Throwable cause = new RuntimeException();
        JMXSecurityException subject = new JMXSecurityException(message, cause);
        assertEquals(message,subject.getMessage());
        assertEquals(cause,subject.getCause());
    }

    @Test
    void constructor_cause()
    {
        Throwable cause = new RuntimeException();
        JMXSecurityException subject = new JMXSecurityException( cause);
        assertEquals(cause,subject.getCause());
    }

    @Test
    void constructor_message()
    {
        String message = "hey";
        JMXSecurityException subject = new JMXSecurityException(message);
        assertEquals(message,subject.getMessage());
    }

}