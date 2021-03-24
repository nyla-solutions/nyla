package nyla.solutions.core.patterns.machineLearning.associations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTransitionTest
{
    private ProductTransition<Integer> subject = new ProductTransition<Integer>(1,"hello");

    @Test
    void testEquals()
    {
       ProductTransition<Integer> other = new ProductTransition<>(subject.getTransitionId(), subject.getProductName());
        assertEquals(subject,other);
    }

    @Test
    void TestToString()
    {
        assertTrue(subject.toString().contains(subject.getProductName()));
        assertTrue(subject.toString().contains(subject.getTransitionId().toString()));

    }
}