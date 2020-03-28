package nyla.solutions.core.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropertyTest
{

    @Test
    public void testCompareTo()
    {
        assertTrue(new Property("test0", "test0").compareTo(new Property("test0", "test0")) == 0);

        int compare = new Property("test0", "test0").compareTo(new Property("test1", "test0"));
        assertTrue(compare < 0);

        compare = new Property("test1", "test0").compareTo(new Property("test0", "test0"));
        assertTrue(compare > 0);

        compare = new Property("test0", "test0").compareTo(new Property("test0", "test1"));

        assertTrue(compare < 0);

        compare = new Property("test0", "test1").compareTo(new Property("test0", "test0"));

        assertTrue(compare > 0);
    }

}
