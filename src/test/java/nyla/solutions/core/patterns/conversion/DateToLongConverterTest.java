package nyla.solutions.core.patterns.conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DateToLongConverterTest {

    private DateToLongConverter subject;

    @BeforeEach
    void setUp() {
        subject = new DateToLongConverter();
    }

    @Test
    @DisplayName("Given null data When convert Then Return 0")
    void convert_zero() {
        assertThat(subject.convert(null)).isEqualTo(0);
    }

    @Test
    @DisplayName("Given null data When convert Then Return 0")
    void convert() {
        var date = new Date();

        assertThat(subject.convert(date)).isEqualTo(date.getTime());
    }
}