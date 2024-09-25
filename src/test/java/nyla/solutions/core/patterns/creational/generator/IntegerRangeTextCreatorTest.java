package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerRangeTextCreatorTest
{

    private int min = 2;
    private int max = 45;

    @Test
    void range() {
        var subject = IntegerRangeTextCreator.range(min,max);
        assertThat(subject).isNotNull();

        assertThat(Integer.parseInt(subject.create())).isBetween(min,max);
    }

    @Test
    void setMinAndMaxWithConstructor() {
        var subject = new IntegerRangeTextCreator(min,max);

        assertThat(Integer.parseInt(subject.create())).isBetween(min,max);
    }

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