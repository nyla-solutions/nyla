package nyla.solutions.spring.settings;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Cryption;

/**
 * Test EnvironmentSettings
 * @author Gregory Green
 *
 */
public class EnvironmentSettingsTest
{
	private Environment environment = null;
	
	/**
	 * setup mock environment
	 * @throws Exception
	 */
	@Before
	public void setUp()
	throws Exception
	{
		System.setProperty(Cryption.CRYPTION_KEY_PROP, "JUNIT_TESTING");
		Config.reLoad();
		Cryption cryption = new Cryption();
		String encryptedPassword = Cryption.CRYPTION_PREFIX+cryption.encryptText("PASSWORD");
		
		environment = Mockito.mock(Environment.class);
		Mockito.when(environment.getProperty("valid")).thenReturn("valid");
		Mockito.when(environment.getProperty("myPasswordProperty")).thenReturn(encryptedPassword);
		
	}
	
	/**
	 * Test GetRawPropertyerty
	 */
	@Test
	public void testGetRawProperty()
	{
		EnvironmentSettings environmentSettings = new EnvironmentSettings(environment);
		
		Assert.assertEquals(environmentSettings.getProperty("invalid",""),"");
	
		
		Assert.assertEquals("PASSWORD",String.valueOf(environmentSettings.getPropertyPassword("myPasswordProperty","")));
		
		try
		{
			environmentSettings.getProperty("invalid");
			Assert.fail();
		}
		catch (ConfigException e)
		{
		}
		
	}//------------------------------------------------
	/**
	 * Test Config.setSetting
	 */
	@Test
	public void testConfigSettings()
	{
		EnvironmentSettings environmentSettings = new EnvironmentSettings(environment);
		Config.setSettings(environmentSettings);
		
		Assert.assertTrue(Config.getSettings() instanceof EnvironmentSettings);
		Assert.assertEquals(Config.getProperty("invalid",""),"");
	
		Assert.assertEquals("PASSWORD",String.valueOf(Config.getPropertyPassword("myPasswordProperty","")));
		
		try
		{
			Config.getProperty("invalid");
			Assert.fail();
		}
		catch (ConfigException e)
		{
		}
	}//------------------------------------------------
}
