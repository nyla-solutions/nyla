package nyla.solutions.core.patterns.workthread;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemorizedQueueTest {

    @Test
    void add() {
        Runnable run1 = () -> {
            System.out.println("run1");
        };

        Runnable run2 = () -> {
            System.out.println("run2");
        };

        MemorizedQueue subject = new MemorizedQueue(run1, run2);

        assertEquals(run1, subject.nextTask());
        assertEquals(run2, subject.nextTask());

        subject.add(run1);

        assertEquals(run1, subject.nextTask());
    }

    @Test
    void nextTask() {
    }

    @Test
    void hasMoreTasks() {
    }
}