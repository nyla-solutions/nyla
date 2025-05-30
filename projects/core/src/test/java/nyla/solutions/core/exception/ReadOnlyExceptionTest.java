package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadOnlyExceptionTest {

    private String field = "firstName";

    @Test
    void constructor() {
        var subject = new ReadOnlyException(field);

        var message = subject.getMessage();

        assertNotNull(message);
    }
}