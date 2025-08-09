package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.exception.FormatException;

import java.util.Map;

import static nyla.solutions.core.util.Config.settings;

public interface Styles
{
	/**
	 * DEFAULT_PREFIX = Config.getProperty(Styles.class,"templatePrefix", "${")
	 */
	public static final String DEFAULT_PREFIX = settings().getProperty(Styles.class,"templatePrefix", "${");
	
	/**
	 * DEFAULT_SUFFIX = Config.getProperty(Styles.class,"templatePrefix", "}")
	 */
	public static final String DEFAULT_SUFFIX = settings().getProperty(Styles.class,"templatePrefix", "}");
		
			

	/**
	 * Loop thrus map to formats any needed value with data from other
	 * properties.
	 * 
	 * See Text.format(String,Map)
	 * 
	 * @param map the map to format
	 * @throws FormatException
	 */
	public abstract void formatMap(Map<Object,Object> map) throws FormatException;

}