package nyla.solutions.core.patterns.iteration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IteratorExceptionTest
{

    @Test
    void constructor()
    {
        String expected = "error";
        assertEquals(expected,new IteratorException(expected).getMessage());
    }

    @Test
    void constructor_defaultMsgBlank()
    {
        assertNull(new IteratorException().getMessage());
    }


}