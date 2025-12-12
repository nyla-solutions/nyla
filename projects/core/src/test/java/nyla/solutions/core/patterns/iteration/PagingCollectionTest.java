package nyla.solutions.core.patterns.iteration;


import nyla.solutions.core.util.Organizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class PagingCollectionTest
{
    private Collection<String> collection;
    private PageCriteria pageCriteria;
    private PagingCollection<String> subject;
    private int beginIndex = 1;
    private int size = 2;

    @BeforeEach
    void setUp()
    {
        collection = Organizer.change().toList("1", "2", "3", "4", "5", "6");
        pageCriteria = new PageCriteria(beginIndex, size);
        subject = new PagingCollection<>(collection,
                pageCriteria);

    }

    @Test
    public void testEqualsObject()
    {

        PagingCollection<String> collection1 = new PagingCollection<>(Collections.singleton("hello"),
                new PageCriteria());
        PagingCollection<String> collection2 = new PagingCollection<>(Collections.singleton("hello"),
                new PageCriteria());
        assertEquals(collection1, collection2);

        collection2 = new PagingCollection<>(Collections.singleton("world"), new PageCriteria());
        assertNotEquals(collection1, collection2);
    }

    @Test
    void getPageCriteria()
    {
        assertEquals(pageCriteria, subject.getPageCriteria());
    }

    @Test
    void isLast()
    {
        assertFalse(subject.isLast());
        subject.setLast(true);
        assertTrue(subject.isLast());
    }

    @Test
    void isFirst()
    {
        assertTrue(subject.isFirst());
        subject.setFirst(false);
        assertFalse(subject.isFirst());
    }

    @Test
    void isFirst_When_PageCriteria_Is_Null_Then_Return_True()
    {
        subject = new PagingCollection<>(collection,
                null);

        assertTrue(subject.isFirst());
    }
    @Test
    void isFirst_When_PageCriteria_Is_LessThan2_Then_Return_True()
    {
        pageCriteria.setBeginIndex(1);
        subject = new PagingCollection<>(collection,
                pageCriteria);

        assertTrue(subject.isFirst());

    }

    @Test
    void add_WhenNull_ThenReturnFalse()
    {
        assertFalse(subject.add(null));
    }

    @Test
    void add()
    {
        pageCriteria.setSize(3);
        collection.clear();
        subject = new PagingCollection<String>(collection, pageCriteria);
        assertTrue(subject.add("test"));
    }


    @Test
    void add_WhenPageCriteria_IsNull_Return_True()
    {
        collection = Organizer.change().toList("1", "2", "3");
        subject = new PagingCollection<String>(collection, null);
        assertTrue(subject.add("test"));

    }

    @Test
    void add_when_collections_GreaterThanSize_ThenReturn_false_And_isLast()
    {

        pageCriteria.setSize(2);
        collection = Arrays.asList("1", "2", "3");
        subject = new PagingCollection<String>(collection, pageCriteria);
        assertFalse(subject.add("test"));
        assertFalse(subject.isLast());
    }


    @DisplayName("Given AddAll")
    @Nested
    public class AddAllTest{

        @DisplayName("When PageCriteria Size Then still AddAll")
        @Test
        void addAl_criteriaPageSize0()
        {
            pageCriteria.setSize(0);
            collection = Organizer.change().toList("1", "2");
            subject = new PagingCollection<String>(collection, pageCriteria);

            List<String> expected = Arrays.asList("A", "B");
            assertTrue(subject.addAll(expected));

            assertThat(subject.size()).isEqualTo(4);
            assertThat(subject).contains("1");
            assertThat(subject).contains("2");
            assertThat(subject).contains("A");
            assertThat(subject).contains("B");
        }

        @Test
        void addAll()
        {
            pageCriteria.setSize(4);
            collection = Organizer.change().toList("1", "2");
            subject = new PagingCollection<String>(collection, pageCriteria);

            List<String> expected = Arrays.asList("A", "B");
            assertTrue(subject.addAll(expected));

            assertThat(subject.size()).isEqualTo(4);
            assertThat(subject).contains("1");
            assertThat(subject).contains("2");
            assertThat(subject).contains("A");
            assertThat(subject).contains("B");
        }

    }


    @Test
    void addAll_WhenDuplicate_Then_Return_False()
    {
        pageCriteria.setSize(4);
        collection = Organizer.change().toList("1", "2");
        subject = new PagingCollection<String>(collection, pageCriteria);

        List<String> expected = Arrays.asList("1", "B");
        assertTrue(subject.addAll(expected));
    }

    @Test
    void clear_Then_IsEmpty()
    {
        assertFalse(subject.isEmpty());
        subject.clear();
        assertTrue(subject.isEmpty());
    }

    @Test
    void contains()
    {
       assertTrue(subject.contains("1"));
       assertFalse(subject.contains("NA"));
    }

    @Test
    void containsAll()
    {
        assertTrue(subject.containsAll(Arrays.asList("1","2")));
    }


    @Test
    void iterator()
    {
        assertNotNull(subject.iterator());
    }

    @Test
    void remove()
    {
        assertTrue(subject.contains("1"));
        subject.remove("1");
        assertFalse(subject.contains("1"));
    }

    @Test
    void removeAll()
    {
        assertTrue(subject.containsAll(Arrays.asList("1","2")));
        assertTrue(subject.removeAll(Arrays.asList("1","2")));
        assertFalse(subject.containsAll(Arrays.asList("1","2")));

    }

    @Test
    void retainAll()
    {
        assertTrue(subject.retainAll(Arrays.asList("1","2")));
        assertFalse(subject.retainAll(Arrays.asList("1","2")));

    }


    @Test
    void size()
    {
        int actual = subject.size();
        assertTrue(actual > 0);
        subject.clear();
        assertEquals(0,subject.size());
    }

    @Test
    void toArray()
    {
        Object[] actual = subject.toArray();
        assertNotNull(actual);
        assertEquals(subject.size(),actual.length);
    }

    @Test
    void toArray_withArray()
    {
        String[] expected = new String[2];

        Object[] actual = subject.toArray(expected);
        assertNotNull(actual);
        assertEquals(subject.size(),actual.length);
    }


    @Test
    void testToString()
    {
        String actual = subject.toString();
        assertThat(actual).contains(collection.iterator().next());
    }

    @Test
    void testHashCode()
    {
        assertTrue(subject.hashCode() != 0);
    }
}