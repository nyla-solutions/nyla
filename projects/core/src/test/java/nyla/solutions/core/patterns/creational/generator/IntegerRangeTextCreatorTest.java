package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

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
        int expected = 34;


        IntegerRangeTextCreator subject = new IntegerRangeTextCreator(expected,expected);
        String actual = subject.create();

        assertEquals(String.valueOf(expected),actual);
    }
}