package nyla.solutions.core.io.grep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class GrepResultCsvDecoratorTest {

    private GrepResultCsvDecorator subject;
    private GrepResult result;
    private String text = "JUNIT";
    private File file = Paths.get("src/test/resources/iotest/emails/emails.json").toFile();

    @BeforeEach
    void setUp() {
        subject = new GrepResultCsvDecorator();

        result = new GrepResult(text,file);
    }

    @Test
    void decorator() {
        var actual = subject.decorate(result);
        System.out.println(actual);
        assertThat(actual).contains(text)
                .contains(file.getName());
    }
}