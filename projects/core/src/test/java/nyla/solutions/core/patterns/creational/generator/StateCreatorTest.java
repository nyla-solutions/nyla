package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static nyla.solutions.core.util.Debugger.println;
import static org.assertj.core.api.Assertions.assertThat;

class StateCreatorTest {

    private final StateCreator subject = new StateCreator();

    @Test
    void create() {
        var actual = subject.create();
        println(actual);
        assertThat(actual).isNotEmpty();
    }
}