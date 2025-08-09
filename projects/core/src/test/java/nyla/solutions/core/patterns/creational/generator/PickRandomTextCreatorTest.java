package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static nyla.solutions.core.util.Debugger.println;
import static org.assertj.core.api.Assertions.assertThat;

class PickRandomTextCreatorTest {

    @Test
    void create() {

        var subject = PickRandomTextCreator.options("A","B","C");
        assertThat(subject).isNotNull();
        String actual = subject.create();
        println(actual);
        assertThat(actual).containsAnyOf("A","B","C");
    }
}