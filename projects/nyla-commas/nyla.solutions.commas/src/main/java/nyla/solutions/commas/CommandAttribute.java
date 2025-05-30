package nyla.solutions.commas;

import java.io.Serializable;

public class CommandAttribute implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5059199875213793671L;
	public CommandAttribute(String name, String type, String value)
	{
		super();
		this.name = name;
		this.type = type;
		this.value = value;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}
	
	private final String name;
	private final String type;
	private final String value;

}
