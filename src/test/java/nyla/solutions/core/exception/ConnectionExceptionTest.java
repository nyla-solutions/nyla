package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionExceptionTest {

    private String message = "Hello world";
    private Exception exception = new IllegalArgumentException("junit");


    @Test
    void construct_with_msg_exception() {
        var subject = new ConcurrencyException(message,exception);
        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(ConcurrencyException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_msg() {
        var subject = new ConcurrencyException(message);
        assertEquals(message, subject.getMessage());
        assertEquals(ConcurrencyException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct() {
        var subject = new ConcurrencyException();
        assertEquals(ConcurrencyException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}