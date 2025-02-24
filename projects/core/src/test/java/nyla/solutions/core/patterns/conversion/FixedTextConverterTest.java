package nyla.solutions.core.patterns.conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedTextConverterTest
{
    FixedTextConverter fixedTextConverter;

    @BeforeEach
    void setUp()
    {
       fixedTextConverter = new FixedTextConverter();
    }

    @Test
    void toText()
    {
        String expected ="sdsds";
        fixedTextConverter.setFixedText(expected);

        assertEquals(expected,fixedTextConverter.toText("any"));

    }

}