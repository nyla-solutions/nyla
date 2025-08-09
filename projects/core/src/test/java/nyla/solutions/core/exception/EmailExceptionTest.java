package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmailExceptionTest {

    private String message = "Hello world";

    @Test
    void constructor() {
        var subject = new EmailException();

        assertEquals(EmailException.DEFAULT_EMAIL_ERROR_MSG, subject.getMessage());
        assertEquals(EmailException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_message() {
        var subject = new EmailException(message);

        assertEquals(message, subject.getMessage());
        assertEquals(EmailException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}