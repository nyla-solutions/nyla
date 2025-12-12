package nyla.solutions.core.patterns.loadbalancer;

import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RoundRobinTest
{
    private RoundRobin<String> subject;

    @BeforeEach
    void setUp()
    {
        subject = new RoundRobin<>();
    }

    @Test
    void next_Returns_False()
    {
        assertNull(subject.next());
    }

    @Test
    void constructWithArrays() {
        var subject = new RoundRobin<String>("1","2");

        assertThat(subject.toCollection()).isNotEmpty().hasSize(2);
    }

    @Test
    void add_Then_Next_isEqual_To_added()
    {
        String expected = "expected";
        assertTrue(subject.add(expected));
        assertEquals(expected, subject.next());
        assertEquals(expected, subject.next());
    }
    @Test
    void add_null_Then_False()
    {
        assertFalse(subject.add(null));
    }
    @Test
    void addAll_collections_nextEquals()
    {
        String expected1 = "expected1";
        String expected2 = "expected2";
        Collection<String> items = Arrays.asList(expected1, expected2);
        assertTrue(subject.addAll(items));
        assertEquals(expected1, subject.next());
        assertEquals(expected2, subject.next());
        assertEquals(expected1, subject.next());
    }

    @Test
    void addAll_with_a_duplicate_collection_Returns_False()
    {
        String expected1 = "expected1";
        String expected2 = "expected2";
        Collection<String> items = Arrays.asList(expected1, expected2);
        assertTrue(subject.addAll(items));

        items = Arrays.asList(expected2,"expected 3");
        assertFalse(subject.addAll(items));
    }

    @Test
    void addAll_arrayItems_nextEquals()
    {
        String expected1 = "expected1";
        String expected2 = "expected2";
        assertTrue(subject.addAll(expected1,expected2));
        assertEquals(expected1, subject.next());
        assertEquals(expected2, subject.next());
        assertEquals(expected1, subject.next());
    }

    @Test
    void add_duplicate_Then_ReturnFalse()
    {
        assertTrue(subject.add("1"));
        assertFalse(subject.add("1"));
    }

    @Test
    void toCollection()
    {
        Collection<String> expected = Organizer.change().toSet("1","2");
        for (String i: expected) {
            subject.add(i);
        }

        assertEquals(expected,subject.toCollection());
    }

    @Test
    void remove_then_toCollectionNotFound()
    {
        String expected = "1";
        subject.add(expected);
        assertEquals(expected,subject.next());
        assertTrue(subject.toCollection().contains(expected));
        subject.remove(expected);
        assertFalse(subject.toCollection().contains(expected));
        assertNull(subject.next());
    }
}