package nyla.solutions.core.patterns.conversion;

import nyla.solutions.core.patterns.conversion.numbers.NumberToStringConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberToStringConverterTest {
    private Long myLong = 232L;
    private Long myLongNull = null;
    private Double myDouble = 2.3;

    @Test
    void fromConvert() {
        assertEquals("232", NumberToStringConverter.from(myLong));
    }


    @Test
    void convert() {
        assertEquals("2.3",new NumberToStringConverter(Double.class).convert(myDouble));
    }

    @Test
    void fromConvertNull() {
        assertEquals("", NumberToStringConverter.from(myLongNull));
    }


}