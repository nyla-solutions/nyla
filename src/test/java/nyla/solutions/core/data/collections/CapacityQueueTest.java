package nyla.solutions.core.data.collections;

import nyla.solutions.core.exception.CapacityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapacityQueueTest {

    private CapacityQueue subject;
    private Queue<String> queue = new LinkedList();
    private int length = 2;

    @BeforeEach
    void setUp() {
        subject = new CapacityQueue(queue,length);
    }

    @Test
    void add() {
        subject.add("1");
        subject.add("2");

        assertThat(subject.poll()).isEqualTo("1");
        assertThat(subject.remove()).isEqualTo("2");
    }

    @Test
    void fixed() {
        subject.add("1");
        subject.add("2");

        assertThrows(CapacityException.class, () -> subject.add("3"));

        assertThat(subject.size()).isEqualTo(length);
    }
}