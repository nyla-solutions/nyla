package nyla.solutions.core.operations.performance;

import nyla.solutions.core.util.stats.MathematicStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class MathematicStatsDecoratorTest
{
    private MathematicStats stats;
    private MathematicStatsDecorator subject;

    @BeforeEach
    void setUp()
    {
        stats = mock(MathematicStats.class);
        subject = new MathematicStatsDecorator(stats);
    }


    @Test
    void toMilliseconds()
    {
        assertEquals(1,subject.toMilliseconds(1000000));
    }

    @Test
    void reports()
    {
        String text = subject.getText();
        assertNotNull(text);
        System.out.println(text);
    }


}