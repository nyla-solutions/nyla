package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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


    @Test
    void debugPerfTest() {

        final int  valueLength = 25;
        final int keyLength = 15;
        final int putCount = 3;
        final String seedText = "Hello";

        var map = MapTextCreator.builder().size(putCount)
                .keyPadLength(keyPadLength)
                .valueLength(valueLength)
                .seedText(seedText)
                .build().create();

        assertThat(map).isNotNull();
    }
}