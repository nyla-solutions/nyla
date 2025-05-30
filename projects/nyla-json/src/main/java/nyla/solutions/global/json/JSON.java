package nyla.solutions.global.json;

import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.util.Config;

/**
 * JavaScript Object Notation utility interface
 * @author Gregory Green
 *
 */
public abstract class JSON
{	
	/**
	 * Default value: "MM/dd/yyyy HH:mm:ss:SSS"
	 * Override with configuration property 
	 */
	public static final String DATE_FORMAT = Config.settings().getProperty(JSON.class,"DATE_FORMAT","MM/dd/yyyy HH:mm:ss:SSS");
			
	/**
	 * Convert from an object to JSON string
	 * @param src the source object to convert
	 * @return the JSON 
	 */
	 public abstract String toJson(Object src);

	 /**
	  * 
	 * Convert from an object to JSON string
	 * @param src the source object to convert
	  * @param classType the class of the src
	 * @return the JSON 
	  */
	public abstract String toJson(Object src, Class<?> classType);
	 
	 /**
	  * Convert from a json to an object
	 * @param <T> the TYPE
	  * @param json the JSON text to convert
	  * @param classOfT the 
	  * @return instance object
	  */
	public abstract <T> T fromJson(String json, Class<?> classOfT);
	 
	 /**
	  * 
	  * @return a new instance of the JSON implementation
	  */
	 public synchronized static JSON newInstance()
	 {
			try
			{
				return (JSON)Class.forName(instanceClassName).newInstance();
			}
			catch (Exception e)
			{
				throw new SetupException(e);
			}
		
	 }// -----------------------------------------------
	 private static String instanceClassName = Config.settings().getProperty(JSON.class,"instanceClassName","nyla.solutions.global.json.GSON");
	 
}
