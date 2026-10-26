package nyla.solutions.core.util.text;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextFormatterTest {

    private final TextFormatter subject = new TextFormatter();

    @Test
    void camelCase() {

        var actual = subject.toCamelCase("Gene Newson");

        assertThat(actual).isEqualTo("geneNewson");
    }

    @Test
    void camelCaseWithLowerCases() {

        var actual = subject.toCamelCase("Gene newson");

        assertThat(actual).isEqualTo("geneNewson");
    }

}