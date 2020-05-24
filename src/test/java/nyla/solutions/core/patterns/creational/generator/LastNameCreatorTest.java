package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastNameCreatorTest
{
    @Test
    public void test_create()
    {

        LastNameCreator subject = new LastNameCreator();

        String ln1 = subject.create();
        assertNotNull(ln1);
        String ln2 = subject.create();
        assertNotNull(ln2);

        assertNotEquals(ln1,ln2);
    }
}