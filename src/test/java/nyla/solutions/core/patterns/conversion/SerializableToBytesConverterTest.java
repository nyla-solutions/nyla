package nyla.solutions.core.patterns.conversion;

import nyla.solutions.core.patterns.conversion.io.SerializableToBytesConverter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing for ObjectToBytesSerializerConverter
 * @author gregory green
 */
class SerializableToBytesConverterTest {

    private UserProfile expected = JavaBeanGeneratorCreator.of(UserProfile.class).create();
    private SerializableToBytesConverter<UserProfile> subject;

    @BeforeEach
    void setUp() {
        subject = new SerializableToBytesConverter<UserProfile>();
    }

    @Test
    void convert() {
        var actual = subject.convert(expected);

        assertNotNull(actual);
        assertTrue(actual.length > 0);
    }

    @Test
    void given_byteSize_when_setSize_then_getSize_isExpected() {
        int expected = 100;
        subject.setByteSize(expected);

        assertEquals(expected, subject.getByteSize());
    }
}