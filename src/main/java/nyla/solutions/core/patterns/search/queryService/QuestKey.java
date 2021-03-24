package nyla.solutions.core.patterns.search.queryService;

import nyla.solutions.core.data.Mappable;

import java.util.Arrays;

/**
 * Key name and value pairs for inputs for a quest query service search
 * @author Gregory Green
 *
 */
public class QuestKey implements Mappable<String, String>
{
	public QuestKey()
	{}
	// --------------------------------------------------------
	public QuestKey(String key, String value, String type)
	{
		super();
		this.key = key;
		this.value = value;
		this.type = type;
	}
	// --------------------------------------------------------
	public QuestKey(String key, String value, String format, String type,
			String[] hints)
	{
		super();
		this.key = key;
		this.value = value;
		this.format = format;
		this.type = type;
		
		if(hints == null)
			this.hints = null;
		else
			this.hints = hints.clone();
	}
	// --------------------------------------------------------
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + Arrays.hashCode(hints);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	// --------------------------------------------------------
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestKey other = (QuestKey) obj;
		if (format == null)
		{
			if (other.format != null)
				return false;
		}
		else if (!format.equals(other.format))
			return false;
		if (!Arrays.equals(hints, other.hints))
			return false;
		if (key == null)
		{
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		if (type == null)
		{
			if (other.type != null)
				return false;
		}
		else if (!type.equals(other.type))
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}
	// --------------------------------------------------------
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "QuestKey [key=" + key + ", value=" + value + ", format="
				+ format + ", type=" + type + ", hints="
				+ Arrays.toString(hints) + "]";
	}// --------------------------------------------------------
	/**
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}// --------------------------------------------------------
	/**
	 * @param key the key to set
	 */
	public void setKey(String key)
	{
		this.key = key;
	}// --------------------------------------------------------
	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}// --------------------------------------------------------
	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}// --------------------------------------------------------
	/**
	 * @return the format
	 */
	public String getFormat()
	{
		return format;
	}// --------------------------------------------------------
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}// --------------------------------------------------------
	/**
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}// --------------------------------------------------------
	/**
	 * @return the hints
	 */
	public String[] getHints()
	{
		if(hints == null)
			return null;
		
		return hints.clone();
	}// --------------------------------------------------------
	/**
	 * @param hints the hints to set
	 */
	public void setHints(String[] hints)
	{
		if(hints == null)
			this.hints = null;
		else
			this.hints = hints.clone();
	}// --------------------------------------------------------
	private String key;
	private String value;
	private String format;
	private String type;
	private String[] hints;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5493751631649168641L;

}
