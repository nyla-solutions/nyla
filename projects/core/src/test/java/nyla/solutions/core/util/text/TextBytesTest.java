package nyla.solutions.core.util.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TextBytesTest {

    private TextBytes subject;

    @BeforeEach
    void setUp() {
        subject = new TextBytes();
    }

    @Test
    void toByteText_and_toBytesFromByteText() {
        String text = "Hello World";
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);

        String byteText = subject.toByteText(bytes);
        assertThat(byteText).isNotNull();

        byte[] resultBytes = subject.toBytesFromByteText(byteText);
        assertThat(resultBytes).isNotNull();
        assertThat(resultBytes).isEqualTo(bytes);
    }
}