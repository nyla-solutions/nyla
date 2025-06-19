package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static nyla.solutions.core.util.Debugger.println;
import static org.assertj.core.api.Assertions.assertThat;

class ZipCreatorTest {

    private final ZipCreator subject = new ZipCreator();

    @Test
    void create() {
        var actual = subject.create();
        println(actual);
        assertThat(actual).containsPattern("55...");
    }
}