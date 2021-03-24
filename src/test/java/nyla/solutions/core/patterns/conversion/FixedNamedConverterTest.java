package nyla.solutions.core.patterns.conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * Test for FixedNamedConverter
 * @author Gregory Green
 *
 */

class FixedNamedConverterTest
{
    @Mock
    private FixedNamedConverter expected;



    @BeforeEach
    public void setUp()
    {
        expected = new FixedNamedConverter();

    }

    @Test
    void convert()
    {

        expected.setTarget("output");
        assertEquals("output",expected.convert("output"));

    }

    @Test
    void getName()
    {
        expected.setName("test");
        assertEquals("test",expected.getName());


    }

    @Test
    void getTarget()
    {
        expected.setTarget("test");
        assertEquals("test",expected.getTarget());
    }
}