package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.exception.RequiredException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedNameCreatorTest
{

    @Test
    void givenArrayThreeWhenCreate_ThenReturnFromArray()
    {
        FixedNameCreator subject = new FixedNameCreator("0-zero","1-first","2-twice");
        assertEquals(subject.create(),"0-zero");
        assertEquals(subject.create(),"1-first");
        assertEquals(subject.create(),"2-twice");
        assertEquals(subject.create(),"0-zero");
        assertEquals(subject.create(),"1-first");
        assertEquals(subject.create(),"2-twice");
    }

    @Test
    void givenArrayWhenCreate_ThenReturnFromArray()
    {
        FixedNameCreator subject = new FixedNameCreator("0-first","2-seconds");
        assertEquals(subject.create(),"0-first");
        assertEquals(subject.create(),"2-seconds");
        assertEquals(subject.create(),"0-first");
        assertEquals(subject.create(),"2-seconds");
        assertEquals(subject.create(),"0-first");
    }

    @Test
    void givenEmptyWhenCreate_ThenReturnFromArray()
    {
        assertThrows(RequiredException.class, () -> new FixedNameCreator());

    }
}