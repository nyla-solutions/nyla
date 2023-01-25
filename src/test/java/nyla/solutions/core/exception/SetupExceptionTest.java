package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetupExceptionTest {

    private String message = "Message";
    private Exception exception = new IllegalArgumentException("junit");

    @Test
    void constructor() {
        var subject = new SetupException();

        assertEquals(SetupException.SETUP_ERROR_MSG, subject.getMessage());
        assertEquals(SetupException.DEFAULT_ERROR_CODE, subject.getCode());
        assertEquals(SetupException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
    }

    @Test
    void constructor_with_msg() {
        var subject = new SetupException(message);

        assertEquals(message, subject.getMessage());
        assertEquals(SetupException.DEFAULT_ERROR_CODE, subject.getCode());
        assertEquals(SetupException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
    }

    @Test
    void constructor_with_exception() {
        var subject = new SetupException(exception);

        assertEquals(exception, subject.getCause());
        assertEquals(SetupException.DEFAULT_ERROR_CODE, subject.getCode());
        assertEquals(SetupException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
    }

    @Test
    void constructor_with_message_exception() {
        var subject = new SetupException(message,exception);

        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(SetupException.DEFAULT_ERROR_CODE, subject.getCode());
        assertEquals(SetupException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
    }
}