package nyla.solutions.core.io;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IoReaderTest {


    private final Path fileDirectory = Paths.get("runtime/tmp/io/");
    private final Path filePath = Paths.get(fileDirectory.toString(),"expect.txt");
    private final IoReader subject = new IoReader();

    @BeforeEach
    void setUp() {

    }

    @Test
    void readProperties() throws IOException {

        var properties = new Properties();
        properties.setProperty("key1","value1");
        properties.setProperty("key2","value2");

        var filePath = fileDirectory+"/test.properties";
        IO.mkdir(fileDirectory.toString());

        properties.store(new FileWriter(filePath),"test properties");

        var actual = subject.readProperties(filePath);
        assertThat(actual.get("key1")).isEqualTo("value1");
        assertThat(actual.get("key2")).isEqualTo("value2");

    }

    @Test
    void readFile() throws IOException {

        var expected = """
                Testing
                """;
        Files.writeString(filePath, expected);

        var actual = subject.readTextFile(filePath);
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void readFileWithTextPath() throws IOException {

        var expected = """
                Testing Writer
                """;
        Files.writeString(filePath, expected);

        var actual = subject.readTextFile(filePath.toString());
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    void readFileWithRetries() throws IOException {

        var expected = """
                Testing With Retries
                """;

        Files.writeString(filePath, expected);

       var actual = subject.readFile(filePath,2,100);

       assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readFile_null() throws IOException {

        assertNull(subject.readTextFile((String)null));
    }

    @Test
    void readFile_doesNotExist() throws IOException {

        assertThrows(NoSuchFileException.class, () -> subject.readTextFile(Paths.get("runtime", Text.generateId())));
    }

    @Test
    void readFile_director() throws IOException {

        IO.mkdir("runtime");
        assertThrows(IOException.class, () -> subject.readTextFile(Paths.get("runtime")));
    }

    @Test
    void readTextInputStream() throws IOException {

        var expected = "Input Stream Text Data";
        Files.writeString(this.filePath,expected);

        var actual = subject.readTextInputStream(Files.newInputStream(filePath));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readMap() throws IOException {

        var expected = Map.of("k1","v2");
        Properties properties = new Properties();
        properties.putAll(expected);

        properties.store(new FileWriter(filePath.toFile()),"test map");
        var actual = subject.readMap(filePath);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void readTextLines() throws IOException {

        var readTextLinesPath = Paths.get(fileDirectory.toString(),"readTextLines.txt");
        var expected = """ 
                Line 1
                Line 2
                Line3""";

        Files.writeString(readTextLinesPath,expected);
        var actual = subject.readTextLines(readTextLinesPath);

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(3);
    }

    @Test
    void readBinaryFile() throws IOException {

        var expected = "Binary Data 1234";
        Files.writeString(this.filePath,expected);

        var actual = subject.readBinaryFile(this.filePath,3,1);

        assertThat(new String(actual)).isEqualTo(expected);
    }
}