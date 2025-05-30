package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TooManyRowsExceptionTest {

    private String message = "Error";

    @Test
    void constructor() {
        var subject = new TooManyRowsException();

        assertEquals(TooManyRowsException.TOO_MANY_ROWS_ERROR_MSG,subject.getMessage());
    }

    @Test
    void constructor_message() {
        var subject = new TooManyRowsException(message);

        assertEquals(message,subject.getMessage());
    }
}