package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FirstNameCreatorTest
{
    @Test
    public void test_create()
    {

        FirstNameCreator subject = new FirstNameCreator();

        String fn1 = subject.create();
        assertNotNull(fn1);
        String fn2 = subject.create();
        assertNotNull(fn2);
    }
}