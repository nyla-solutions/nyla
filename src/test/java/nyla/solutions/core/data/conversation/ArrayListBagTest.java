package nyla.solutions.core.data.conversation;

import nyla.solutions.core.exception.NonSerializableException;
import nyla.solutions.core.util.Scheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ArrayListBagTest
{
    private String excepted = "hello";
    private ArrayListBag<String> subject;
    private ArrayList<String> list;

    @BeforeEach
    void setUp()
    {
        list = new ArrayList<>();
        list.add(excepted);
       subject = new ArrayListBag<>(list);
    }

    @Test
    void given_Bag_when_unbag_thenEquals()
    {
        subject.bag(list);
        assertEquals(list,subject.unbag());
    }

    @Test
    void givenEmptyBag_when_bagNull_ThenReturnNull()
    {
        subject = new ArrayListBag<>();
        subject.bag(null);
        assertNull(subject.unbag());
    }

    @Test
    void givenEmptyBag_when_bagEmpty_ThenReturnNull()
    {
        subject = new ArrayListBag<>();
        subject.bag(new ArrayList<>());
        assertNull(subject.unbag());
    }

    @Test
    void when_BagContainsNonSerialized()
    {
        subject = new ArrayListBag<>();
        ArrayList list  =new ArrayList<>();
        list.add(new NonSerializable());
        assertThrows(NonSerializableException.class, () -> subject.bag(list));
    }

    @Test
    void when_BagContainsDate()
    {
        ArrayListBag<Date> subject = new ArrayListBag<>();
        ArrayList list  =new ArrayList<>();
        Date expectedDate = Scheduler.dateNow();
        list.add(expectedDate);
        subject.bag(list);
        List<Date> actual =  subject.unbag();
        assertNotNull(actual);
        assertThat(actual).contains(expectedDate);
    }

    @Test
    void when_BagContainsList()
    {
        ArrayListBag<?> subject = new ArrayListBag<>();
        ArrayList list  = new ArrayList<>();
        LinkedList<String> expectedList = new LinkedList<>();
        list.add(expectedList);
        subject.bag(list);
        List<?> actual =  subject.unbag();
        assertNotNull(actual);
        assertEquals(1,actual.size());

    }

    class NonSerializable
    {
    }
}