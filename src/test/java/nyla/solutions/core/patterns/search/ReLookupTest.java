package nyla.solutions.core.patterns.search;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReLookupTest
{
    private ReLookup<UserProfile> subject;
    private UserProfile expected;

    @BeforeEach
    void setUp()
    {
        subject = new ReLookup<>();
        expected = createUserProfile();
    }

    private UserProfile createUserProfile()
    {
        return new JavaBeanGeneratorCreator
                <UserProfile>(UserProfile.class)
                .randomizeAll().create();
    }

    @Nested
    public class WhenLookup
    {
        @Test
        void lookup_matchesExpressions()
        {
            subject.put(".*smiling.*",expected);
            String key= "She was smiling all the time";
            UserProfile actual = subject.lookup(key);
            assertEquals(expected, actual);
        }
        @Test
        void lookup_noMatchesExpressions()
        {
            subject.put(".*smiling.*",expected);
            String key= "She was smil all the time";
            UserProfile actual = subject.lookup(key);
            assertNotEquals(expected, actual);
        }
    }


    @Test
    void lookupCollection()
    {
        subject.put("A",createUserProfile());
        subject.put("B",createUserProfile());
        subject.put("AA",createUserProfile());
        String expression = "A*";
        Collection<UserProfile> collection = subject.lookupCollection(expression);
        assertNotNull(collection);
        assertEquals(subject.size()-1,collection.size());
    }
    @Test
    void lookupCollection_GivenNull_ThenReturn_Null()
    {
        assertNull(subject.lookupCollection(null));

        subject.put("A",createUserProfile());
        subject.put("B",createUserProfile());
        subject.put("AA",createUserProfile());
        assertNull(subject.lookupCollection(null));
    }

    @Test
    void get_put()
    {
        String key = "A";
        subject.put(key,expected);
        assertEquals(expected,subject.get(key));
    }

    @Test
    void isEmpty()
    {
        assertTrue(subject.isEmpty());
        subject.put("",expected);
        assertFalse(subject.isEmpty());
    }

    @Test
    void containsKey()
    {
        String expectedKey = "hi";
        assertFalse(subject.containsKey(expectedKey));
        subject.put(expectedKey,expected);
        assertTrue(subject.containsKey(expectedKey));
    }

    @Test
    void containsValue()
    {
        String expectedKey = "hi";
        assertFalse(subject.containsValue(expected));
        subject.put(expectedKey,expected);
        assertTrue(subject.containsKey(expectedKey));
    }

    @Test
    void clear()
    {
        String expectedKey = "hi";
        subject.put(expectedKey,expected);
        assertFalse(subject.isEmpty());
        subject.clear();
        assertTrue(subject.isEmpty());
    }

    @Test
    void keySet()
    {
        String expectedKey = "hi";
        Collection<String> expectedKeys = Collections.singletonList(expectedKey);
        assertNotEquals(expectedKeys, subject.keySet());
        subject.put(expectedKey,expected);
        assertEquals(expectedKeys.size(), subject.keySet().size());
    }

    @Test
    void entrySet()
    {
        String expectedKey = "hi";
        assertTrue(subject.entrySet().isEmpty());
        subject.put(expectedKey,expected);
        assertEquals(subject.entrySet().size(), subject.entrySet().size());
    }

    @Test
    void size()
    {
        int expectedSize = 0;
        assertEquals(expectedSize,subject.size());
        subject.put("A",expected);
        expectedSize++;
        assertEquals(expectedSize,subject.size());

    }

    @Test
    void remove()
    {
        int expectedSize = 0;
        subject.put("A",expected);
        expectedSize++;
        assertEquals(expectedSize,subject.size());
        subject.remove("A");
        expectedSize--;
        assertEquals(expectedSize,subject.size());
    }

    @Test
    void putAll()
    {
        Map<String, UserProfile> expectedMap = new HashMap<>();
        subject.putAll(expectedMap);
        assertTrue(subject.isEmpty());
        expectedMap.put("A",expected);
        subject.putAll(expectedMap);
        assertFalse(subject.isEmpty());
    }

    @Test
    void values()
    {

        Collection<UserProfile> actual = subject.values();
        assertTrue(actual.isEmpty());
        subject.put("A",expected);
        actual = subject.values();
        assertFalse(actual.isEmpty());
        assertEquals(expected,subject.values().iterator().next());
    }
}