package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FullNameCreatorTest
{

    @Test
    void create()
    {
        FullNameCreator subject = new FullNameCreator();
        String actual = subject.create();
        assertNotNull(actual);
        assertThat(actual).doesNotContain("@");
        assertThat(actual).doesNotContain(subject.getClass().getSimpleName());
    }
}