package nyla.solutions.core.io.grep;

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

        var actual = grep.searchFirstN(
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