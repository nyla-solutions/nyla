package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotImplementedExceptionTest {

    @Test
    void constructor() {

        var subject = new NotImplementedException();
        assertEquals("Not Implemented", subject.getMessage());

    }
}