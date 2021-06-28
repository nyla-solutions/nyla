package nyla.solutions.core.util.stats;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
* @author Gregory Green
*/
public class NullableNumberComparatorTest
{
    private NullableNumberComparator subject = new NullableNumberComparator();

    @Test
    void compare_nulls_equals()
    {
        assertEquals(0,subject.compare(null,null));
    }

    @Test
    void compare_nulls_second_greater_than()
    {
        assertEquals(1,subject.compare(null,1));
    }

    @Test
    void compare_nulls_first_less_than()
    {
        assertEquals(-1,subject.compare(1,null));
    }

    @Test
    void compare_greater()
    {
        assertEquals(1,subject.compare(1,0));
    }
    @Test
    void compare_lessage()
    {
        assertEquals(-1,subject.compare(0,1));
    }

    @Test
    void compare_longs()
    {
        Long long1 = 232L;
        Long long2 = 132L;
        assertEquals(1,subject.compare(long1,long2));
    }

    @Test
    void compare_int()
    {
        int long1 = 1;
        int long2 = 2;
        assertEquals(-1,subject.compare(long1,long2));
    }
}
