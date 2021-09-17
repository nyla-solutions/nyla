package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for MapTextCreator
 * @author Gregory Green
 */
class MapTextCreatorTest
{
    private int size = 3;
    private int keyPadLength = 10;
    private int valueLength = 10;
    private String seedText = "hi";

    @Test
    void creator()
    {
        MapTextCreator subject = new MapTextCreator(size, keyPadLength, valueLength,seedText);
        Map<String, String> actual = subject.create();
        assertNotNull(actual);
        assertEquals(size,actual.size());
        actual.forEach((k,v) -> {
            assertTrue(k.length()>= keyPadLength);
            assertEquals(valueLength,v.length());
        });
    }

    @Test
    void builder()
    {

        MapTextCreator subject = MapTextCreator.builder()
                .size(size)
                .keyPadLength(keyPadLength)
                .valueLength(valueLength)
                .seedText(seedText)
                .build();

        Map<String, String> actual = subject.create();
        assertNotNull(actual);
        assertEquals(size,actual.size());
        actual.forEach((k,v) -> {
            assertTrue(k.length()>= keyPadLength);
            assertEquals(valueLength,v.length());
            assertTrue(k.contains(seedText));
        });

    }
}