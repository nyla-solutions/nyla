package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuplicateRowExceptionTest {

    private String message = "hello world";
    private Throwable exception = new IllegalArgumentException("junit");

    @Test
    void constructor() {
        var subject = new DuplicateRowException();

        assertEquals(DuplicateRowException.DEFAULT_DUPLICATE_ROW_MESSAGE, subject.getMessage());
        assertEquals(DuplicateRowException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_message() {

        var subject = new DuplicateRowException(message);

        assertEquals(message, subject.getMessage());
        assertEquals(DuplicateRowException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_message_exception() {

        var subject = new DuplicateRowException(message,exception);

        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(DuplicateRowException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_exception() {

        var subject = new DuplicateRowException(exception);

        assertEquals(exception, subject.getCause());
        assertEquals(DuplicateRowException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}