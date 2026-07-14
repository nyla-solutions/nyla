package nyla.solutions.core.io.csv;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing the CsvReaderIterable class
 * @author Gregory Green
 */
class CsvStreamingReaderTest {

    @Test
    void iterator() {

        var csv = """
                "A", "A record"
                "B", "B record"
                """;

        var reader = new StringReader(csv);
        CsvStreamingReader subject =
                new CsvStreamingReader.Builder()
                        .reader(reader)
                        .build();

        int count = 0;
        for (var row : subject) {
            System.out.println(row);
            count++;
        }

        assertThat(count).isEqualTo(2);
    }


    @Test
    void emptyFileDoesNotHaveNext() throws FileNotFoundException {
        var filePath = IO.tempDir() + "/test.csv";
        var csv = """
                """;

        IO.writer().writeFile(filePath, csv);

        var subject = new CsvStreamingReader.Builder()
                .reader(new FileReader(Paths.get(filePath).toFile()))
                .build();

        assertThat(subject.hasNext()).isEqualTo(false);
    }


    @Test
    void singleLineFileHasARecord() throws FileNotFoundException {
        var filePath = IO.tempDir() + "/test.csv";
        var csv = """
                "A", "A record"
                """;

        IO.writer().writeFile(filePath, csv);

        var subject = new CsvStreamingReader.Builder()
                .reader(new FileReader(Paths.get(filePath).toFile()))
                .build();

        for(var row : subject) {
            assertThat(row).containsExactly("A", "A record");
        }
    }

    @Test
    void skipHeaderRecord() throws FileNotFoundException {
        var filePath = IO.tempDir() + "/test.csv";
        var csv = """
                "id", "name"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                "A", "A record"
                "B", "B record"
                """;

        IO.writer().writeFile(filePath, csv);

        var subject = new CsvStreamingReader.Builder()
                .reader(new FileReader(Paths.get(filePath).toFile()))
                .skipHeader(true)
                .build();

        int count = 0;
        for(var row : subject) {
            System.out.println(row);
            count++;
        }

        assertThat(count).isEqualTo(16);
    }
}
