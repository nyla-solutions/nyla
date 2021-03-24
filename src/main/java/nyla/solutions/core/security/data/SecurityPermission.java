package nyla.solutions.core.security.data;

import java.io.Serializable;

/**
 * <pre>
 * SecurityPermission is a value object representation of 
 * the Permission table.
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class SecurityPermission
implements Permission, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5321184181505071682L;

	/**
	 * 
	 * Constructor for SecurityPermission initializes internal data settings.
	 * 
	 * @param text
	 *            the permission name
	 */
	public SecurityPermission(String text)
	{
		this.text = text;
	}// --------------------------------------------

	@Override
	public String getText()
	{
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		SecurityPermission other = (SecurityPermission) obj;
		if (text == null)
		{
			if (other.text != null)
				return false;
		}
		else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public boolean isAuthorized(Permission other)
	{

		if(other == null)
			return false;
		
		String otherText = other.getText();
		
		if (text == null)
		{
			if (otherText != null)
				return false;
		}
		else if (!this.text.equals(otherText))
			return false;
		return true;
	}

	private final String text;
}