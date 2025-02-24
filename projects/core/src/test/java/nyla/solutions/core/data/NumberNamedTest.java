package nyla.solutions.core.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberNamedTest {

    @Test
    void getNumber() {
        int expected = 3;
        var subject = new NumberNamed();
        subject.setNumber(expected);

        assertEquals(expected, subject.getNumber());
    }

    @Test
    void sortByNumber() {
    }

    @Test
    void compareTo() {

    }

    @Test
    void getKey() {

        int expected = 33;
        var subject = new NumberNamed();
        subject.setKey(expected);

        assertEquals(expected, subject.getKey());
    }

    @Test
    void getValue() {

        String expected = "name";
        var subject = new NumberNamed();
        subject.setName(expected);

        assertEquals(expected, subject.getValue());
    }


}