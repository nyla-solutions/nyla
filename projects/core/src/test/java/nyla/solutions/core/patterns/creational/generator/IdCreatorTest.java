package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IdCreatorTest
{

    @Test
    public void test_create_return_id_integer()
    {
        IdCreator subject = new IdCreator();
        assertTrue(Text.isInteger(subject.create()));
    }

}