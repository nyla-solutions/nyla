package nyla.solutions.core.data.conversation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SerializationMapKeyWrapperTest {

    private SerializationMapKeyWrapper subject;
    private Object key =  Integer.valueOf(1);
    private String keyClassName = key.getClass().getName();

    @BeforeEach
    void setUp() {
        subject = new SerializationMapKeyWrapper();
    }

    @DisplayName("GIVEN key and keyClass when construct THEN key class matches")
    @Test
    void constructorKey_keyClassName() {

        var subject = new SerializationMapKeyWrapper(key,keyClassName);
        assertThat(subject.getKeyClassName()).isEqualTo(keyClassName);
        assertThat(subject.getKey()).isEqualTo(key);
    }

    @DisplayName("GIVEN key when construct THEN key class matches")
    @Test
    void constructorKey() {

        var subject = new SerializationMapKeyWrapper(key);
        assertThat(subject.getKeyClassName()).isEqualTo(keyClassName);
        assertThat(subject.getKey()).isEqualTo(key);
    }

    @Test
    void testEquals() {

        String testKey = "123";
        var subject1 = new SerializationMapKeyWrapper(testKey);
        var subject2 = new SerializationMapKeyWrapper(testKey);

        assertThat(subject1).isEqualTo(subject2);

    }

    @Test
    void testHashCode() {

        String testKey = "123";
        var subject1 = new SerializationMapKeyWrapper(testKey);
        var subject2 = new SerializationMapKeyWrapper(testKey);

        assertThat(subject1.hashCode()).isEqualTo(subject2.hashCode());

    }

    @Test
    void testToString_key() {

        String testKey = "123";
        var subject = new SerializationMapKeyWrapper(testKey);

        assertThat(subject.toString()).contains(testKey);

    }

    @Test
    void testToString_keyClass() {

        String testKey = "123";
        String testKeyClassName ="Hello";
        var subject = new SerializationMapKeyWrapper(testKey,testKeyClassName);

        assertThat(subject.toString()).contains(testKeyClassName);

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