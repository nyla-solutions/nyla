package nyla.solutions.core.io.csv;

import org.junit.jupiter.api.Test;

import java.util.List;

import static nyla.solutions.core.util.Organizer.toList;
import static org.junit.jupiter.api.Assertions.*;

class CsvOrderByColComparatorTest {

    @Test
    void given_2list_secondsCol1_is_greater_when_compared_then_return_second() {

        var subject = new CsvOrderByColComparator(1);

        var list1 = toList("1","3");
        var list2 = toList("1","1");
        var actual = subject.compare(list1,list2);
        int expected = 1;
        assertEquals(expected, actual);
    }


    @Test
    void given_2list_secondsCol1_is_lessThan_when_compared_then_return_second() {

        var subject = new CsvOrderByColComparator(1);

        var list1 = toList("1","232");
        var list2 = toList("1121","23232");
        var actual = subject.compare(list1,list2);
        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    void given_2list_secondsCol1_Equals_when_compared_then_return_Equal() {

        var subject = new CsvOrderByColComparator(1);

        var list1 = toList("121","23232");
        var list2 = toList("1","23232");
        var actual = subject.compare(list1,list2);
        int expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    void given_l1AndNull_then_return_l1_greater() {
        var subject = new CsvOrderByColComparator(1);

        var list1 = toList("121","23232");
        var actual = subject.compare(list1,null);
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void given_l2And_l1Null_then_return_l2_greater() {
        var subject = new CsvOrderByColComparator(1);

        var list2 = toList("121","23232");
        var actual = subject.compare(null,list2);
        int expected = -1;
        assertEquals(expected, actual);
    }
}