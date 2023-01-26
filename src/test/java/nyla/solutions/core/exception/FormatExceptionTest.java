package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormatExceptionTest {

    private String message = "formatting";
    private Throwable exception = new IllegalArgumentException("junit");

    @Test
    void constructor() {
        var subject = new FormatException();
        assertEquals(FormatException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_message() {
        var subject = new FormatException(message);
        assertEquals(message, subject.getMessage());
        assertEquals(FormatException.DEFAULT_ERROR_CODE, subject.getCode());
    }
    @Test
    void constructor_message_exception() {
        var subject = new FormatException(message,exception);
        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(FormatException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_exception() {
        var subject = new FormatException(exception);
        assertEquals(exception, subject.getCause());
        assertEquals(FormatException.DEFAULT_ERROR_CODE, subject.getCode());
    }

}