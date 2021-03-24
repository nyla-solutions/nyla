package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LastNameCreatorTest
{
    @Test
    public void test_create()
    throws InterruptedException
    {

        LastNameCreator subject = new LastNameCreator();

        String ln1 = subject.create();
        assertNotNull(ln1);
        Thread.sleep(100);
        String ln2 = subject.create();
        assertNotNull(ln2);

        assertNotEquals(ln1,ln2);
    }
}