package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerRangeTextCreatorTest
{

    @Test
    void given_configMinMax_when_create_thenText_BetweenMinAndMax()
    {
        String expected = "34";

        Properties properties = new Properties();
        properties.setProperty(IntegerRangeTextCreator.MIN_INT_TEXT_PROP,expected);
        properties.setProperty(IntegerRangeTextCreator.MAX_INT_TEXT_PROP,expected);
        Config.setProperties(properties);

        IntegerRangeTextCreator subject = new IntegerRangeTextCreator();
        String actual = subject.create();

        assertEquals(expected,actual);
    }
}