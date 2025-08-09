package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigLockExceptionTest {

    private String message = "Hello world";

    @Test
    void construct_with_message() {
        var subject = new ConfigLockException(message);

        assertEquals(message, subject.getMessage());
    }
}