package nyla.solutions.core.util;

import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.ConfigLockException;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.settings.ConfigSettings;
import nyla.solutions.core.util.settings.Settings;

import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;



/**
 * <pre>
 * This class provides a central mechanism for applications to access
 * key/value property settings and encrypted passwords.
 * 
 * There are several ways to set to specify the 
 * configuration property file location.
 * <ol><li>Add file config.properties to CLASSPATH.
 *This file will be loaded as a Java resource bundle.</li>
 *   <li>Add the JVM argument -Dconfig.properties where the value is equal to 
 *the location of configuration file.
 *
 *Example:
 *-Dconfig.properties=/dev/configurations/files/system/config.properties</li></ol>
 * There are methods to get the String value property  such as <i>Config.getProperty(key)</i> method
 * or get an expected property value of a type such as Integer, Boolean, etc.
 * 
 * JVM argument system properties can also by accessed by adding the
 * following to the configuration file; 
 * <span style="color:blue">
 * nyla.solutions.core.util.Config.mergeSystemProperties=true
 * </span>
 * 
 * Values in the System properties can be set with values for the configuration by using the following
 * solutions.global.util.Config.setSystemProperties=true
 * 
 * It also supports formatting several property values into a single property
 * by the added the following property;
 * 
 * <span style="color:blue">
 * nyla.solutions.core.util.Config.useFormatting=true
 * 
 * <i>Example</i>
 * machineName=localhost
 * host=${machineName}.mycompany.com
 * </span>
 * 
 * By default the configuration is read only once when the 
 * application is initialized. Add the following to the 
 * configuration property file to always reload the property whenever 
 * a getProperty... method is called.
 * 
 * <span style="color:blue">nyla.solutions.core.util.Config.alwaysReloadProperties=true</span>
 * </pre>
 * 
 * @author Gregory Green
 */

public class Config
{

	private final static ReentrantLock lock = new ReentrantLock();

	public static final String RESOURCE_BUNDLE_NAME = "config";

	/**
	 * SMP_PROPERTY_FILE
	 */
	public static final String SYS_PROPERTY = "config.properties";

	public static final String DEFAULT_PROP_FILE_NAME = SYS_PROPERTY;


	private static Settings settings = null;
	private static final long lockPeriodMs = 3000;


	/**
	 * Property may reference properties in example ${prop.name}+somethingElse
	 * @param property the property
	 * @return the formatted value
	 * @throws ConfigException when format exception occur
	 */
	public static String interpret(String property)
	{
		return getSettings().interpret(property);
	}// --------------------------------------------------------
	
	/**
	 * 
	 * @param alwaysReload boolean to determine you should always relaod
	 */
	public static void setAlwaysReload(boolean alwaysReload)
	{
		getSettings().setAlwaysReload(alwaysReload);
	}// --------------------------------------------------------
	
	public static void reLoad()
	{
		getSettings().reLoad();
	}//------------------------------------------------
	
	/**
	 * 
	 * @return the configuration location
	 */
	public static String getLocation()
	{
		return System.getProperty("java.io.tmpdir");
	}// ----------------------------------------------
	/**
	 * 
	 * @return System.getProperty("java.io.tmpdir")
	 */
	public static String getTempDir()
	{
		return System.getProperty("java.io.tmpdir");
	}// ----------------------------------------------

	/**
	 * Retrieves a configuration property as a String object.
	 * Loads the file if not already initialized.
	 * 
	 * @param key Key Name of the property to be returned.
	 * 
	 * @return Value of the property as a string or null if no property found.
	 */

	public static String getProperty(String key)
	{
		return getSettings().getProperty(key);
	}// ------------------------------------------------------------

	/**
	 * 
	 * @param key the key of the property
	 * @return Text.split(getProperty(key))
	 */
	public static String[] getPropertyStrings(String key)
	{
		return getSettings().getPropertyStrings(key);
	}// ------------------------------------------------------------

	/**
	 * Multiple properties separated by white spaces
	 * 
	 * @param aClass the calling classes
	 * @param key the key 
	 * @return array of String
	 */
	public static String[] getPropertyStrings(Class<?> aClass, String key)
	{
		return Text.split(getProperty(aClass, key));
	}// -----------------------------------------------
	/**
	 * Multiple properties separated by white spaces
	 * 
	 * @param aClass
	 * @param key Name of the property to be returned.
	 * @param aDefault the default value
	 * @return Text.split(getProperty(aClass,key,aDefault))
	 */
	public static String[] getPropertyStrings(Class<?> aClass, String key,
			String aDefault)
	{
		return getSettings().getPropertyStrings(aClass, key,aDefault);
	}
	/**
	 * Get the property 
	 * @param aClass the class associate with property
	 * @param key the property key
	 * @param resourceBundle the resource bundle default used if property not found
	 * @return the property key
	 */
	public static String getProperty(Class<?> aClass,String key,ResourceBundle resourceBundle)
	{
		return getSettings().getProperty(aClass, key, resourceBundle);
	}// --------------------------------------------------------
	
	/**
	 * Multiple properties separated by white spaces
	 * 
	 * @param aClass
	 * @param key Name of the property to be returned.
	 * @param aDefault the default value
	 * @return Text.split(getProperty(aClass,key,aDefault))
	 */
	public static String[] getPropertyStrings(Class<?> aClass, String key,
			String... aDefault)
	{
		return getSettings().getPropertyStrings(aClass, key,aDefault);
	}// -----------------------------------------------

	/**
	 * Retrieves a configuration property as a String object.
	 * <p/>
	 * Loads the file if not already initialized.
	 * 
	 * @param aClass the calling class
	 * @param key property key
	 * 
	 * @return Value of the property as a string or null if no property found.
	 */

	public static String getProperty(Class<?> aClass, String key)
	{
		return getSettings().getProperty(aClass, key);
	}// ---------------------------------------------

	/**
	 * Retrieves a configuration property as a String object.
	 * Loads the file if not already initialized.
	 * 
	 * @param aClass the class the name
	 * @param key Name of the property to be returned.
	 * @param aDefault the default value
	 * @return Value of the property as a string or null if no property found.
	 */

	public static String getProperty(Class<?> aClass, String key, String aDefault)
	{
		return getSettings().getProperty(aClass, key, aDefault);

	}// ---------------------------------------------
	public static String getPropertyEnv(String key)
	{
		String env = sanitizeEnvVarNAme(key);
		return getProperty(env);
	}

	public static String getPropertyEnv(String key, String aDefault)
	{
		String env = sanitizeEnvVarNAme(key);
		
		return getProperty(env, aDefault);
	}

	public static String sanitizeEnvVarNAme(String key)
	{
		String env = Text.replaceForRegExprWith(key, "[-\\. ]","_").toUpperCase();
		return env;
	}

	/**
	 * Retrieves a configuration property as a String object.
	 * 
	 * Loads the file if not already initialized.
	 * 
	 * @param key Key Name of the property to be returned.
	 * @param aDefault the default value
	 * @return Value of the property as a string or null if no property found.
	 */

	public static String getProperty(String key, String aDefault)
	{
		return getSettings().getProperty(key, aDefault);

	}// ------------------------------------------------------------

	/**
	 * Get a configuration property as an Integer object.
	 * 
	 * @param aClass calling class
	 * @param key the Key Name of the numeric property to be returned.
	 * @param defaultValue the default value
	 * @return Value of the property as an Integer or null if no property found.
	 */

	public static Integer getPropertyInteger(Class<?> aClass, String key,
			int defaultValue)
	{
		return getSettings().getPropertyInteger(key, defaultValue);
	}// ------------------------------------------------------------
	


	/**
	 * Get a configuration property as an c object.
	 * 
	 * @param aClass the class the property is related to
	 * @param key the configuration name
	 * @param defaultValue the default value to return if the property does not
	 *            exist
	 * @return the configuration character
	 */
	public static Character getPropertyCharacter(Class<?> aClass, String key,
			char defaultValue)
	{
		return getSettings().getPropertyCharacter(aClass, key, defaultValue);

	}// ---------------------------------------------

	/**
	 * Get a configuration property as an Integer object.
	 * 
	 * @param key Name of the numeric property to be returned.
	 * 
	 * @return Value of the property as an Integer or null if no property found.
	 */

	public static Integer getPropertyInteger(String key)
	{
		return getSettings().getPropertyInteger(key);

	}// ------------------------------------------------------------

	public static Integer getPropertyInteger(String key, int aDefault)
	{

		return getSettings().getPropertyInteger(key, aDefault);

	}// -------------------------------------------------------------
	/**
	 * Get a double property
	 * @param cls the class associated with the property
	 * @param key the property key name
	 * @return the double property value
	 */
	public static Double getPropertyDouble(Class<?> cls, String key)
	{
		return getSettings().getPropertyDouble(cls, key);

	}// ---------------------------------------------
	/**
	 * Get a double property
	 * @param aClass the class associated with the property
	 * @param key the property key name
	 * @param defaultValue the default double property
	 * @return the double property value
	 */	
	public static Double getPropertyDouble(Class<?> aClass, String key,
			double defaultValue)
	{
		return getSettings().getPropertyDouble(key, defaultValue);
	}// ------------------------------------------------------------
	/**
	 * 
	 * @param key the double key
	 * @return the Double property
	 */
	public static Double getPropertyDouble(String key)
	{
		return getSettings().getPropertyDouble(key);

	}// ------------------------------------------------------------
	public static Double getPropertyDouble(String key, double aDefault)
	{
		return getPropertyDouble(key, Double.valueOf(aDefault));
	}// -------------------------------------------------------------
	
	public static Double getPropertyDouble(String key, Double aDefault)
	{
		return getSettings().getPropertyDouble(key, aDefault);
	}// ------------------------------------------------------------

	public static Integer getPropertyInteger(Class<?> cls, String key)
	{
		return getSettings().getPropertyInteger(cls, key);

	}// ---------------------------------------------

	public static Integer getPropertyInteger(Class<?> cls, String key,
			Integer aDefault)
	{
		return getSettings().getPropertyInteger(cls, key,aDefault);

	}// ---------------------------------------------

	public static Integer getPropertyInteger(String key, Integer aDefault)
	{
		return getSettings().getPropertyInteger(key, aDefault);
	}// ------------------------------------------------------------

	/**
	 * Get a configuration property as a Boolean object.
	 * 
	 * @param  key the Key Name of the numeric property to be returned.
	 * 
	 * @return Value of the property as an Boolean or null if no property found.

	 *         Note that the value of the returned Boolean will be false if the
	 *         property sought after exists but is not equal to "true" (ignoring
	 *         case).
	 */

	public static Boolean getPropertyBoolean(String key)
	{
		return getSettings().getPropertyBoolean(key);
	}// ------------------------------------------------------------
	/**
	 * 
	 * @param key the property key
	 * @param aBool the default boolean
	 * @return property boolean
	 */
	public static Boolean getPropertyBoolean(String key, Boolean aBool)
	{
		return getSettings().getPropertyBoolean(key, aBool);

	}// ------------------------------------------------------------

	/**
	 * @param aClass the class name
	 * @param key the configuration key
	 * @param aBool default value
	 * 
	 * @return aBool if the configuration value for the key is blank
	 */

	public static Boolean getPropertyBoolean(Class<?> aClass, String key,
			boolean aBool)
	{
		return getSettings().getPropertyBoolean(aClass, key, aBool);
	}// ---------------------------------------------

	/**
	 * @param key the configuration key
	 * @param aBool default value
	 * 
	 * @return aBool if the configuration value for the key is blank
	 */

	public static Boolean getPropertyBoolean(String key, boolean aBool)
	{
		return getSettings().getPropertyBoolean(key, aBool);
	}// ------------------------------------------------------------
	/**
	 * 
	 * @param key the property key
	 * @return the long property
	 */
	public static Long getPropertyLong(String key)
	{
		return getSettings().getPropertyLong(key);
	}// ------------------------------------------------------------
	public static Long getPropertyLong(Class<?> aClass, String key,  long aDefault)
	{
		return getSettings().getPropertyLong(aClass, key, aDefault);
	}// ------------------------------------------------------------
	public static Long getPropertyLong(Class<?> aClass, String key)
	{
		return getSettings().getPropertyLong(aClass, key);
	}// ------------------------------------------------------------
	public static Long getPropertyLong(String key, long aDefault)
	{
		return getSettings().getPropertyLong(key,aDefault);
	}// -------------------------------------------------------------

	public static Long getPropertyLong(String key, Long aDefault)
	{
		return getSettings().getPropertyLong(key,aDefault);
	}// ------------------------------------------------------------

	/**
	 * Get a configuration property as a Password object.
	 * 
	 * @param key Name of the numeric property to be returned.
	 * 
	 * @return Value of the property as an Password or null if no property
	 *         found.
	 *         
	 *         Note that the value of the returned Password will be false if the
	 *         
	 *         property sought after exists but is not equal to "true" (ignoring
	 *         case).
	 */

	public static char[] getPropertyPassword(String key)
	{
		return getSettings().getPropertyPassword(key);
	}// ------------------------------------------------------------

	
	public static Day getDay(String key)
	{
		return new Day(getProperty(key));
	}

	/**
	 * Get the an encrypted password
	 * 
	 * @param key the key
	 * @param defaultPassword
	 * @return the default password if no password exists in the configuration
	 */
	public static char[] getPropertyPassword(String key, char... defaultPassword)
	{
		return getSettings().getPropertyPassword(key, defaultPassword);

	}// ------------------------------------------------------------

	/**
	 * Get the an encrypted password
	 * 
	 * @param key the key
	 * @param defaultPassword
	 * @return the default password if no password exists in the configuration
	 */
    public static char[] getPropertyPassword(String key, String defaultPassword)
	{
		return getSettings().getPropertyPassword(key, defaultPassword);
	}// ------------------------------------------------------------

	/**
	 * Retrieve the password
	 * 
	 * @param aClass the class name
	 * @param key the configuration key
	 * @param defaultPassword default value
	 * 
	 * @return defaultPassword if the configuration value for the key is blank
	 */

	public static char[] getPropertyPassword(Class<?> aClass, String key,
			char[] defaultPassword)
	{
		return getSettings().getPropertyPassword(aClass, key, defaultPassword);
	}// ---------------------------------------------


	/**
	 * @return a copy of the configured properties
	 */

	public static Map<Object,Object> getProperties()
	{
		return getSettings().getProperties();
	}// ------------------------------------------------------------

	public static void setProperties(Properties properties)
	{
		try
		{
			if(lock.tryLock(lockPeriodMs, TimeUnit.MILLISECONDS))
			{
				try
				{
					getSettings().setProperties(properties);
				}
				finally
				{
					lock.unlock();
				}

			}
			else
			{
				throw new ConfigLockException("Setting properties");
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}// --------------------------------------------

	/**
	 * 
	 * @return System.getProperty("user.dir")
	 */
	public static String getUserDir()
	{
		return System.getProperty("user.dir");
	}// --------------------------------------------
	/**
	 * 
	 * @return System.getProperty("file.separator")
	 */
	public static String getFileSeparator()
	{
		return System.getProperty("file.separator");
	}// --------------------------------------------
	public static Settings getSettings()
	{

		try
		{
			if(lock.tryLock(lockPeriodMs,TimeUnit.MILLISECONDS))
			{
				try{
					if(settings == null)
						settings = new ConfigSettings();

					return settings;
				}
				finally
				{
					lock.unlock();
				}
			}
			else
			{
				throw new ConfigLockException("Get settings");
			}
		}
		catch (InterruptedException e)
		{
			throw new ConfigException(e);
		}

	}//------------------------------------------------
	public static void setSettings(Settings theSettings)
	{
		try
		{
			if(lock.tryLock(lockPeriodMs,TimeUnit.MILLISECONDS))
			{
				if (theSettings == null)
					throw new IllegalArgumentException("theSettings is required");

				settings = theSettings;
			}
			else
			{
				throw new ConfigLockException("Setting settings");
			}
		}
		catch (InterruptedException e)
		{
			throw new ConfigException(e);
		}
	}//------------------------------------------------
	/**
	 * Do environment variable name friend configuration lookup
	 * @param key the Environment variable 
	 * @param properties the default properties
	 * @return from properties or environment/configurations
	 */
	public static String getPropertyEnv(String key, Map<?,?> properties)
	{
		Object value = null;
		
		if(properties != null)
		{
			value = properties.get(key);
			if(value != null)
				return value.toString();			
		}
		
		 value = getPropertyEnv(key, "");
		 String text = value.toString();
		 if(text.length() == 0)
			 return null;
		 
		return text;
	}//------------------------------------------------
	public static void registerObserver(SubjectObserver<Settings> settingsObserver)
	{
		getSettings().registerObserver(settingsObserver);
		
	}//------------------------------------------------
	public static Day getPropertyDay(String key)
	{
		return new Day(getProperty(key));
	}
	
	/**
	 * Parse input arguments and add to configuration properties
	 * @param args the input arguments
	 */
	public static Settings loadArgs(String[] args)
	{
		return getSettings().loadArgs(args);
		
	}//------------------------------------------------
	/**
	 * Lookup a property using a default if not found
	 * @param key other property key
	 * @param properties the default props
	 * @param defaultValue the default value to use if not found
	 * @return the found property value
	 */
	public static String getPropertyEnv(String key, Properties properties, String defaultValue)
	{
		String value = getPropertyEnv(key, properties);
		if(value == null || value.length() == 0)
			return defaultValue;
		
		
		return value;
	}

	public static <T> Class<T> getPropertyClass(String propertyKey)
	{
		return getSettings().getPropertyClass(propertyKey);
	}
	public static <T> Class<T> getPropertyClass(String propertyKey,Class<T> defautlClass)
	{
		return getSettings().getPropertyClass(propertyKey,defautlClass);
	}
}
