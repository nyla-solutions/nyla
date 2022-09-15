package nyla.solutions.core.util.settings;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


public class ConfigSettingsTest
{

	private Settings subject;

	@BeforeEach
	void setUp()
	{
		subject = new ConfigSettings();
	}

	@Test
	void getPropertyClass()
	{
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
	void reLoad()
	{

	}
}
