package nyla.solutions.core.io;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IoReaderTest {


    private Path fileDirectory = Paths.get("runtime/tmp/io/");
    private Path filePath = Paths.get(fileDirectory.toString(),"expect.txt");
    private IoReader subject = new IoReader();

    @BeforeEach
    void setUp() {

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
    void readFile_null() throws IOException {

        assertNull(subject.readTextFile(null));
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
}