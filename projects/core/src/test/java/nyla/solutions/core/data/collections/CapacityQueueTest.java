package nyla.solutions.core.data.collections;

import nyla.solutions.core.exception.CapacityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.NoSuchElementException;
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
    void offer() {
        assertThat(subject.offer("1")).isTrue();
        assertThat(subject.offer("1")).isTrue();
        assertThat(subject.offer("1")).isFalse();
    }

    @Test
    void remove() {
        subject.add("1");
        subject.add("2");

        assertThat(subject.remove()).isEqualTo("1");
        assertThat(subject.remove()).isEqualTo("2");

    }

    @Test
    void poll() {
        subject.add("1");
        subject.add("2");

        assertThat(subject.poll()).isEqualTo("1");
        assertThat(subject.poll()).isEqualTo("2");

    }

    @Test
    void element() {
        assertThrows(NoSuchElementException.class,
                ()-> subject.element());
    }
    @Test
    void peek() {
        assertThat(subject.peek()).isNull();
    }

    @Test
    void size() {
        subject.add("1");
        subject.add("2");

        assertThat(subject.size()).isEqualTo(2);
    }

    @Test
    void isEmpty() {
        assertThat(subject.isEmpty()).isTrue();
    }

    @Test
    void contains() {
        assertThat(subject.contains("1")).isFalse();
    }

    @Test
    void iterator() {
        subject.add("1");
        assertThat(subject.iterator()).isNotNull();
    }

    @Test
    void toArray() {
        subject.add("1");
        assertThat(subject.toArray()).isNotNull();
    }

    @Test
    void fixed() {
        subject.add("1");
        subject.add("2");

        assertThrows(CapacityException.class, () -> subject.add("3"));

        assertThat(subject.size()).isEqualTo(length);
    }
}