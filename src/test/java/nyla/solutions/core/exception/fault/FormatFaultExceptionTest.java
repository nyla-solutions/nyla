package nyla.solutions.core.exception.fault;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormatFaultExceptionTest {
    @Test
    void cause() {
        Exception cause = new Exception("cause");
        assertEquals(cause, new FormatFaultException(cause).getCause());
    }

    @Test
    void message() {
        String message = "message";
        assertEquals(message,new FormatFaultException(message).getMessage());
    }

    @Test
    void category() {
        assertEquals("DEFAULT",new FormatFaultException().getCategory());
    }

    @Test
    void code() {
        assertEquals("FORMAT",new FormatFaultException().getCode());
    }
}