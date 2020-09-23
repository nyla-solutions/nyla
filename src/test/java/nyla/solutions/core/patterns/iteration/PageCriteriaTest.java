package nyla.solutions.core.patterns.iteration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageCriteriaTest
{

    @Test
    void getId()
    {
        String expected ="expected";
        assertEquals(expected,new PageCriteria(expected).getId());
    }
}