package nyla.solutions.core.io.csv.transformer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CsvToMapConverterTest {

    @DisplayName("Given CSV with headers when convert ")
    @Test
    void convert() {
        var subject = new CsvToMapConverter("firstName","lastName");

        String csv = """
                "John","Doe"
                """;
        var actual = subject.convert(csv);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get("firstName")).isEqualTo("John");
        assertThat(actual.get("lastName")).isEqualTo("Doe");
    }


    @DisplayName("Given CSV with more headers when values when convert then no exceptions")
    @Test
    void convert_with_more_headers() {
        var subject = new CsvToMapConverter("address","zip","fn","ln");

        String csv = """
                "123 Street","00023"
                """;
        var actual = subject.convert(csv);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get("address")).isEqualTo("123 Street");
        assertThat(actual.get("zip")).isEqualTo("00023");
    }

    @DisplayName("Given CSV with more values when values when convert then no exceptions")
    @Test
    void convert_with_more_values() {
        var subject = new CsvToMapConverter("address");

        String csv = """
                "ABC Street","00023","hello"
                """;
        var actual = subject.convert(csv);

        assertThat(actual).isNotEmpty();
        assertThat(actual.get("hello")).isNull();
        assertThat(actual.get("address")).isEqualTo("ABC Street");
    }

    @DisplayName("Given CSV with empty headers when values when convert then no exceptions")
    @Test
    void convert_with_empty_headers() {
        var subject = new CsvToMapConverter();

        String csv = """
                "ABC Street","00023","hello"
                """;
        var actual = subject.convert(csv);

        assertThat(actual).isEmpty();
    }

    @DisplayName("Given CSV with empty values when values when convert then no exceptions")
    @Test
    void convert_with_empty_values() {
        var subject = new CsvToMapConverter();

        String csv = "";
        var actual = subject.convert(csv);
        assertThat(actual).isNull();
    }

    @Test
    void convert_with_null_values() {
        var subject = new CsvToMapConverter();

        var actual = subject.convert(null);
        assertThat(actual).isNull();
    }
}