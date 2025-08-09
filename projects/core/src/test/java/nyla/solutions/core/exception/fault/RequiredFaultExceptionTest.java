package nyla.solutions.core.exception.fault;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequiredFaultExceptionTest {

    @Test
    void cause() {
        Exception cause = new Exception("cause");
        assertEquals(cause, new RequiredFaultException(cause).getCause());
    }

    @Test
    void message() {
        String message = "message";
        assertEquals(message,new RequiredFaultException(message).getMessage());
    }

    @Test
    void category() {
        assertEquals("DEFAULT",new RequiredFaultException().getCategory());
    }

    @Test
    void code() {
        assertEquals("REQ",new RequiredFaultException().getCode());
    }

}