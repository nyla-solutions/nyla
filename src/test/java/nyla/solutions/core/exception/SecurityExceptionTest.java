package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for SecurityException
 * @author gregory green
 */
class SecurityExceptionTest {
    private Exception exception = new IllegalArgumentException("junit");
    private String message = "Error";

    @Test
    void constructor() {

        var subject = new SecurityException();
        assertEquals(SecurityException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SecurityException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_exception() {

        var subject = new SecurityException(exception);
        assertEquals(exception, subject.getCause());
        assertEquals(SecurityException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SecurityException.DEFAULT_ERROR_CODE, subject.getCode());
    }


    @Test
    void constructor_message() {

        var subject = new SecurityException(message);
        assertEquals(message, subject.getMessage());
        assertEquals(SecurityException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SecurityException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_message_exception() {

        var subject = new SecurityException(message,exception);
        assertEquals(exception, subject.getCause());
        assertEquals(message, subject.getMessage());
        assertEquals(SecurityException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SecurityException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}