package nyla.solutions.core.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class IoDirTest {

    private IoDir subject;

    @BeforeEach
    void setUp() {
        subject = new IoDir();
    }

    @Test
    void listDirectories() throws IOException {

        var directory = Paths.get("src/test/resources/directories").toFile();

        var files = subject.listFilesOnly(directory);

        System.out.println("files:"+files);

        assertThat(files).isNotNull();

        assertThat(files.size()).isEqualTo(2);
    }

    @Test
    void listDirectoriesWithString() throws IOException {
        var directory = "src/test/resources/directories";

        var files = subject.listFilesOnly(directory);

        System.out.println("files:"+files);

        assertThat(files).isNotNull();

        assertThat(files.size()).isEqualTo(2);
    }
}