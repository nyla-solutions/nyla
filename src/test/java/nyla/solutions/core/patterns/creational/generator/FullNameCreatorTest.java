package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameCreatorTest
{

    @Test
    void create()
    {
        FullNameCreator subject = new FullNameCreator();
        assertNotNull(subject.create());
    }
}