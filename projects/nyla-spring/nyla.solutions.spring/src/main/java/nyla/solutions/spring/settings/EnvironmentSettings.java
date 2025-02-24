package nyla.solutions.spring.settings;

import java.util.Map;

import org.springframework.core.env.Environment;

import nyla.solutions.core.util.settings.AbstractSettings;

/**
 * @author Gregory Green
 *
 */
public class EnvironmentSettings extends AbstractSettings
{
	public EnvironmentSettings(Environment environment)
	{
		if (environment == null)
			throw new IllegalArgumentException("environment is required");

		
		this.environment = environment;
		
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.settings.Settings#getLocation()
	 */
	@Override
	public String getLocation()
	{
		return environment.toString();
	}
	
	@Override
	public String getRawProperty(String key)
	{
		return  this.environment.getProperty(key);
		
	}//------------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.settings.Settings#reLoad()
	 */
	@Override
	public void reLoad()
	{

	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.settings.Settings#getProperties()
	 */
	@Override
	public Map<Object, Object> getProperties()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see nyla.solutions.core.util.settings.Settings#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<Object, Object> properties)
	{
	}

	private final Environment environment;
}
