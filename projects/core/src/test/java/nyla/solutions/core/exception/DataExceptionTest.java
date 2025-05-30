package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataExceptionTest {

    private String message = "Internal";
    private Exception exception = new IllegalArgumentException("junit");
    private String notes = "notes";
    private String programName = "junit";
    private String functionName = "function";
    private String errorCategory = "error";
    private String errorCode = "code001";

    @Test
    void construct() {
        var subject = new DataException();

        assertEquals(DataException.DEFAULT_MESSAGE, subject.getMessage());
        assertEquals(DataException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(DataException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_message() {
        var subject = new DataException(message);

        assertEquals(message, subject.getMessage());
        assertEquals(DataException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(DataException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_message_exception() {
        var subject = new DataException(message,exception);

        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
        assertEquals(DataException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(DataException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_exception() {
        var subject = new DataException(exception);

        assertEquals(exception, subject.getCause());
        assertEquals(DataException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(DataException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_args() {
        var subject = new DataException(message,notes,programName,functionName,errorCategory,errorCode);

        assertEquals(message, subject.getMessage());
        assertEquals(notes, subject.getNotes());
        assertEquals(programName, subject.getModule());
        assertEquals(functionName, subject.getOperation());
        assertEquals(errorCategory, subject.getCategory());
        assertEquals(errorCode, subject.getCode());
    }
}