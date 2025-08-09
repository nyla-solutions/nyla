package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoDataFoundExceptionTest {

    private String message = "Hello world";

    @Test
    void constructor() {
        var subject = new NoDataFoundException();

        assertEquals(NoDataFoundException.DEFAULT_NO_DATA_ERROR_MSG, subject.getMessage());
    }

    @Test
    void constructor_with_message() {

        var subject = new NoDataFoundException(message);

        assertEquals(message, subject.getMessage());
    }
}