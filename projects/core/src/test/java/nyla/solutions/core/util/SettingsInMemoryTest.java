package nyla.solutions.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SettingsInMemoryTest
{

    private SettingsInMemory subject;
    private Map<Object, Object> map;



    @BeforeEach
    void setUp()
    {
        map = new HashMap<>();
        subject = new SettingsInMemory(map);

    }

    @Test
    void getText()
    {
        String expectedText = "hi";
        String expectedTextKey = "key1";
        map.put(expectedTextKey,expectedText);
        assertEquals(expectedText,subject.getText(expectedTextKey));
    }

    @Test
    void testGetText_default()
    {
        String expectedText = "hi";
        assertEquals(expectedText,subject.getText("notthere",expectedText));
    }

    @Test
    void testGetText_Class_Key()
    {
        String classKey = "hello";
        String expectedKey = this.getClass().getName()+"."+classKey;
        String expected = "12131";
        this.map.put(expectedKey,expected);
        subject.getText(this.getClass(),classKey);
    }

    @Test
    void testGetText_ClassKeyWithDefault()
    {
        String expectedKey = "hello";
        String defaultValue = "world";
        String expected = "expected";
        map.put(getClass().getName()+"."+expectedKey,expected);
        assertEquals(expected,subject.getText(this.getClass(),expectedKey,defaultValue));

    }

    @Test
    void getInteger()
    {
        Integer expected = 3;
        String key = "key";
        map.put(key,expected.toString());
        assertEquals(expected,subject.getInteger(key));
    }

    @Test
    void getCharacter()
    {
        String expected = "G";
        String key = "key";
        map.put(key,expected.toString());
        char defaultValue = 'G';
        assertEquals(expected.charAt(0),subject.getCharacter(getClass(),key,defaultValue));
    }

    @Test
    void testGetInteger_withDefault()
    {
        Integer expected = 56;
        Integer actual = subject.getInteger("notThere",expected);
        assertEquals(expected,actual);
    }

    @Test
    void testGetInteger_byClass_withDefault()
    {
        Integer expected = 56;
        Integer actual = subject.getInteger(getClass(),"notThere",expected);
        assertEquals(expected,actual);
    }

    @Test
    void testGetInteger_byClass()
    {
        Integer expected = 56;
        String key = "the";
        map.put(getClass().getName()+"."+key,expected.toString());
        Integer actual = subject.getInteger(getClass(),key,expected);
        assertEquals(expected,actual);
    }

}