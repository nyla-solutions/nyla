package nyla.solutions.core.util.settings;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.MissingConfigPropertiesException;
import nyla.solutions.core.patterns.decorator.Styles;
import nyla.solutions.core.util.Cryption;
import nyla.solutions.core.util.Text;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;



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

public abstract class AbstractSettings implements Settings
{

	public static final String RESOURCE_BUNDLE_NAME = "config";

	/**
	 * SMP_PROPERTY_FILE
	 */
	public static final String SYS_PROPERTY = "config.properties";

	public static final String DEFAULT_PROP_FILE_NAME = SYS_PROPERTY;
	
	private boolean alwaysReload = false;
	private boolean useFormatting = false;


	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#interpret(java.lang.String)
	 */
	@Override
	public String interpret(String property)
	{
		try
		{
			property = Cryption.interpret(property);
			
			if(property != null && property.indexOf(Styles.DEFAULT_PREFIX) > -1)
			{
				property = Text.format(property,this.getProperties());
			}
		}
		catch (FormatException e)
		{
			throw new ConfigException("Format exception for \""+property+"\"",e);
		}
		
		return Cryption.interpret(property);
	}
	
	

	/**
	 * @return resource bundle name
	 */

	String getBundleName()
	{
		return RESOURCE_BUNDLE_NAME;

	}

	
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#setAlwaysReload(boolean)
	 */
	@Override
	public void setAlwaysReload(boolean alwaysReload)
	{
		this.alwaysReload = alwaysReload;
	}
	

	
	/**
	 * @return the alwaysReload
	 */
	public boolean isAlwaysReload()
	{
		return alwaysReload;
	}


	/**
	 * @return  getProperty(key,true);
	 */
	@Override
	public String getProperty(String key)
	{
		return getProperty(key,true,false);
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getProperty(java.lang.String)
	 */	
	public String getProperty(String key,boolean throwRequiredException)
	{
		return getProperty(key,throwRequiredException,false);
		
	}
	public String getRawProperty(String key)
	{
		Map<Object,Object> properties = this.getProperties();
		
		return (String)properties.get(key);
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getProperty(java.lang.String)
	 */	
	public String getProperty(String key,boolean throwRequiredException, boolean checkSecured)
	{

		String retval = this.getRawProperty(key);

		if (retval == null || retval.length() == 0)
		{
			if(!throwRequiredException)
				return null;
			
			String configSourceLocation = this.getLocation();
			
			if (configSourceLocation == null)
				throw new MissingConfigPropertiesException(key);

			
			throw new ConfigException("Configuration property \"" + key
					+ "\" not found in environment variable, system properties or keys " + " file:" + configSourceLocation+" keys:"+getProperties().keySet());
		}

		
		if(checkSecured)
		{
			if (!retval.startsWith(Cryption.CRYPTION_PREFIX))
			{
				throw new ConfigException("Configuration key \"" + key
						+ "\" must be encypted");
			}
		}
		
		return Cryption.interpret(retval);

	}
	public Set<Object> keySet()
	{
		Map<Object,Object> properties = this.getProperties();
		if(properties == null)
			return null;
		
		return properties.keySet();
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyStrings(java.lang.String)
	 */
	@Override
	public String[] getPropertyStrings(String key)
	{
		return Text.split(getProperty(key));
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyStrings(java.lang.Class, java.lang.String)
	 */
	@Override
	public String[] getPropertyStrings(Class<?> aClass, String key)
	{
		return Text.split(getProperty(aClass, key));
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyStrings(java.lang.Class, java.lang.String, java.lang.String)
	 */
	@Override
	public String[] getPropertyStrings(Class<?> aClass, String key,
			String aDefault)
	{
		String[] defaultArray = {aDefault};
		
		return getPropertyStrings(aClass, key, defaultArray);
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getProperty(java.lang.Class, java.lang.String, java.util.ResourceBundle)
	 */
	@Override
	public String getProperty(Class<?> aClass,String key,ResourceBundle resourceBundle)
	{
		String results = getProperty(aClass,key, "");
		
		if(results == null || results.length() == 0)
			results = resourceBundle.getString(key);
		
		return results;
	}
	
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyStrings(java.lang.Class, java.lang.String, java.lang.String[])
	 */
	@Override
	public String[] getPropertyStrings(Class<?> aClass, String key,
			String... aDefault)
	{
		String property = getProperty(aClass, key,"");
		
		if("".equals(property))
			return aDefault;
		
		return Text.split(property);
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getProperty(java.lang.Class, java.lang.String)
	 */

	@Override
	public String getProperty(Class<?> aClass, String key)
	{
		return getProperty(new StringBuilder(aClass.getName()).append(".").append(key).toString());
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getProperty(java.lang.Class, java.lang.String, java.lang.String)
	 */

	@Override
	public String getProperty(Class<?> aClass, String key, String aDefault)
	{
		return getProperty(new StringBuilder(aClass.getName()).append(".").append(key).toString(), aDefault);

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getProperty(java.lang.String, java.lang.String)
	 */

	@Override
	public String getProperty(String key, String aDefault)
	{
		String retval = null;
		
		
		retval = getProperty(key,false);
		if (retval == null || retval.length() == 0)
		{
			retval = aDefault;
		}

		return Cryption.interpret(retval);

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyInteger(java.lang.Class, java.lang.String, int)
	 */

	@Override
	public Integer getPropertyInteger(Class<?> aClass, String key,
			int defaultValue)
	{
		return getPropertyInteger(new StringBuilder(aClass.getName()).append(".")
				.append(key).toString(), defaultValue);
	}
	


	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyCharacter(java.lang.Class, java.lang.String, char)
	 */
	@Override
	public Character getPropertyCharacter(Class<?> aClass, String key,
			char defaultValue)
	{
		String results = getProperty(aClass, key, "");

		if (results.length() == 0)
			return Character.valueOf(defaultValue);
		else
			return Character.valueOf(results.charAt(0));// return first character

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyInteger(java.lang.String)
	 */

	@Override
	public Integer getPropertyInteger(String key)
	{
		Integer iVal = null;
		String sVal = getProperty(key);

		if ((sVal != null) && (sVal.length() > 0))
		{

			iVal = Integer.valueOf(sVal);

		}
		return iVal;

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyInteger(java.lang.String, int)
	 */
	@Override
	public Integer getPropertyInteger(String key, int aDefault)
	{

		return getPropertyInteger(key, Integer.valueOf(aDefault));

	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyDouble(java.lang.Class, java.lang.String)
	 */
	@Override
	public Double getPropertyDouble(Class<?> cls, String key)
	{
		return getPropertyDouble(new StringBuilder(cls.getName()).append(".").append(key).toString());

	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyDouble(java.lang.Class, java.lang.String, double)
	 */	
	@Override
	public Double getPropertyDouble(Class<?> aClass, String key,
			double defaultValue)
	{
		return getPropertyDouble(new StringBuilder(aClass.getName()).append(".")
				.append(key).toString(), defaultValue);
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyDouble(java.lang.String)
	 */
	@Override
	public Double getPropertyDouble(String key)
	{
		Double iVal = null;
		String sVal = getProperty(key);

		if ((sVal != null) && (sVal.length() > 0))
		{

			iVal = Double.valueOf(sVal);

		}
		return iVal;

	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyDouble(java.lang.String, double)
	 */
	@Override
	public Double getPropertyDouble(String key, double aDefault)
	{

		return getPropertyDouble(key, Double.valueOf(aDefault));

	}
	
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyDouble(java.lang.String, java.lang.Double)
	 */
	@Override
	public Double getPropertyDouble(String key, Double aDefault)
	{
		Double iVal = null;
		
		String sVal = getProperty(key,false);
		if ((sVal != null) && (sVal.length() > 0))
		{
			iVal = Double.valueOf(sVal);
		}
		else
		{
			iVal = aDefault;
		}
		return iVal;
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyInteger(java.lang.Class, java.lang.String)
	 */
	@Override
	public Integer getPropertyInteger(Class<?> cls, String key)
	{
		return getPropertyInteger(new StringBuilder(cls.getName()).append(".").append(key).toString());

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyInteger(java.lang.Class, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getPropertyInteger(Class<?> cls, String key,
			Integer aDefault)
	{
		return getPropertyInteger(new StringBuilder(cls.getName()).append(".").append(key).toString(), aDefault);

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyInteger(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getPropertyInteger(String key, Integer aDefault)
	{
		Integer iVal = null;
		
		String sVal = getProperty(key,false);
		if ((sVal != null) && (sVal.length() > 0))
		{
			iVal = Integer.valueOf(sVal);
		}
		else
		{
			iVal = aDefault;
		}
		return iVal;
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyBoolean(java.lang.String)
	 */

	@Override
	public Boolean getPropertyBoolean(String key)
	{
		Boolean bVal = null;
		String sVal = getProperty(key);
		if ((sVal != null) && (sVal.length() > 0))
		{
			bVal = Boolean.valueOf(sVal);
		}
		return bVal;
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyBoolean(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Boolean getPropertyBoolean(String key, Boolean aBool)
	{
		Boolean bVal = null;
		
		
		String sVal = getProperty(key,false);
		if ((sVal != null) && (sVal.length() > 0))
		{
			bVal = Boolean.valueOf(sVal);
		}
		else
		{
			bVal = aBool;
		}
		return bVal;

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyBoolean(java.lang.Class, java.lang.String, boolean)
	 */

	@Override
	public Boolean getPropertyBoolean(Class<?> aClass, String key,
			boolean aBool)
	{
		return getPropertyBoolean(new StringBuilder(aClass.getName()).append(".").append(key).toString(), aBool);
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyBoolean(java.lang.String, boolean)
	 */

	@Override
	public Boolean getPropertyBoolean(String key, boolean aBool)
	{
		Boolean bVal = null;
		
		
		
		String sVal = getProperty(key,false);
		if ((sVal != null) && (sVal.length() > 0))
		{
			bVal =  Boolean.valueOf(sVal);
		}
		else
		{
			bVal = Boolean.valueOf(aBool);
		}
		return bVal;

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyLong(java.lang.String)
	 */
	@Override
	public Long getPropertyLong(String key)
	{
		Long longValue = null;
		String sVal = getProperty(key);
		if ((sVal != null) && (sVal.length() > 0))
		{
			longValue = Long.valueOf(sVal);
		}
		return longValue;
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyLong(java.lang.Class, java.lang.String, long)
	 */
	@Override
	public Long getPropertyLong(Class<?> aClass, String key,  long aDefault)
	{
		return getPropertyLong(new StringBuilder(aClass.getName()).append(".").append(key).toString(), Long.valueOf(aDefault));
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyLong(java.lang.Class, java.lang.String)
	 */
	@Override
	public Long getPropertyLong(Class<?> aClass, String key)
	{
		return getPropertyLong(new StringBuilder(aClass.getName()).append(".").append(key).toString());
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyLong(java.lang.String, long)
	 */
	@Override
	public Long getPropertyLong(String key, long aDefault)
	{
		return getPropertyLong(key, Long.valueOf(aDefault));
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyLong(java.lang.String, java.lang.Long)
	 */
	@Override
	public Long getPropertyLong(String key, Long aDefault)
	{
		Long longValue = null;
		
		String sVal = getProperty(key,false);
		if ((sVal != null) && (sVal.length() > 0))
		{
			longValue = Long.valueOf(sVal);
		}
		else
		{
			longValue = aDefault;
		}
		return longValue;

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyPassword(java.lang.String)
	 */

	@Override
	public char[] getPropertyPassword(String key)
	{
		char[] bVal = null;
		String sVal = getSecureProperty(key);

		if(sVal == null)
			throw new MissingConfigPropertiesException(key);
		
		bVal = sVal.toCharArray();

		return bVal;
	}


	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyPassword(java.lang.String, char[])
	 */
	@Override
	public char[] getPropertyPassword(String key, char[] defaultPassword)
	{
		char[] bVal = null;
		String sVal = getSecureProperty(key);
		if ((sVal != null) && (sVal.length() > 0))
		{
			bVal = sVal.toCharArray();
		}
		else
		{
			bVal = defaultPassword;
		}
		return bVal;

	}

	@Override
	public char[] getPropertyPassword(Class<?> callerClass, String key) {
		var newKey = new StringBuilder(callerClass.getName()).append(".").append(key).toString();

		return getPropertyPassword(newKey);
	}


	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyPassword(java.lang.String, java.lang.String)
	 */
	@Override
	public char[] getPropertyPassword(String key, String defaultPassword)
	{
		char[] bVal = null;
		String sVal = getSecureProperty(key);
		if ((sVal != null) && (sVal.length() > 0))
		{
			bVal = sVal.toCharArray();
		}
		else
		{
			if (defaultPassword == null)
				return null;

			bVal = defaultPassword.toCharArray();
		}
		return bVal;

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#getPropertyPassword(java.lang.Class, java.lang.String, char[])
	 */

	@Override
	public char[] getPropertyPassword(Class<?> aClass, String key,
			char[] defaultPassword)
	{
		return getPropertyPassword(new StringBuilder(aClass.getName()).append(".").append(key).toString(),
				defaultPassword);
	}

	/**
	 * Retrieves a configuration property as a encrypted value.
	 * <p/>
	 * Loads the file if not already initialized.
	 * @param key Name of the property to be returned.
	 * @return Value of the property as a string or null if no property found.
	 */

	protected String getSecureProperty(String key)
	{
		return getProperty(key,false,true);
	}



	/**
	 * 
	 * @return System.getProperty("file.separator")
	 */
	public String getFileSeparator()
	{
		return System.getProperty("file.separator");
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.Settings#isUseFormatting()
	 */
	@Override
	public boolean isUseFormatting()
	{
		return useFormatting;
	}

	@Override
	public void loadArgs(List<String> arguments)
	{		
		if(arguments == null || arguments.isEmpty())
			return;
		
		this.setProperties(ArgsParser.parse(arguments));
	}


}
