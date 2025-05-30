package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeOutExceptionTest {

    private String message = "Hello world";

    @Test
    void constructor() {
        var subject = new TimeOutException();

        assertEquals(TimeOutException.TIMEOUT_MSG, subject.getMessage());
    }

    @Test
    void constructor_with_msg() {
        var subject = new TimeOutException(message);

        assertEquals(message, subject.getMessage());
    }
}