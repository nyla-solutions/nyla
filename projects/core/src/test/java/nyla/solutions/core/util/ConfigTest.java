package nyla.solutions.core.util;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.settings.ConfigSettingsTest;
import nyla.solutions.core.util.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

import static nyla.solutions.core.util.Config.config;
import static nyla.solutions.core.util.Config.settings;
import static org.junit.jupiter.api.Assertions.*;


public class ConfigTest
{
	/**
	 * Config reload testing
	 */
	static boolean isCalled;
	private Settings subject;

	@BeforeEach
	void setUp() {
		subject = config().getSettings();
	}

	@Test
	void testGetClass()
	{
		Class<? extends ConfigSettingsTest> myclass = settings().getPropertyClass("test.class.name");
		assertEquals(ConfigSettingsTest.class,myclass);
	}

	@Test
	void testGetClass_withDefault()
	{
		Class<? extends ConfigTest> myclass = settings().getPropertyClass("INVALID",ConfigTest.class);
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
		
		assertNotNull(path, settings().getProperty("PATH"));
	}//------------------------------------------------
	
	@Test
	public void test_loadFromArguments() throws Exception
	{
		String[] args  = { "--LOCATOR_HOST=localhost","--url=ExpectedUrl"};
		Settings settings= config().loadArgs(args);
		assertNotNull(settings);
		assertEquals("localhost",settings().getProperty("LOCATOR_HOST"));
		assertEquals("ExpectedUrl",settings().getProperty("url"));
		assertEquals("ExpectedUrl",settings.getProperty("url"));
	}//------------------------------------------------


	@Test
	public void testLoadFromSystemProperties() throws Exception
	{
		String key = System.getProperties().keySet().iterator().next().toString();
		String systemProp = System.getProperty(key);
		
		assertEquals(systemProp, settings().getProperty(key));
		
		
	}//------------------------------------------------
	@Test
	public void testReload()
	{
		System.setProperty(config().SYS_PROPERTY, "");
		System.setProperty("mail.auth.required", "true");
		assertEquals(System.getProperty("mail.auth.required"), "true");
		
		System.setProperty("mail.auth.required", "true");
		config().reLoad();
		
		assertTrue(settings().getPropertyBoolean("mail.auth.required"));
		

		System.setProperty("mail.auth.required", "false");

		config().reLoad();
		assertTrue(!settings().getPropertyBoolean("mail.auth.required"));
	}


	@Test
	void given_url_when_reLoad_whenLoadPropertiesFromUrl() throws MalformedURLException {

		String workDir = System.getProperty("user.dir");
		var url = new URL("file:///"+workDir+"/src/test/resources/config/url.properties");

		System.out.println(url);

		 System.setProperty(Config.SYS_PROPERTY,url.toString());

		 subject.reLoad();

		assertEquals("hello", subject.getProperty ("given_url_when_reLoad_whenLoadPropertiesFromUrl"));

	}

	@Test
	public void testLoadFromPropertyFile()
	{
		System.setProperty(config().SYS_PROPERTY, "src/test/resources/config.properties");
		config().reLoad();
		
		try
		{
			System.setProperty(config().SYS_PROPERTY, "src/test/resources/config.propertie");
			config().reLoad();
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
		String property = settings().getProperty("application.name");
		assertNotNull(property);
		
		//An exception will be thrown if the referenced property does not exist in the property file
		//in this case the ConfigException will be thrown
		try
		{
			property = settings().getProperty("missing.property");
		}
		catch(ConfigException e)
		{
			//valid configuration exception
		}
		
		//Provide a default value if the default value is missing
		property = settings().getProperty("missing.property","default");
		assertEquals("default", property);
		
		
		//Properties can be retrieved by type (boolean, Integer, Character, Long, Bytes)
		//The following assumes;
		//debug=true
		boolean propertyBoolean = settings().getPropertyBoolean("debug");
		assertTrue(propertyBoolean);
		
		//Each getProperty<Type> accepts a default value
		//The following assumes;
		//missing.boolean.property=false
		 propertyBoolean = settings().getPropertyBoolean("missing.boolean.property",false);
		 assertFalse(propertyBoolean);
		 
		 //Config has a user friendly way to associate properties with classes
		 //The properties can be prefixed with the class name
		 //Each getProperty<Type> optional accept the class name as the first argument
		 //The following assumes the property 
		 //solutions.global.util.ConfigTest.integerProperty=24
		 int integerProperty = settings().getPropertyInteger(ConfigTest.class, "integerProperty");
		 assertEquals(24, integerProperty);
		 
		 
		 //Passwords encrypted with the solutions.global.util.Cryption object 
		 //can be retrieved with the settings().getPassword(key) method
		 //An exception will be thrown if the password is not encrypted correctly in the property file
		 //The following is example encrypted password stored in the property file
		 //password={cryption} 2 -21 23 12 2 -21 23 12 2 -21 23 12 2 -21 23 12 2 -21 23 12
		 
		 assertFalse(Cryption.isEncrypted("password"));
		 
		 char[] password = settings().getPropertyPassword("password");
		 assertNotNull(password);
		 
		 
		 //Properties in the System.getProperties() can be merged with the Config's object properties
		 //This is done by setting the property
		 //solutions.global.util.settings().mergeSystemProperties=true
		 String jvmSystemPropertyName = "user.dir";
		 property = settings().getProperty(jvmSystemPropertyName);
		 assertNotNull(property);
		 
		 
		 //solutions.globa.util.settings().useFormatting property can be use to dynamically combine properties.
		 //This feature uses the solutions.global.patterns.decorator.style  package (see Styles interface)
		 //The value of property surrounded with ${property.name} will be formatted by replacing it with the
		 //actual value from another property.
		 
		 //The following is based on the following properties (note this combines the system property "user.dir")
		 //solutions.global.util.settings().useFormatting=true
		 //application.name.debug=${application.name}.${debug}.${user.dir}
	 
		 property = settings().getProperty("application.name.debug");
		 Debugger.println(this,"property="+property);
		
		assertTrue(property.indexOf("${") < 0);
	}// --------------------------------------------------------
	@Test
	public void test_getEnvPropertyNames()
	{
		System.setProperty(config().SYS_PROPERTY, "");
		System.setProperty("SECURITY_USERNAME", "nyla");
		config().reLoad();
		String username1 = config().getPropertyEnv("security-username","");
		
		String username2 = config().getPropertyEnv("SECURITY_USERNAME");
		
		assertEquals(username1,username2);
	}//------------------------------------------------
	
	@Test
	public void test_getEnvPropertyNamesWithProperties()
	{
		System.setProperty(config().SYS_PROPERTY, "");
		System.setProperty("SEC_PROP1", "prop1");
		config().reLoad();
		Properties props = new Properties();
		props.setProperty("sec-prop2", "prop2");
		
		assertNull(config().getPropertyEnv("sec-no",props));
		assertEquals("prop1",config().getPropertyEnv("sec-prop1",props));
		assertEquals("prop2",config().getPropertyEnv("sec-prop2",props));
		
	}//------------------------------------------------
	@Test
	public void testGetDouble() throws Exception
	{
		System.setProperty(config().SYS_PROPERTY, "src/test/resources/config.properties");
		config().reLoad();
		
		Double test = Double.valueOf(30.500000);
		assertEquals(test,settings().getPropertyDouble("nyla.solutions.core.util.ConfigTest.test.config.double"));
		
		test = Double.valueOf(5.55);
		assertTrue(test.equals(settings().getPropertyDouble("doesnotexits",5.55)));
	}
	
	
	@Test
	public void testFileObserver() throws Exception
	{
		
		File config = Paths.get("src/test/resources/config/configTest.properties").toFile();
		
		System.setProperty(config().SYS_PROPERTY, config.getAbsolutePath());
		
		SubjectObserver<Settings> settingsObserver = new SubjectObserver<Settings>()
		{	
			@Override
			public void update(String subjectName, Settings data)
			{
				System.out.println("subjectNAme:"+subjectName+" data:"+data);
				isCalled = true;
			}
		};
		config().registerObserver(settingsObserver);
		config().reLoad();

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
		config().loadArgs(args);
		 String host = settings().getProperty("LOCATOR_HOST");
		 assertEquals("localhost",host);
	}//------------------------------------------------
	
}
