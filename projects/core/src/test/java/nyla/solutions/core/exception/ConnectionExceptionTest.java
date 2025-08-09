package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionExceptionTest {

    private String message = "Hello world";
    private Exception exception = new IllegalArgumentException("junit");


    @Test
    void construct_with_msg_exception() {
        var subject = new ConnectionException(message,exception);
        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(ConnectionException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_msg() {
        var subject = new ConnectionException(message);
        assertEquals(message, subject.getMessage());
        assertEquals(ConnectionException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct() {
        var subject = new ConnectionException();
        assertEquals(ConnectionException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}