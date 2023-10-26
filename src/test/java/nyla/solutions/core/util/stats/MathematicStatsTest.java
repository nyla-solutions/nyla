package nyla.solutions.core.util.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Gregory Green
 */
public class MathematicStatsTest
{

    private int capacity = 3;
    private Mathematics mathematics;

    @BeforeEach
    void setUp()
    {
        mathematics = mock(Mathematics.class);
    }

    @Test
    void min()
    {
        double expected = 3;
        when(mathematics.min(any(),any(),any())).thenReturn(expected);
        MathematicStats subject = new MathematicStats( capacity, mathematics);
        assertEquals(expected,subject.min());
    }

    @Test
    void max()
    {
        double expected = 3;
        when(mathematics.max(any(),any(),any())).thenReturn(expected);

        var subject = new MathematicStats( capacity, mathematics);

        var actual = subject.max();
        verify(mathematics).max(any(),any(),any());

        assertEquals(expected,actual);
    }
}
