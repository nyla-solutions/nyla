package nyla.solutions.core.data.collections;

import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class QueueSupplierTest
{

    private QueueSupplier<String> subject;
    private Queue<String> queue;

    @BeforeEach
    void setUp()
    {
        queue = mock(Queue.class);
        subject = new QueueSupplier<String>(queue);
    }

    @Test
    void whenQueueNull_ThenThrowExpected()
    {
        assertThrows(NullPointerException.class, ()-> new QueueSupplier<String>(null));
    }

    @Test
    void whenGivenQueue_Then_SupplierWorks()
    {
        subject = new QueueSupplier<>();
        String expected = "hi";
        subject.add(expected);
        when(queue.poll()).thenReturn(expected);
        String actual = subject.get();

        assertEquals(expected,actual);
    }

    @Test
    void supplier()
    {
        String expected = "hi";
        when(queue.poll()).thenReturn(expected);
        String actual = subject.get();

        assertEquals(expected,actual);
    }

    @Test
    void add()
    {
        subject.add("hi");
        verify(queue).add(anyString());
    }

    @Test
    void testEquals()
    {

        QueueSupplier<String> subject1 = new QueueSupplier<>(Organizer.arrange("hi").toQueue());
        QueueSupplier<String> subject2 = new QueueSupplier<>(Organizer.arrange("hi").toQueue());
        assertTrue(subject1.equals(subject1));

        assertFalse(subject.equals(subject2));
    }


    @Test
    void offer()
    {
        subject.offer("hi");
        verify(queue).offer(anyString());
    }

    @Test
    void remove()
    {
        subject.remove("hi");
        verify(queue).remove(anyString());
    }

    @Test
    void removeAll()
    {
        subject.removeAll(Collections.emptyList());
        verify(queue).removeAll(any());
    }

    @Test
    void retainAll()
    {
        subject.retainAll(Collections.emptyList());
        verify(queue).retainAll(any());
    }

    @Test
    void toArray()
    {
        subject.toArray();
        verify(queue).toArray();
    }

    @Test
    void parallelStream()
    {
        subject.parallelStream();
        verify(queue).parallelStream();
    }
    @Test
    void size()
    {
        subject.size();
        verify(queue).size();
    }

    @Test
    void spliterator()
    {
        subject.spliterator();
        verify(queue).spliterator();
    }

    @Test
    void iterator()
    {
        subject.iterator();
        verify(queue).iterator();
    }

    @Test
    void forEach()
    {
        Consumer<? super String> consumer = mock(Consumer.class);
        subject.forEach(consumer);
        verify(queue).forEach(any());
    }

    @Test
    void stream()
    {
        subject.stream();
        verify(queue).stream();
    }


    @Test
    void addAll()
    {
        subject.addAll(new ArrayList<>());
        verify(queue).addAll(any());
    }

    @Test
    void clear()
    {
        subject.clear();
        verify(queue).clear();
    }
    @Test
    void contains()
    {
        subject.contains("");
        verify(queue).contains(any());
    }
    @Test
    void containsAll()
    {
        subject.containsAll(new ArrayList<>());
        verify(queue).containsAll(any());
    }


    @Test
    void element()
    {
        subject.element();
        verify(queue).element();
    }

    @Test
    void peek()
    {
        subject.peek();
        verify(queue).peek();
    }
    @Test
    void poll()
    {
        subject.poll();
        verify(queue).poll();
    }
}