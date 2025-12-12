package nyla.solutions.core.patterns.iteration;

import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IterateIteratorTest
{

    @Test
    void next_WhenNull_ReturnsNull()
    {
        IterateIterator<String> subject = new IterateIterator<>(null);

        assertNull(subject.next());
    }

    @Test
    void next_WhenEmpty_ReturnsNull()
    {
        IterateIterator<String> subject = new IterateIterator<>(new ArrayList<String>().iterator());

        assertNull(subject.next());
    }

    @Test
    void next_WhenNot_Returnsl()
    {
        String expected = "hi";
        IterateIterator<String> subject = new IterateIterator<String>(Organizer.change().toList(expected).iterator());

        assertEquals(expected, subject.next());
        assertNull(subject.next());
    }
}