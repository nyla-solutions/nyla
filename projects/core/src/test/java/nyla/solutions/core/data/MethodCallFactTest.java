package nyla.solutions.core.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Gregory Green
 */
class MethodCallFactTest
{

    @Test
    void setArguments()
    {
        MethodCallFact subject = new MethodCallFact();
        String expected = "expected";
        subject.setArguments(expected);
        Object[] output = subject.getArguments();
        assertNotNull(output);
        assertEquals(1,output.length);
        assertEquals(output[0],expected);
    }
}