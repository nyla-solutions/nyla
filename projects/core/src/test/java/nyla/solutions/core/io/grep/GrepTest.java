package nyla.solutions.core.io.grep;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GrepTest {

    private File file = Paths.get("src/test/resources/directories").toFile();
    private Grep subject;


    @BeforeEach
    void setUp() throws IOException {
        subject = Grep.file(file);
        assertNotNull(subject);
    }

    @Test
    void createWithStringDirect() throws IOException {
        assertNotNull(Grep.file(file.getPath()));
    }

    @Test
    void searchToFile() throws IOException {

        var directory = IO.tempDir()+"/searchToFileDir";
        IO.dir().mkdir(directory);
        var newFile = Paths.get(directory+"/newFile.txt").toFile();
        newFile.delete();
        var finalFile = Paths.get(directory+"/final.txt").toFile();
        finalFile.delete();

        var fileText  = """
                2025/05/11 sever error 1
                2025/05/11 sever error 2
                """;
        IO.writer().writeFile(newFile,fileText);

        Grep actual = subject.searchToFile(line ->
                line.contains("2025/05/11"),newFile)
                .searchToFile( line -> line.contains("sever"),finalFile);

        assertThat(actual.searchFirst(line -> line.contains("sever"))).isNotNull();

        var finalFileTextOutput = IO.reader().readTextFile(finalFile.toPath());

        System.out.println("FINAL OUTPUT:\n"+finalFileTextOutput);

        assertThat(finalFileTextOutput).contains("\n");
    }

    @Test
    void searchFirst() throws IOException {

        var actual = subject.searchFirst(
                line ->
                        line.contains("2025/05/11") &&
                                line.contains("sever")
                && !line.contains("Failed to publish 26 metrics")
                && !line.contains("05:00:07.754")
                && !line.contains("1440474")
                && !line.contains("SocketTimeoutException")
                && !line.contains("finalization")
                && !line.contains("tid=0x1")
                && !line.contains("GatewaySender_AsyncEventQueue")
         );


        System.out.println("ACTUAL="+actual);
        assertThat(actual).isNotNull();

        assertThat(actual.file()).isNotNull();
        assertThat(actual.results()).isNotNull();
    }


    @Test
    void searchFirstN() throws IOException {

        var actual = subject.searchFirstN(
                line ->
                        ( line.contains("2025/05/11") &&
                                (line.contains("2025/05/11 03:") || line.contains("2025/05/11 04:")) &&
                !line.contains("1440474") &&
                                line.toLowerCase().contains("shutdown") &&
                                !line.contains("Possible re-authentication") &&
                                !line.contains("-ssqKey") &&
                                !line.contains("Authenticated") &&
        !line.contains("Authenticated")
                        )
        , Integer.MAX_VALUE);


        System.out.println("ACTUAL="+ Debugger.toPrettyPrint(actual));
        assertThat(actual).isNotNull();

    }
}