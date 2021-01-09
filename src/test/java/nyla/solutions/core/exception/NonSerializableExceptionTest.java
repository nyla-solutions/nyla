package nyla.solutions.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonSerializableExceptionTest
{
    @Test
    void constructors_message()
    {
        String expected = "hello";
        NonSerializableException subject = new NonSerializableException(expected);
        assertEquals(expected,subject.getMessage());




    }

    @Test
    void constructor_cause()
    {
        Throwable expectedClass = new IllegalArgumentException("hello");
        NonSerializableException subject = new NonSerializableException("hello",expectedClass);
        assertEquals(expectedClass,subject.getCause());
    }


}