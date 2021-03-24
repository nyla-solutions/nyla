package nyla.solutions.core.data.clock;

import nyla.solutions.core.data.Identifier;
import nyla.solutions.core.data.Mappable;
import nyla.solutions.core.data.Nameable;
import nyla.solutions.core.data.Textable;

import java.io.Serializable;

/**
 * A named timing event 
 * 
 * 
 * @param <ValueType> the value type
 * @param <KeyType> the type
 * 
 * @author Gregory Green
 * 
 */
public class TimingAuditMappable<KeyType,ValueType> implements Serializable, Nameable, Mappable<KeyType,ValueType>, Identifier, Textable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1210199192832986691L;
	
	
	/**
	 * @return the key
	 */
	public KeyType getKey()
	{
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(KeyType key)
	{
		this.key = key;
	}
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the time
	 */
	public Time getTime()
	{
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Time time)
	{
		this.time = time;
	}
	
	/**
	 * @return the value
	 */
	public ValueType getValue()
	{
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(ValueType value)
	{
		this.value = value;
	}
	

	/**
	 * @return the from
	 */
	public String getFrom()
	{
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from)
	{
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public String getTo()
	{
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to)
	{
		this.to = to;
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the system
	 */
	public String getSystem()
	{
		return system;
	}
	/**
	 * @param system the system to set
	 */
	public void setSystem(String system)
	{
		this.system = system;
	}
	
	
	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}
	
	/**
	 * @return the host
	 */
	public String getHost()
	{
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host = host;
	}
	
	
	/**
	 * @return the processId
	 */
	public int getProcessId()
	{
		return processId;
	}
	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(int processId)
	{
		this.processId = processId;
	}
	
	
	/**
	 * @return the operation
	 */
	public String getOperation()
	{
		return operation;
	}
	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
	/**
	 * @return the dataName
	 */
	public String getDataName()
	{
		return dataName;
	}
	/**
	 * @param dataName the dataName to set
	 */
	public void setDataName(String dataName)
	{
		this.dataName = dataName;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TimingAuditMappable [id=").append(id).append(", text=")
				.append(text).append(", key=").append(key).append(", value=")
				.append(value).append(", operation=").append(operation)
				.append(", dataName=").append(dataName).append(", system=")
				.append(system).append(", name=").append(name)
				.append(", from=").append(from).append(", to=").append(to)
				.append(", host=").append(host).append(", processId=")
				.append(processId).append(", time=").append(time).append("]");
		return builder.toString();
	}
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataName == null) ? 0 : dataName.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + processId;
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
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
		TimingAuditMappable<?,?> other = (TimingAuditMappable<?,?>) obj;
		if (dataName == null)
		{
			if (other.dataName != null)
				return false;
		}
		else if (!dataName.equals(other.dataName))
			return false;
		if (from == null)
		{
			if (other.from != null)
				return false;
		}
		else if (!from.equals(other.from))
			return false;
		if (host == null)
		{
			if (other.host != null)
				return false;
		}
		else if (!host.equals(other.host))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (key == null)
		{
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (operation == null)
		{
			if (other.operation != null)
				return false;
		}
		else if (!operation.equals(other.operation))
			return false;
		if (processId != other.processId)
			return false;
		if (system == null)
		{
			if (other.system != null)
				return false;
		}
		else if (!system.equals(other.system))
			return false;
		if (text == null)
		{
			if (other.text != null)
				return false;
		}
		else if (!text.equals(other.text))
			return false;
		if (time == null)
		{
			if (other.time != null)
				return false;
		}
		else if (!time.equals(other.time))
			return false;
		if (to == null)
		{
			if (other.to != null)
				return false;
		}
		else if (!to.equals(other.to))
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


	private String id;
	private String text;
	private KeyType key;
	private ValueType value;
	private String operation;
	private String dataName;
	private String system;
	private String name;
	private String from;
	private String to;
	private String host;
	private int processId;
	private Time time;
}
