package nyla.solutions.core.util.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathematicsTest
{
    Mathematics subject;

    @BeforeEach
    public void setUp()
    {
        subject = new Mathematics();
    }

    @Test
    void var()
    {
        assertEquals(700,subject.variance(10, 20, 60));
    }

    @Test
    void mean()
    {
        assertEquals(30,subject.mean(10, 20, 60));
    }

    @Test
    void varp()
    {
        assertEquals(1622.2222222222224,subject.varp(100, 10, 20));
    }

    @Test
    void stdDev()
    {
        //9, 10, 11
        assertEquals(0.816496580927726,subject.stdDev(9, 10, 11));

    }

    @Test
    void stdDev_null()
    {
        assertEquals(0.816496580927726,subject.stdDev(9, 10, null,11));
    }
    @Test
    void stdDev_list()
    {
        //9, 10, 11
        List<Number> list = Arrays.asList(9, 10, 11);
        assertEquals(0.816496580927726,subject.stdDev(list));
    }

    @Test
    void percentile()
    {
        //10,232,232,323,232
        assertEquals(323,subject.percentile(95.0,10,232,232,323,232));
        assertEquals(454,subject.percentile(95.0,23,75,19,3,5,454,100000,232,23,23,2,32,32,3,2,3,4,34,3,43,43,4,3,43,43,4,3,43,4,34,3,4,3,4343));
        assertEquals(98,subject.percentile(95.0,23,1,23,2,32,3,2,356,56,5,6,57,6,8,9,8,98,9,8,12,1,2,1,21,21,21));
    }

    @Test
    void percentile_empty()
    {
        //10,232,232,323,232
        assertEquals(Double.NaN,subject.percentile(95.0));

    }
    @Test
    void percentile_99_9_when_empty()
    {
        //10,232,232,323,232
        assertEquals(0,subject.percentile(99.9,0));

    }

    @Test
    void percentile_handle_nulls()
    {
        subject.percentile(95.0,10,232,232,323,null);
    }

    @Test
    void sum()
    {
        assertEquals(31,subject.sum(10, 10, 11));
    }

    @Test
    void min()
    {
        double expected = 0.232;
        assertEquals(expected,subject.min(343,232,0.4,0.9,expected));
    }

    @Test
    void min_empty_returns_0()
    {
        assertEquals(0,subject.min());
    }

    @Test
    void max()
    {
        double expected = 99999;
        assertEquals(expected,subject.max(343,232,expected,0.4,0.9));
    }

    @Test
    void max_empty_returns_0()
    {
        assertEquals(0,subject.max());

    }
}