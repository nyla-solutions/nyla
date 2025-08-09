package nyla.solutions.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class SynchronizedIOTest {

    private SynchronizedIO subject;
    private String fileDir = "runtime/tmp/SynchronizedIOTest";

    @BeforeEach
    void setUp() {
        Paths.get(fileDir).toFile().mkdirs();

        subject = SynchronizedIO.getInstance();
    }

    @Test
    void write() throws IOException {
        var expectedText = "Text".getBytes(StandardCharsets.UTF_8);
        var inputStream = new ByteArrayInputStream(expectedText);

        var filePath = fileDir + "/write.txt";
        IO.delete(Path.of(filePath).toFile());
        assertThat(IO.exists(filePath)).isFalse();

        subject.write(filePath,inputStream);

        assertThat(IO.exists(filePath)).isTrue();

        var actual = subject.readFile(filePath);
        assertThat(expectedText).isEqualTo(expectedText);
    }



    @Test
    void writeAppend() throws IOException {
        var expectedText = "Text".getBytes(StandardCharsets.UTF_8);
        var input = "Append Text";

        var filePath = fileDir + "/writeAppend.txt";
        IO.delete(Path.of(filePath).toFile());
        assertThat(IO.exists(filePath)).isFalse();

        subject.writeAppend(filePath,input);

        assertThat(IO.exists(filePath)).isTrue();

        var actual = subject.readFile(filePath);
        assertThat(expectedText).isEqualTo(expectedText);
    }
}