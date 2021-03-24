package nyla.solutions.core.util;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.settings.ConfigSettingsTest;
import nyla.solutions.core.util.settings.Settings;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;


public class ConfigTest
{
	/**
	 * Config reload testing
	 */
	static boolean isCalled;

	@Test
	void testGetClass()
	{
		Class<? extends ConfigSettingsTest> myclass = Config.getPropertyClass("test.class.name");
		assertEquals(ConfigSettingsTest.class,myclass);
	}

	@Test
	void testGetClass_withDefault()
	{
		Class<? extends ConfigTest> myclass = Config.getPropertyClass("INVALID",ConfigTest.class);
		assertEquals(ConfigTest.class,myclass);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoadFromEnv() throws Exception
	{
		String path = System.getenv("PATH");
		
		assertNotNull(path, Config.getProperty("PATH"));
	}//------------------------------------------------
	
	@Test
	public void test_loadFromArguments() throws Exception
	{
		String[] args  = { "--LOCATOR_HOST=localhost","--url=ExpectedUrl"};
		Settings settings= Config.loadArgs(args);
		assertNotNull(settings);
		assertEquals("localhost",Config.getProperty("LOCATOR_HOST"));
		assertEquals("ExpectedUrl",Config.getProperty("url"));
		assertEquals("ExpectedUrl",settings.getProperty("url"));
	}//------------------------------------------------


	@Test
	public void testLoadFromSystemProperties() throws Exception
	{
		String key = System.getProperties().keySet().iterator().next().toString();
		String systemProp = System.getProperty(key);
		
		assertEquals(systemProp, Config.getProperty(key));
		
		
	}//------------------------------------------------
	@Test
	public void testReload()
	{
		System.setProperty(Config.SYS_PROPERTY, "");
		System.setProperty("mail.auth.required", "true");
		assertEquals(System.getProperty("mail.auth.required"), "true");
		
		System.setProperty("mail.auth.required", "true");
		Config.reLoad();
		
		assertTrue(Config.getPropertyBoolean("mail.auth.required"));
		

		System.setProperty("mail.auth.required", "false");

		Config.reLoad();
		assertTrue(!Config.getPropertyBoolean("mail.auth.required"));
	}
	
	@Test
	public void testLoadFromPropertyFile()
	{
		System.setProperty(Config.SYS_PROPERTY, "src/test/resources/config.properties");
		Config.reLoad();
		
		try
		{
			System.setProperty(Config.SYS_PROPERTY, "src/test/resources/config.propertie");
			Config.reLoad();			
			fail();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}// --------------------------------------------------------
	
	@Test
	public void testGetPropertyString()
	{
		//Get a default string property
		//The following assumes;
		//application.name=JUNIT
		String property = Config.getProperty("application.name");
		assertNotNull(property);
		
		//An exception will be thrown if the referenced property does not exist in the property file
		//in this case the ConfigException will be thrown
		try
		{
			property = Config.getProperty("missing.property");
		}
		catch(ConfigException e)
		{
			//valid configuration exception
		}
		
		//Provide a default value if the default value is missing
		property = Config.getProperty("missing.property","default");
		assertEquals("default", property);
		
		
		//Properties can be retrieved by type (boolean, Integer, Character, Long, Bytes)
		//The following assumes;
		//debug=true
		boolean propertyBoolean = Config.getPropertyBoolean("debug");
		assertTrue(propertyBoolean);
		
		//Each getProperty<Type> accepts a default value
		//The following assumes;
		//missing.boolean.property=false
		 propertyBoolean = Config.getPropertyBoolean("missing.boolean.property",false);
		 assertFalse(propertyBoolean);
		 
		 //Config has a user friendly way to associate properties with classes
		 //The properties can be prefixed with the class name
		 //Each getProperty<Type> optional accept the class name as the first argument
		 //The following assumes the property 
		 //solutions.global.util.ConfigTest.integerProperty=24
		 int integerProperty = Config.getPropertyInteger(ConfigTest.class, "integerProperty");
		 assertEquals(24, integerProperty);
		 
		 
		 //Passwords encrypted with the solutions.global.util.Cryption object 
		 //can be retrieved with the Config.getPassword(key) method
		 //An exception will be thrown if the password is not encrypted correctly in the property file
		 //The following is example encrypted password stored in the property file
		 //password={cryption} 2 -21 23 12 2 -21 23 12 2 -21 23 12 2 -21 23 12 2 -21 23 12
		 
		 assertFalse(Cryption.isEncrypted("password"));
		 
		 char[] password = Config.getPropertyPassword("password");
		 assertNotNull(password);
		 
		 
		 //Properties in the System.getProperties() can be merged with the Config's object properties
		 //This is done by setting the property
		 //solutions.global.util.Config.mergeSystemProperties=true
		 String jvmSystemPropertyName = "user.dir";
		 property = Config.getProperty(jvmSystemPropertyName); 
		 assertNotNull(property);
		 
		 
		 //solutions.globa.util.Config.useFormatting property can be use to dynamically combine properties.
		 //This feature uses the solutions.global.patterns.decorator.style  package (see Styles interface)
		 //The value of property surrounded with ${property.name} will be formatted by replacing it with the
		 //actual value from another property.
		 
		 //The following is based on the following properties (note this combines the system property "user.dir")
		 //solutions.global.util.Config.useFormatting=true
		 //application.name.debug=${application.name}.${debug}.${user.dir}
	 
		 property = Config.getProperty("application.name.debug");
		 Debugger.println(this,"property="+property);
		
		assertTrue(property.indexOf("${") < 0);
	}// --------------------------------------------------------
	@Test
	public void test_getEnvPropertyNames()
	{
		System.setProperty(Config.SYS_PROPERTY, "");
		System.setProperty("SECURITY_USERNAME", "nyla");
		Config.reLoad();
		String username1 = Config.getPropertyEnv("security-username","");
		
		String username2 = Config.getPropertyEnv("SECURITY_USERNAME");
		
		assertEquals(username1,username2);
	}//------------------------------------------------
	
	@Test
	public void test_getEnvPropertyNamesWithProperties()
	{
		System.setProperty(Config.SYS_PROPERTY, "");
		System.setProperty("SEC_PROP1", "prop1");
		Config.reLoad();
		Properties props = new Properties();
		props.setProperty("sec-prop2", "prop2");
		
		assertNull(Config.getPropertyEnv("sec-no",props));
		assertEquals("prop1",Config.getPropertyEnv("sec-prop1",props));
		assertEquals("prop2",Config.getPropertyEnv("sec-prop2",props));
		
	}//------------------------------------------------
	@Test
	public void testGetDouble() throws Exception
	{
		System.setProperty(Config.SYS_PROPERTY, "src/test/resources/config.properties");
		Config.reLoad();
		
		Double test = Double.valueOf(30.500000);
		assertEquals(test,Config.getPropertyDouble("nyla.solutions.core.util.ConfigTest.test.config.double"));
		
		test = Double.valueOf(5.55);
		assertTrue(test.equals(Config.getPropertyDouble("doesnotexits",5.55)));
	}
	
	
	@Test
	public void testFileObserver() throws Exception
	{
		
		File config = Paths.get("src/test/resources/config/configTest.properties").toFile();
		
		System.setProperty(Config.SYS_PROPERTY, config.getAbsolutePath());
		
		SubjectObserver<Settings> settingsObserver = new SubjectObserver<Settings>()
		{	
			@Override
			public void update(String subjectName, Settings data)
			{
				System.out.println("subjectNAme:"+subjectName+" data:"+data);
				isCalled = true;
			}
		};
		Config.registerObserver(settingsObserver);
		Config.reLoad();

		IO.touch(config);
		Thread.sleep(1000);
		IO.touch(config);
		Thread.sleep(1000);
		IO.touch(config);
		assertTrue(isCalled);
		
	}

	
	@Test
	public void test_loadFromArgumentsFixed() throws Exception
	{
		String [] args = {"--LOCATOR_HOST=localhost"};
		Config.loadArgs(args);
		 String host = Config.getProperty("LOCATOR_HOST");
		 assertEquals("localhost",host);
	}//------------------------------------------------
	
}
