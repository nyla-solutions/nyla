package nyla.solutions.core.patterns.loadbalancer;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PropertiesLoadBalanceRegistryTest
{
    private PropertiesLoadBalanceRegistry subject;
    private final String outDirectoryPath = IO.tempDir()+"/lb.properties";
    private Properties properties;

    @BeforeEach
    void setUp()
    {
        properties =  new Properties();

        subject = new PropertiesLoadBalanceRegistry(properties,outDirectoryPath);
    }


    @Test
    void lookup()
    {
        String expected = "Hi";
        properties.put("nyla",expected);

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


}