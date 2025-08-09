package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegrityConstraintExceptionTest {

    private String message =  "Hello world";
    private Throwable exception = new IllegalArgumentException();

    @Test
    void constructor() {

        var subject = new IntegrityConstraintException();
        assertEquals(IntegrityConstraintException.DEFAULT_INTEGRITY_CONSTRAINT_ERROR_MSG, subject.getMessage());
        assertEquals(IntegrityConstraintException.DEFAULT_ERROR_CODE, subject.getCode());
    }


    @Test
    void constructor_message() {

        var subject = new IntegrityConstraintException(message);
        assertEquals(message, subject.getMessage());
        assertEquals(IntegrityConstraintException.DEFAULT_ERROR_CODE, subject.getCode());
    }


    @Test
    void constructor_message_exception() {

        var subject = new IntegrityConstraintException(message,exception);
        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());

        assertEquals(IntegrityConstraintException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_exception() {

        var subject = new IntegrityConstraintException(exception);
        assertEquals(exception, subject.getCause());

        assertEquals(IntegrityConstraintException.DEFAULT_ERROR_CODE, subject.getCode());
    }
}