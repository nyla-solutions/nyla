package nyla.solutions.core.util.settings;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.security.user.data.UserProfile;
import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class ConfigSettingsTest
{

	private ConfigSettings subject;
    @Mock
    private SubjectObserver<Settings> settingObserver;

    @BeforeEach
	void setUp()
	{
		subject = new ConfigSettings();
	}

	@Test
	void getPropertyClass()
	{
        System.setProperty(Config.SYS_PROPERTY,"src/test/resources/config.properties");
        subject.reLoad();

		assertEquals(ConfigSettingsTest.class,subject.getPropertyClass("test.class.name"));
	}

	@Test
	void getPropertyClass_with_default()
	{
		assertEquals(UserProfile.class,subject.getPropertyClass("invalid",UserProfile.class));
	}

	@Test
	public void testLoadFromCommandLineArgs() throws Exception
	{
		String [] args = {"--spring.output.ansi.enabled","always","--arg1=1","--arg2","2","-arg3=3","-arg4","4","arg5=5","-arg6","\"6\"","-arg7","' 7 '"} ;
		
		Settings subject = new ConfigSettings();
		
		String home = subject.getProperty("java.home");
		
		Settings actual = subject.loadArgs(args);
		assertEquals("always",subject.getProperty("spring.output.ansi.enabled"));
		assertEquals("1",subject.getProperty("arg1"));
		assertEquals("2",subject.getProperty("arg2"));
		assertEquals("3",subject.getProperty("arg3"));
		assertEquals("4",subject.getProperty("arg4"));
		assertEquals("5",subject.getProperty("arg5"));
		assertEquals("6",subject.getProperty("arg6"));
		assertEquals(" 7 ",subject.getProperty("arg7"));
		
		assertEquals(home,subject.getProperty("java.home"));
		assertEquals(subject,actual);
	}//------------------------------------------------

	@Test
	void givenEnvVarWhenGetPropertiesThenEnvFound()
	{
		String actual = subject.getProperty("USER");
		assertNotNull(actual);
	}

	@Test
	public void test_SetProperties() throws Exception
	{
		Settings c = new ConfigSettings();
		Map<Object,Object> map = c.getProperties();
		
		Object key = map.entrySet().iterator().next().getKey();
		
		Properties props = new Properties();
		props.setProperty("new2", "new");
		
		c.setProperties(props);
		
		assertTrue(c.getProperties().containsKey(key));
		assertTrue(c.getProperties().containsKey("new2"));
		
		c.setProperties(new Properties());
		
		assertTrue(c.getProperties().containsKey(key));
		assertTrue(c.getProperties().containsKey("new2"));
		
	}

	@Test
	void getPasswordWithClass()
	{
		Settings subject = new ConfigSettings();

		var actual = subject.getPropertyPassword(ConfigSettingsTest.class,"password");

		System.out.println(actual);
		assertThat(actual).isNotEmpty();

	}

    @Test
    void given_property_updates_when_executed_in_thread_then_changed() throws IOException, InterruptedException {

        var junitFilePropertyFile = "runtime/tmp/junit.properties";
        IO.dir().mkdir("runtime/tmp");
        var properties = new Properties();
        properties.setProperty("key1","value1");
        properties.store(new FileWriter(Paths.get(junitFilePropertyFile).toFile()),"");


        System.setProperty(Config.SYS_PROPERTY,junitFilePropertyFile);

        subject = new ConfigSettings(properties);
        subject.setAlwaysReload(true);
        subject.watchFile(10, 500, 1);

        assertThat(subject.getProperty("key1")).isEqualTo("value1");


        properties.put("key2","value2");
        properties.store(new FileWriter(Paths.get(junitFilePropertyFile).toFile()),"");

        sleep(6000);

        assertThat(subject.getProperty("key2")).isEqualTo("value2");
    }

    @AfterEach
    void tearDown() {
        if(subject.getReloadScheduler()!=null)
            subject.getReloadScheduler().close();
    }
}
