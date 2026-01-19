package nyla.solutions.core.patterns.workthread;

import nyla.solutions.core.exception.RequiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemorizedQueueTest {

    private MemorizedQueue subject;

    Runnable run1 = () -> {
        System.out.println("run1");
    };

    Runnable run2 = () -> {
        System.out.println("run2");
    };


    @BeforeEach
    void setUp() {
        subject = new MemorizedQueue(run1, run2);
    }

    @Test
    void add() {

        assertEquals(run1, subject.nextTask());
        assertEquals(run2, subject.nextTask());

        subject.add(run1);

        assertEquals(run1, subject.nextTask());
    }

    @Test
    void nextTask() {

        assertThat(subject.nextTask()).isNotNull();
    }


    @Test
    void given_null_when_create_then_RequiredException() {
        assertThrows(RequiredException.class, () -> {
            new MemorizedQueue((Runnable[])null );
        });
    }

    @Test
    void emptyQueue() {
        subject = new MemorizedQueue(1);

        assertThat(subject.size()).isEqualTo(0);
    }

    @Test
    void hasMoreTasks() {
        assertThat(subject.hasMoreTasks()).isTrue();
    }

    @Test
    void size() {
        assertThat(subject.size()).isEqualTo(2);
    }

}