package nyla.solutions.core.patterns.iteration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PageCriteriaTest
{

    @Test
    void getId()
    {
        String expected ="expected";
        assertEquals(expected,new PageCriteria(expected).getId());
    }

    @Test
    void constructor_BeginIndex_Size()
    {
        int beginIndex = 4;
        int size = 5;
        PageCriteria subject = new PageCriteria(beginIndex,size);
        assertEquals(size,subject.getSize());
        assertEquals(beginIndex,subject.getBeginIndex());
    }

    @Test
    void constructor_ID()
    {
        String id ="hello";
        PageCriteria subject = new PageCriteria(id);
        assertEquals(id,subject.getId());
    }

    @Test
    void constructor_BeginIndex_LessThan_1_ThrowsException()
    {
        assertThrows(Exception.class, () -> new PageCriteria(0,1));
        assertDoesNotThrow( () -> new PageCriteria(1,1));
    }

    @Test
    void hasIdentifier()
    {
        PageCriteria subject = new PageCriteria();
        assertFalse(subject.hasIdentifier());
        subject.setId("id");
        assertTrue(subject.hasIdentifier());
        subject.setId("");
        assertFalse(subject.hasIdentifier());
        subject.setId("hello");
        assertTrue(subject.hasIdentifier());
        subject.setId(null);
        assertFalse(subject.hasIdentifier());
    }

    @Test
    void incrementPage()
    {
        int beginIndex = 4;
        int size = 5;
        PageCriteria subject = new PageCriteria(beginIndex,size);
        subject.incrementPage();
        assertEquals(9,subject.getBeginIndex());
        assertEquals(size,subject.getSize());
    }

    @Test
    void incrementPage_PageCriteria()
    {
        int beginIndex = 4;
        int size = 5;
        PageCriteria subject = new PageCriteria(beginIndex,size);
        PageCriteria.incrementPage(subject);
        assertEquals(9,subject.getBeginIndex());
        assertEquals(size,subject.getSize());

    }

    @Test
    void constructor_Size_LessThan_1_ThrowsException()
    {
        assertThrows(Exception.class, () -> new PageCriteria(0,0));
        assertThrows(Exception.class, () -> new PageCriteria(0,-1));
    }
    @Test
    void first()
    {
        int beginIndex =4;
        int size = 1;
        PageCriteria subject = new PageCriteria(beginIndex,size);
        subject.first();
        assertEquals(1,subject.getBeginIndex());
        assertEquals(size,subject.getSize());
    }


    @Test
    void className()
    {
        PageCriteria subject = new PageCriteria();
        String expected = "hello";
        subject.setClassName(expected);
        assertEquals(expected,subject.getClassName());
    }

    @Test
    void getEndIndex()
    {
        assertEquals(6,new PageCriteria(1,5).getEndIndex());
    }

    @Test
    void id()
    {
        PageCriteria subject = new PageCriteria();
        String expected ="id";
        subject.setId(expected);
        assertEquals(expected,subject.getId());
    }
    @Test
    void size()
    {
        PageCriteria subject = new PageCriteria();
        assertEquals(0,subject.getSize());
        int expected = 3;
        subject.setSize(expected);
        assertEquals(expected,subject.getSize());
    }

    @Test
    void savePagination()
    {
        PageCriteria subject = new PageCriteria();
        assertFalse(subject.isSavePagination());
        subject.setSavePagination(true);
        assertTrue(subject.isSavePagination());
        subject.setSavePagination(false);
        assertFalse(subject.isSavePagination());

    }

    @Test
    void testToString()
    {
        int beginIndex =2;
        int size = 3;
        PageCriteria subject = new PageCriteria(beginIndex,size);
        assertThat(subject.toString()).contains(String.valueOf(beginIndex));
        assertThat(subject.toString()).contains(String.valueOf(size));

        String id ="myId";
        subject.setId(id);
        assertThat(subject.toString()).contains(subject.getId());

        subject.setClassName("theClassName");
        assertThat(subject.toString()).contains(subject.getClassName());
    }


    @Test
    void testEquals()
    {
        int beginIndex =2;
        int size = 3;
        PageCriteria subject1 = new PageCriteria(beginIndex,size);
        PageCriteria subject2 = new PageCriteria(beginIndex,size);
        assertTrue(subject1.equals(subject2));

        String id ="myId";
        subject1.setId(id);
        assertFalse(subject1.equals(subject2));
        subject2.setId(id);
        assertTrue(subject1.equals(subject2));


        subject1.setClassName(id);
        assertFalse(subject1.equals(subject2));

        subject2.setClassName(id);
        assertTrue(subject1.equals(subject2));

    }
    @Test
    void testHashCode()
    {
        int beginIndex =2;
        int size = 3;
        PageCriteria subject1 = new PageCriteria(beginIndex,size);
        PageCriteria subject2 = new PageCriteria(beginIndex,size);
        assertEquals(subject1.hashCode(),subject2.hashCode());

        String id ="myId";
        subject1.setId(id);
        assertNotEquals(subject1.hashCode(),subject2.hashCode());
        subject2.setId(id);
        assertEquals(subject1.hashCode(),subject2.hashCode());

    }
}