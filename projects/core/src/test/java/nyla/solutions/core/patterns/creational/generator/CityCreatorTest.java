package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static nyla.solutions.core.util.Debugger.println;
import static org.assertj.core.api.Assertions.assertThat;

class CityCreatorTest {

    private final CityCreator subject = new CityCreator();

    @Test
    void create() {
        var actual = subject.create();
        println(actual);

        assertThat(actual).isNotEmpty();

    }
}