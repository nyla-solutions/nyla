package nyla.solutions.core.net;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NetworkingTest
{

    @Test
    void hostEquals()
    {
        assertTrue(Networking.hostEquals("localhost","localhost"));
        assertTrue(Networking.hostEquals("127.0.0.1","localhost"));
        assertFalse(Networking.hostEquals("127.0.0.1",null));
        assertFalse(Networking.hostEquals(null,"localhost"));
    }
}