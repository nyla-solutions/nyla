package nyla.solutions.core.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NamedIdentifierTest
{
    @Test
    void name()
    {
        String expected = "name";
        assertEquals(expected,new NamedIdentifier(expected, expected).getName());
    }

    @Test
    void id()
    {
        String expected = "id";
        assertEquals(expected,new NamedIdentifier("name",expected).getId());
    }


}