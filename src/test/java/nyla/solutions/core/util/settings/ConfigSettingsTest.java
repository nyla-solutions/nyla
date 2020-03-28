package nyla.solutions.core.util.settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.Properties;


public class ConfigSettingsTest
{

	@Test
	public void testLoadFromCommandLineArgs() throws Exception
	{
		String [] args = {"--spring.output.ansi.enabled","always","--arg1=1","--arg2","2","-arg3=3","-arg4","4","arg5=5","-arg6","\"6\"","-arg7","' 7 '"} ;
		
		Settings c = new ConfigSettings();
		
		String home = c.getProperty("java.home");
		
		c.loadArgs(args);
		assertEquals("always",c.getProperty("spring.output.ansi.enabled"));
		assertEquals("1",c.getProperty("arg1"));
		assertEquals("2",c.getProperty("arg2"));
		assertEquals("3",c.getProperty("arg3"));
		assertEquals("4",c.getProperty("arg4"));
		assertEquals("5",c.getProperty("arg5"));
		assertEquals("6",c.getProperty("arg6"));
		assertEquals(" 7 ",c.getProperty("arg7"));
		
		assertEquals(home,c.getProperty("java.home"));
	}//------------------------------------------------

	@Test
	public void test_SetPRoperties() throws Exception
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
}
