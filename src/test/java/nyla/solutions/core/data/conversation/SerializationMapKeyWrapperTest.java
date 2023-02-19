package nyla.solutions.core.data.conversation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializationMapKeyWrapperTest {

    private SerializationMapKeyWrapper subject;

    @BeforeEach
    void setUp() {
        subject = new SerializationMapKeyWrapper();
    }

    @Test
    void getKey() {
        String expected = "Key";
        subject.setKey(expected);
        Object actual = subject.getKey();
        assertEquals(expected, actual);
    }

    @Test
    void getKeyClassName() {

        String expected = "Key";
        subject.setKeyClassName(expected);
        String actual = subject.getKeyClassName();
        assertEquals(expected, actual);
    }
}