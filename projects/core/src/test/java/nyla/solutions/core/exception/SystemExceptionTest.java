package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SystemExceptionTest {

    private String message = "Hello world";
    private Exception exception = new IllegalArgumentException("junit");
    private String notes = "Notes";
    private String programName = "program";
    private String functionName = "functions";
    private String errorCategory = "category";
    private String errorCode = "001";

    @Test
    void constructor() {
        var subject = new SystemException();

        assertEquals(SystemException.INTERNAL_ERROR_MSG, subject.getMessage());
        assertEquals(SystemException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SystemException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_message() {
        var subject = new SystemException(message);

        assertEquals(message, subject.getMessage());
        assertEquals(SystemException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SystemException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_message_exception() {
        var subject = new SystemException(message,exception);

        assertEquals(message, subject.getMessage());
        assertEquals(SystemException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SystemException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void constructor_with_exception() {
        var subject = new SystemException(exception);

        assertEquals(exception, subject.getCause());
        assertEquals(SystemException.DEFAULT_ERROR_CATEGORY, subject.getCategory());
        assertEquals(SystemException.DEFAULT_ERROR_CODE, subject.getCode());
    }


    @Test
    void constructor_with_args() {
        var subject = new SystemException(message,notes,programName,
                functionName,errorCategory,errorCode);

        assertEquals(message, subject.getMessage());
        assertEquals(notes, subject.getNotes());
        assertEquals(programName, subject.getModule());
        assertEquals(functionName, subject.getOperation());
        assertEquals(errorCategory, subject.getCategory());
        assertEquals(errorCode, subject.getCode());

    }

}