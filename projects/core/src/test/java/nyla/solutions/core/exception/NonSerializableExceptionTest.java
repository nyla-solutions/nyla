package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NonSerializableExceptionTest
{
    private Throwable expectedClass = new IllegalArgumentException("hello");
    private String message = "hello";

    @Test
    void constructors()
    {
        assertDoesNotThrow( () -> new NonSerializableException());
    }
    @Test
    void constructors_message()
    {

        NonSerializableException subject = new NonSerializableException(message);
        assertEquals(message,subject.getMessage());
    }

    @Test
    void constructor_message_cause()
    {
        NonSerializableException subject = new NonSerializableException(message,expectedClass);
        assertEquals(expectedClass,subject.getCause());
        assertEquals(message,subject.getMessage());
    }


    @Test
    void constructor_cause()
    {
        NonSerializableException subject = new NonSerializableException(expectedClass);
        assertEquals(expectedClass,subject.getCause());
    }

}