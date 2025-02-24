package nyla.solutions.core.security.data;

import java.io.Serializable;

/**
 * Permission that returns if the permission is exists 
 * in the prevented permission
 * @author Gregory Green
 *
 */
public class SecurityPermissionContains implements Permission, Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7042157206407846236L;



	public SecurityPermissionContains(String text)
	{
		if (text == null || text.length() == 0)
			throw new IllegalArgumentException("text is required");
		
		this.text = text;
	}
	

	/* (non-Javadoc)
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


	/* (non-Javadoc)
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
		SecurityPermissionContains other = (SecurityPermissionContains) obj;
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
	public boolean isAuthorized(Permission permission)
	{
		if(permission == null)
			return false;
		
		String txtPermission = permission.getText();
		if(txtPermission == null || txtPermission.length() == 0)
			return false;
		
		return this.text.contains(txtPermission) || txtPermission.contains(this.text) ;
	}

	
	@Override
	public String getText()
	{
		return text;
	}


	private final String text;

}
