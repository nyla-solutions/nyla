package nyla.solutions.core.io.grep;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GrepTest {

    private File file = Paths.get("/Users/Projects/PlayGround/runtime/files").toFile();
    private Grep grep;


    @BeforeEach
    void setUp() throws IOException {
        grep = Grep.file(file);
        assertNotNull(grep);
    }



    @Test
    void searchFirst() throws IOException {

        var actual = grep.searchFirst(
                line ->
                        line.contains("2025/05/11") &&
                                line.contains("error")
                && !line.contains("Failed to publish 26 metrics")
                && !line.contains("05:00:07.754")
         );


        System.out.println("ACTUAL="+actual);
        assertThat(actual).isNotNull();

        assertThat(actual.file()).isNotNull();
        assertThat(actual.results()).isNotNull();
    }
}