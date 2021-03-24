package nyla.solutions.core.util.settings;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.observer.SubjectObserver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Interface for configuration settings
 * 
 * @author Gregory Green
 *
 */
public interface Settings
{

	boolean isUseFormatting();
	
	/**
	 * the location of where the setting were loaded
	 * @return the location
	 */
	String getLocation();
	
	/**
	 * Property may reference properties in example ${prop.name}+somethingElse
	 * @param property the property
	 * @return the formatted value
	 * @throws ConfigException when format exception occur
	 */
	String interpret(String property);

	/**
	 * 
	 * @param alwaysReload boolean to determine you should always relaod
	 */
	void setAlwaysReload(boolean alwaysReload);

	void reLoad();


	/**
	 * Retrieves a configuration property as a String object.
	 * <p/>
	 * Loads the file if not already initialized.
	 * 
	 * @param key Key Name of the property to be returned.
	 * 
	 * @return Value of the property as a string or null if no property found.
	 */

	String getProperty(String key);

	/**
	 * 
	 * @param key the key of the property
	 * @return Text.split(getProperty(key))
	 */
	String[] getPropertyStrings(String key);

	/**
	 * Multiple properties separated by white spaces
	 * 
	 * @param aClass the class name
	 * @param key the key
	 * @return string property values for class.name.key
	 */
	String[] getPropertyStrings(Class<?> aClass, String key);
	
	/**
	 * Multiple properties separated by white spaces
	 * 
	 * @param aClass
	 * @param key Name of the property to be returned.
	 * @param aDefault the default value
	 * @return Text.split(getProperty(aClass,key,aDefault))
	 */
	String[] getPropertyStrings(Class<?> aClass, String key, String aDefault);

	/**
	 * Get the property 
	 * @param aClass the class associate with property
	 * @param key the property key
	 * @param resourceBundle the resource bundle default used if property not found
	 * @return the property key
	 */
	String getProperty(Class<?> aClass, String key, ResourceBundle resourceBundle);

	/**
	 * Multiple properties separated by white spaces
	 * 
	 * @param aClass
	 * @param key Name of the property to be returned.
	 * @param aDefault the default value
	 * @return Text.split(getProperty(aClass,key,aDefault))
	 */
	String[] getPropertyStrings(Class<?> aClass, String key, String... aDefault);
	
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

	String getProperty(Class<?> aClass, String key);
	
	/**
	 * Retrieves a configuration property as a String object.
	 * <p/>
	 * Loads the file if not already initialized.
	 * 
	 * @param aClass the class name prefix for the property
	 * @param key Key Name of the property to be returned.
	 * @param aDefault the default value
	 * 
	 * @return Value of the property as a string or null if no property found.
	 */

	String getProperty(Class<?> aClass, String key, String aDefault);
	
	/**
	 * Retrieves a configuration property as a String object.
	 * <p/>
	 * Loads the file if not already initialized.
	 * 
	 * @param key the Key Name of the property to be returned.
	 * @param defaultValue the default value to use
	 * 
	 * @return Value of the property as a string or null if no property found.
	 */

	String getProperty(String key, String defaultValue);

	/**
	 * Get a configuration property as an Integer object.
	 * 
	 * @param aClass calling class
	 * @param key Key Name of the numeric property to be returned.
	 * @param defaultValue the default value
	 * @return Value of the property as an Integer or null if no property found.
	 */

	Integer getPropertyInteger(Class<?> aClass, String key, int defaultValue);

	/**
	 * Get a configuration property as an c object.
	 * 
	 * @param aClass the class the property is related to
	 * @param key the configuration name
	 * @param defaultValue the default value to return if the property does not
	 *            exist
	 * @return the configuration character
	 */
	Character getPropertyCharacter(Class<?> aClass, String key, char defaultValue);
	// ---------------------------------------------

	/**
	 * Get a configuration property as an Integer object.
	 * 
	 * @param key the Key Name of the numeric property to be returned.
	 * 
	 * @return Value of the property as an Integer or null if no property found.
	 */

	Integer getPropertyInteger(String key);

	Integer getPropertyInteger(String key, int aDefault);

	/**
	 * Get a double property
	 * @param cls the class associated with the property
	 * @param key the property key name
	 * @return the double property value
	 */
	Double getPropertyDouble(Class<?> cls, String key);
	
	/**
	 * Get a double property
	 * @param aClass the class associated with the property
	 * @param key the property key name
	 * @param defaultValue the default double property
	 * @return the double property value
	 */
	Double getPropertyDouble(Class<?> aClass, String key, double defaultValue);

	/**
	 * 
	 * @param key the double key
	 * @return the Double property
	 */
	Double getPropertyDouble(String key);

	Double getPropertyDouble(String key, double aDefault);

	Double getPropertyDouble(String key, Double aDefault);

	Integer getPropertyInteger(Class<?> cls, String key);
	
	Integer getPropertyInteger(Class<?> cls, String key, Integer aDefault);
	
	Integer getPropertyInteger(String key, Integer aDefault);

	/**
	 * Get a configuration property as a Boolean object.
	 * 
	 * @param key Key Name of the numeric property to be returned.
	 * 
	 * @return Value of the property as an Boolean or null if no property found.
	 *         Note that the value of the returned Boolean will be false if the
	 *         property sought after exists but is not equal to "true" (ignoring
	 *         case).
	 */

	Boolean getPropertyBoolean(String key);

	Boolean getPropertyBoolean(String key, Boolean aBool);

	/**
	 * @param aClass the class name prefix
	 * @param key the configuration key
	 * @param defaultValue the default value
	 * @return aBool if the configuration value for the key is blank
	 */
	Boolean getPropertyBoolean(Class<?> aClass, String key, boolean defaultValue);
	
	/**
	 * @param key the configuration key
	 * @param aBool default value
	 * 
	 * @return aBool if the configuration value for the key is blank
	 */

	Boolean getPropertyBoolean(String key, boolean aBool);

	Long getPropertyLong(String key);

	Long getPropertyLong(Class<?> aClass, String key, long aDefault);

	Long getPropertyLong(Class<?> aClass, String key);

	Long getPropertyLong(String key, long aDefault);

	Long getPropertyLong(String key, Long aDefault);

	/**
	 * Get a configuration property as a Password object.
	 * 
	 * @param key Key Name of the numeric property to be returned.
	 * 
	 * @return Value of the property as an Password or null if no property
	 *         found.

	 *         Note that the value of the returned Password will be false if the
	 *         property sought after exists but is not equal to "true" (ignoring
	 *         case).
	 */

	char[] getPropertyPassword(String key);

	/**
	 * Get the an encrypted password
	 * 
	 * @param key the key
	 * @param defaultPassword
	 * @return the default password if no password exists in the configuration
	 */
	char[] getPropertyPassword(String key, char[] defaultPassword);

	/**
	 * Get the an encrypted password
	 * 
	 * @param key the key
	 * @param defaultPassword the default password
	 * @return the default password if no password exists in the configuration
	 */
	char[] getPropertyPassword(String key, String defaultPassword);
	
	/**
	 * @param aClass the default class prefix
	 * @param key the configuration key
	 * @param defaultPassword default value
	 * 
	 * @return defaultPassword if the configuration value for the key is blank
	 */
	char[] getPropertyPassword(Class<?> aClass, String key, char[] defaultPassword);
	
	/**
	 * @return a copy of the configured properties
	 */

	Map<Object,Object> getProperties();
	
	/**
	 * Set the setting properties
	 * @param properties the properties
	 */
	void setProperties(Map<Object,Object> properties);
	
	/**
	 * 
	 * @return System.getProperty("user.dir")
	 */
	default String getUserDir()
	{
		return System.getProperty("user.dir");
	}

	/**
	 * 
	 * @param settingsObserver the setting observer
	 */
	void registerObserver(SubjectObserver<Settings> settingsObserver);

	/**
	 * 
	 * @param args the arguments to load
	 */
	default Settings loadArgs(String[] args)
	{
		if(args != null && args.length> 0){
			loadArgs(Arrays.asList(args));
		}

		return this;
	}

	/**
	 * 
	 * @param arguments the arguments to load
	 */
	void loadArgs(List<String> arguments);

	default <T> Class<T> getPropertyClass(String propertyKey)
	{
		return (Class)ClassPath.toClass(getProperty(propertyKey));
	}

	default <T> Class<T> getPropertyClass(String propertyKey,Class<T> defaultClass)
	{
		return (Class)ClassPath.toClass(getProperty(propertyKey,defaultClass.getName()));
	}
}