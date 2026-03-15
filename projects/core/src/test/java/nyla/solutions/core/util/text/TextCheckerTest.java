package nyla.solutions.core.util.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextCheckerTest {

    private TextChecker subject;

    @BeforeEach
    void setUp() {
        subject = new TextChecker();
    }

    @Test
    void isInteger_null_empty_whitespace_and_valid_cases() {
        assertFalse(subject.isInteger(null), "null should not be considered an integer");
        assertFalse(subject.isInteger(""), "empty string should not be considered an integer");
        assertFalse(subject.isInteger("   "), "whitespace only should not be considered an integer");

        // plain digits
        assertTrue(subject.isInteger("123"));

        // trimmed digits
        assertTrue(subject.isInteger("  456  "));

        // negative sign is not allowed by implementation
        assertFalse(subject.isInteger("-789"));

        // non-digit characters
        assertFalse(subject.isInteger("12a3"));
        assertFalse(subject.isInteger("1.23"));
    }

    @Test
    void isNull_various_cases() {
        assertTrue(subject.isNull(null));
        assertTrue(subject.isNull(""));
        assertTrue(subject.isNull("   \t  \n"));

        // literal string "null" (case-sensitive) should be considered null
        assertTrue(subject.isNull("null"));
        assertTrue(subject.isNull("  null  "));

        // different case is NOT considered null
        assertFalse(subject.isNull("Null"));
        assertFalse(subject.isNull("value"));
    }

    @Test
    void isNumber_with_numbers_instances_and_parseable_strings() {
        // Number instances should return true
        assertTrue(subject.isNumber(123));
        assertTrue(subject.isNumber(45.67));
        assertTrue(subject.isNumber(Integer.valueOf(0)));
        assertTrue(subject.isNumber(Double.NaN)); // instanceof Number -> true

        // null and empty/whitespace strings
        assertFalse(subject.isNumber(null));
        assertFalse(subject.isNumber(""));
        assertFalse(subject.isNumber("   \t  \n"));

        // parseable numeric strings
        assertTrue(subject.isNumber("42"));
        assertTrue(subject.isNumber("  -3.14  "));
        assertTrue(subject.isNumber("1e3"));
        assertTrue(subject.isNumber("+2.5"));

        // custom object whose toString() returns a parseable number
        Object parseable = new Object() {
            @Override
            public String toString() {
                return "7";
            }
        };
        assertTrue(subject.isNumber(parseable));

        // non-parseable strings
        assertFalse(subject.isNumber("not a number"));
        assertFalse(subject.isNumber("1.2.3"));
    }
}