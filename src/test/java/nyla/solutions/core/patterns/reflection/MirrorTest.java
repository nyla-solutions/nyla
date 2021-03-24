package nyla.solutions.core.patterns.reflection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MirrorTest
{

    @Test
    void isPrimitive()
    {
        assertTrue(Mirror.isPrimitive(int.class));
        assertTrue(Mirror.isPrimitive(float.class));
        assertTrue(Mirror.isPrimitive(double.class));
        assertTrue(Mirror.isPrimitive(long.class));
        assertTrue(Mirror.isPrimitive(String.class));

        assertFalse(Mirror.isPrimitive(Object[].class));
        assertFalse(Mirror.isPrimitive(Object[].class));
    }
}