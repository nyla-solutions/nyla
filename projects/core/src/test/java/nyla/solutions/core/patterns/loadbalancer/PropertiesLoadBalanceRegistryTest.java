package nyla.solutions.core.patterns.loadbalancer;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PropertiesLoadBalanceRegistryTest
{
    private PropertiesLoadBalanceRegistry subject;

    @BeforeAll
    public static void init()
    {
        System.setProperty(PropertiesLoadBalanceRegistry.PROPERTY_FILE_PATH_PROP,"src/test/resources/nyla/solutions/core/patterns/loadbalancer/lb.properties");
        Config.reLoad();
    }

    @BeforeEach
    void setUp()
    {
        subject = new PropertiesLoadBalanceRegistry();
    }

    @Test
    void when_setUp()
    {
        subject.setUp();
        assertNotNull(subject.getProperties());
    }

    @Test
    void lookup()
    {
        String expected = "Hi";
        assertEquals(expected,subject.lookup("nyla"));
    }

    @Test
    void unregister()
    {
        String location = "I donot know";
        subject.register(location);
        assertTrue(subject.listRegistered().contains(location));
        subject.unregister(location);
        assertFalse(subject.listRegistered().contains(location));
    }

    @Test
    void register_location_then_listRegister_contains_locations()
    {
        String location = "I donot know";
        subject.register(location);
        assertTrue(subject.listRegistered().contains(location));
    }

    @Test
    void next_When_Nothing_Registered_Then_Returns()
    {
        assertNull(subject.next());
    }

    @Test
    void when_register_Then_listRegistered_contains_expected()
    {
        String expected = "Hi";
        subject.register(expected);
        assertThat(subject.listRegistered()).contains(expected);
    }

    @Test
    void getPropertyFilePath()
    {
        assertNotNull(subject.getPropertyFilePath());
    }

    @Test
    void setPropertyFilePath()
    {
        String expected = "target/lb.properties";
        subject.setPropertyFilePath(expected);
        assertEquals(expected,subject.getPropertyFilePath());
    }
}