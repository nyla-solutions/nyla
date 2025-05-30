package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FatalExceptionTest {

    private String message = "Hello world";
    private Exception exception = new IllegalArgumentException("junit");
    private String notes = "notes";
    private String programName = "program";
    private String functionName = "function";
    private String errorCategory = "category";
    private String errorCode = "code";

    @Test
    void constructor() {

        var subject = new FatalException();
        assertEquals(FatalException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(FatalException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_message() {

        var subject = new FatalException(message);

        assertEquals(message, subject.getMessage());

        assertEquals(FatalException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(FatalException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_exception() {

        var subject = new FatalException(exception);

        assertEquals(exception, subject.getCause());

        assertEquals(FatalException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(FatalException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_message_exception() {

        var subject = new FatalException(message,exception);

        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());

        assertEquals(FatalException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(FatalException.DEFAULT_ERROR_CODE, subject.getCode());
    }


    @Test
    void constructor_args() {

        var subject = new FatalException(message,notes,programName,
                functionName,errorCategory, errorCode);

        assertEquals(message, subject.getMessage());
        assertEquals(notes, subject.getNotes());
        assertEquals(programName, subject.getModule());
        assertEquals(functionName, subject.getOperation());

        assertEquals(errorCategory, subject.getCategory());
        assertEquals(errorCode, subject.getCode());
    }
}