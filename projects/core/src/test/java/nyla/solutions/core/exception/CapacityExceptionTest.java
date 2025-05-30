package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapacityExceptionTest {

    @Test
    void message() {
        assertEquals("test", new CapacityException("test").getMessage());
    }


    @Test
    void defaultMessage() {
        assertEquals("Capacity Exception", new CapacityException().getMessage());
    }
}