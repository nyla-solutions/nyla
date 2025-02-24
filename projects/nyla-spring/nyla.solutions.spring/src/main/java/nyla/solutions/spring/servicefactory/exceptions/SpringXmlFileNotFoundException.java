package nyla.solutions.spring.servicefactory.exceptions;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.spring.servicefactory.SpringFactory;

/**
 * @author Gregory Green
 *
 */
public class SpringXmlFileNotFoundException extends ConfigException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8331624559019768642L;

	/**
	 * 
	 */
	public SpringXmlFileNotFoundException()
	{
		this("Unable to location the service.factory.xml in classpath and property "+SpringFactory.DEFAULT_CONFIG_PROP_VALUE+
				"\n not found in the confir properties file. Either the location of the file in the "+
				"\n Sample system property;"
				+"\n -Dspring.xml.url=file:///projects/solutions/config/servicefactory.xml"
				+"\n"
				+"\n Sample Configuration Property"
				+"\n spring.xml.url=file:///C:/Projects/solution/runtime/config/service.factory.xml");
	}

	/**
	 * @param aMessage
	 */
	public SpringXmlFileNotFoundException(String aMessage)
	{
		super(aMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aMessage
	 * @param aThrowable
	 */
	public SpringXmlFileNotFoundException(String aMessage,
			Throwable aThrowable)
	{
		super(aMessage, aThrowable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aThrowable
	 */
	public SpringXmlFileNotFoundException(Throwable aThrowable)
	{
		super(aThrowable);
		// TODO Auto-generated constructor stub
	}

}
