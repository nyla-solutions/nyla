package nyla.solutions.core.util;

import nyla.solutions.core.data.clock.Day;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.ConfigLockException;
import nyla.solutions.core.patterns.observer.SubjectObserver;
import nyla.solutions.core.util.settings.ConfigSettings;
import nyla.solutions.core.util.settings.Settings;

import java.util.Map;
import java.util.Properties;
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

public class Config {

	private final static ReentrantLock lock = new ReentrantLock();

	public static final String RESOURCE_BUNDLE_NAME = "config";

	/**
	 * SMP_PROPERTY_FILE
	 */
	public static final String SYS_PROPERTY = "config.properties";

	public static final String DEFAULT_PROP_FILE_NAME = SYS_PROPERTY;

	private Settings settings;
	private static final long lockPeriodMs = 3000;

	private static Config instance = null;

	public Config() {
		this(new ConfigSettings());
	}

	public Config(Settings settings) {
		this.settings = settings;
	}
	public synchronized static Config config()
	{
		try {
			if (lock.tryLock(lockPeriodMs, TimeUnit.MILLISECONDS)) {
				try {
					if (instance == null)
						instance = new Config();

					return instance;
				} finally {
					lock.unlock();
				}
			} else {
				throw new ConfigLockException("Get settings");
			}
		} catch (InterruptedException e) {
			throw new ConfigException(e);
		}

	}


	/**
	 * Property may reference properties in example ${prop.name}+somethingElse
	 *
	 * @param property the property
	 * @return the formatted value
	 * @throws ConfigException when format exception occur
	 */
	public static String interpret(String property) {
		return config().getSettings().interpret(property);
	}// --------------------------------------------------------

	/**
	 * @param alwaysReload boolean to determine you should always relaod
	 */
	public static void setAlwaysReload(boolean alwaysReload) {
		config().getSettings().setAlwaysReload(alwaysReload);
	}// --------------------------------------------------------

	public static void reLoad() {
		config().getSettings().reLoad();
	}

	/**
	 * @return the configuration location
	 */
	public static String getLocation() {
		return System.getProperty("java.io.tmpdir");
	}// ----------------------------------------------

	/**
	 * @return System.getProperty(" java.io.tmpdir ")
	 */
	public static String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}// ----------------------------------------------



	public String getPropertyEnv(String key) {
		String env = sanitizeEnvVarNAme(key);
		return getSettings().getProperty(env);
	}

	public String getPropertyEnv(String key, String aDefault) {
		String env = sanitizeEnvVarNAme(key);

		return getSettings().getProperty(env, aDefault);
	}

	public static String sanitizeEnvVarNAme(String key) {
		String env = Text.replaceForRegExprWith(key, "[-\\. ]", "_").toUpperCase();
		return env;
	}


	public Day getDay(String key) {
		return new Day(getSettings().getProperty(key));
	}

	public static Settings  settings()
	{
		return config().getSettings();
	}

	public static void setProperties(Properties properties) {
		try {
			if (lock.tryLock(lockPeriodMs, TimeUnit.MILLISECONDS)) {
				try {
					config().getSettings().setProperties(properties);
				} finally {
					lock.unlock();
				}

			} else {
				throw new ConfigLockException("Setting properties");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}// --------------------------------------------

	/**
	 * @return System.getProperty(" user.dir ")
	 */
	public static String getUserDir() {
		return System.getProperty("user.dir");
	}// --------------------------------------------

	/**
	 * @return System.getProperty(" file.separator ")
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}// --------------------------------------------

	public  Settings getSettings() {
		return this.settings;
	}//------------------------------------------------

	public void setSettings(Settings theSettings) {
		try {
			if (lock.tryLock(lockPeriodMs, TimeUnit.MILLISECONDS)) {
				if (theSettings == null)
					throw new IllegalArgumentException("theSettings is required");

				this.settings = theSettings;
			} else {
				throw new ConfigLockException("Setting settings");
			}
		} catch (InterruptedException e) {
			throw new ConfigException(e);
		}
	}//------------------------------------------------

	/**
	 * Do environment variable name friend configuration lookup
	 *
	 * @param key        the Environment variable
	 * @param properties the default properties
	 * @return from properties or environment/configurations
	 */
	public String getPropertyEnv(String key, Map<?, ?> properties) {
		Object value = null;

		if (properties != null) {
			value = properties.get(key);
			if (value != null)
				return value.toString();
		}

		value = getPropertyEnv(key, "");
		String text = value.toString();
		if (text.length() == 0)
			return null;

		return text;
	}//------------------------------------------------

	public static void registerObserver(SubjectObserver<Settings> settingsObserver) {
		settings().registerObserver(settingsObserver);

	}//------------------------------------------------

	public Day getPropertyDay(String key) {
		return new Day(getSettings().getProperty(key));
	}

	/**
	 * Parse input arguments and add to configuration properties
	 *
	 * @param args the input arguments
	 */
	public static Settings loadArgs(String[] args) {
		return settings().loadArgs(args);

	}//------------------------------------------------

	/**
	 * Lookup a property using a default if not found
	 *
	 * @param key          other property key
	 * @param properties   the default props
	 * @param defaultValue the default value to use if not found
	 * @return the found property value
	 */
	public String getPropertyEnv(String key, Properties properties, String defaultValue) {
		String value = getPropertyEnv(key, properties);
		if (value == null || value.length() == 0)
			return defaultValue;

		return value;
	}
}
