package nyla.solutions.core.data.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CapacityListTest {

    @Test
    void addBeyondMax() {
        var subject = new CapacityList<String>(2);

        assertTrue(subject.add("1"));
        assertThat(subject).contains("1");

        assertTrue(subject.add("2"));
        assertThat(subject).contains("2");

        assertTrue(subject.add("3"));
        assertThat(subject).contains("3");
        assertThat(subject).doesNotContain("2");
        assertThat(subject).hasSize(2);

    }


    @Test
    void addAll() {

        var subject = new CapacityList<String>(2);
        assertThat(subject.addAll(List.of("1","2","3","4"))).isTrue();

        assertThat(subject).hasSize(2);

    }

    @Test
    void addIndexCollection() {
        var subject = new CapacityList<String>(2);
        subject.addAll(0,List.of("1","2","3","4"));
        assertThat(subject).hasSize(2);
    }
}