package nyla.solutions.core.exception;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static nyla.solutions.core.util.Organizer.toMap;
import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyExceptionTest {

    private String message = "Hello world";
    private Exception exception = new IllegalArgumentException("junit");
    private String id = Text.generateId();
    private Map<String,Object> map = toMap("name","John Smith");

    @Test
    void construct() {
        var subject = new ConcurrencyException();
        assertEquals(ConcurrencyException.DEFAULT_ERROR_CODE, subject.getCode());
    }

    @Test
    void construct_with_message() {
        var subject = new ConcurrencyException(message);
        assertEquals(ConcurrencyException.DEFAULT_ERROR_CODE, subject.getCode());
        assertEquals(message, subject.getMessage());
    }

    @Test
    void construct_with_message_and_exception() {
        var subject = new ConcurrencyException(message,exception);
        assertEquals(ConcurrencyException.DEFAULT_ERROR_CODE, subject.getCode());
        assertEquals(message, subject.getMessage());
        assertEquals(exception, subject.getCause());
    }



}