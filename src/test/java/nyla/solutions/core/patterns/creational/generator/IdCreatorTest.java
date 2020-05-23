package nyla.solutions.core.patterns.creational.generator;

import static org.junit.jupiter.api.Assertions.*;

import nyla.solutions.core.util.Text;
import  org.junit.jupiter.api.*;

class IdCreatorTest
{

    @Test
    public void test_create_return_id_integer()
    {
        IdCreator subject = new IdCreator();
        assertTrue(Text.isInteger(subject.create()));
    }

}