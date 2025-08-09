package nyla.solutions.core.patterns.conversion.numbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test for StringToLong
 */
class TextToLongTest {

    private long expectedLong = 22L;
    private String longText = Long.valueOf(expectedLong).toString();

    @DisplayName("Given Long When From Then Return Long Value")
    @Test
    void convertFrom() {
        assertEquals(expectedLong, TextToLong.from(longText));
    }

    @DisplayName("Given Object When fromObject with Default Return Long Vection")
    @Test
    void fromObjectWithDefault() {
        assertEquals(expectedLong, TextToLong.fromObject(new StringBuilder(String.valueOf(expectedLong))));
    }


    @DisplayName("Given Object When fromObject with Default Return Long Vection")
    @Test
    void fromObject() {
        assertEquals(expectedLong, TextToLong.fromObject(new StringBuilder(String.valueOf(expectedLong)),3L));
    }

    @DisplayName("Given Null Object When fromObject with Default Return Long Vection")
    @Test
    void fromNullObjectWithDefault() {
        assertEquals(3L, TextToLong.fromObject(null,3L));
    }


    @DisplayName("Given Null When From Then Return Null")
    @Test
    void convertFromNull() {
        assertNull(TextToLong.from(null));
    }

    @DisplayName("Given Empty String When From Then Return Null")
    @Test
    void convertFromEmpty() {
        assertNull(TextToLong.from(""));
    }

    @DisplayName("Given Empty String with Default Value When From Then Return Default Value")
    @Test
    void convertFromEmptyDefault() {
        assertEquals(0L, TextToLong.from("",0L));
    }

    @DisplayName("Given Null String with Default Value When From Then Return Default Value")
    @Test
    void convertFromNullDefault() {
        assertEquals(0L, TextToLong.from(null,0L));
    }

    @DisplayName("Given long text when convert Then return long converted")
    @Test
    void convert() {
        assertEquals(expectedLong,new TextToLong().convert(longText));
    }
}