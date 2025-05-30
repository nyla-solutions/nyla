package nyla.solutions.core.patterns.conversion.numbers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextToDoubleTest {

    private TextToDouble subject;
    private Double defaultValue = Double.valueOf(-1.0);

    @BeforeEach
    void setUp() {
        subject = new TextToDouble(defaultValue);
    }

    @Test
    void convert() {
        assertEquals(2.2, subject.convert("2.2"));
    }

    @Test
    void convert_null() {
        assertEquals(-1.0, subject.convert(null));
    }

    @Test
    void convert_empty() {
        assertEquals(-1.0, subject.convert(""));
    }

    @Test
    void fromObject() {
        assertEquals(2.45, TextToDouble.fromObject(new StringBuilder("2.45"), Double.valueOf(0)));

    }
    @Test
    void fromObject_null() {
        assertEquals(-1.0, TextToDouble.fromObject(null, Double.valueOf(-1.0)));

    }

    @Test
    void fromObject_empty() {
        assertEquals(-1.0, TextToDouble.fromObject(new StringBuilder(""),Double.valueOf(-1.0)));

    }
}