package nyla.solutions.core.data;

import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataRowTest {

    private DataRow subject;

    @BeforeEach
    void setUp() {
        subject = new DataRow();
    }

    @Test
    void add() {
        Object expected = "add";
        subject.add(expected);

        assertEquals(expected,subject.retrieveString(1));
    }


    @Test
    void retrieveObject() {
        Object expected = "add";
        subject.add(expected);

        assertEquals(expected,subject.retrieveObject(1));
    }

    @Test
    void given_key_when_assignObject_then_assigned() {
        String key = "key";

        Serializable expected = "value";
        subject.assignObject(key,expected);
        String actual = subject.retrieveString(key);

        assertEquals(expected, actual);
    }


    @Test
    void retrieveDouble() {
        String key = "key";

        Double expected = 3.0;
        subject.assignObject(key,expected);
        Double actual = subject.retrieveDouble(key);

        assertEquals(expected, actual);
    }

    @Test
    void retrieveInteger() {

        String key = "key";

        Integer expected = 3;
        subject.assignObject(key,expected);
        Integer actual = subject.retrieveInteger(key);

        assertEquals(expected, actual);
    }

    @Test
    void retrieveDate() {

        String key = "key";

        Date expected = new Date();
        subject.assignObject(key,expected);
        Date actual = subject.retrieveDate(key);

        assertEquals(expected, actual);
    }

    @Test
    void getStrings() {
        String[] expected = {"1","2"};
        subject.add(expected[0]);
        subject.add(expected[1]);

        var actual = subject.getStrings();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void size() {
        String[] expected = {"1","2"};
        subject.add(expected[0]);
        subject.add(expected[1]);

        assertEquals(expected.length, subject.size());
    }

    @Test
    void getRowNum() {
        int expected = 3;
        subject.setRowNum(expected);
        assertEquals(expected, subject.getRowNum());
    }


    @Test
    void toArray() {
        Object[] expected = {"1","2"};
        subject.add(expected[0]);
        subject.add(expected[1]);

        var actual = subject.toArray();
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void getMap() {

        String key = "key";
        Serializable value = "value";
        Map<String, Serializable> expected = Organizer.change().toMap(key,value);
        subject.setMap(expected);
        Map<String, Serializable> actual = subject.getMap();
        assertEquals(expected, actual);
    }

}