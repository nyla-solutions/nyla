package nyla.solutions.core.security.data;

import java.io.Serializable;
import java.security.Principal;

/**
 * <pre>
 * ServiceClient implements for SecurityCredential
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class SecurityClient implements SecurityCredential, Serializable, Principal
{
	/**
	 * Constructor for ServiceClient initializes internal data settings.
	 * 
	 */
	public SecurityClient()
	{
	}// --------------------------------------------

	/**
	 * Constructor for ServiceClient initializes internal data settings.
	 * 
	 * @param loginID
	 * @param id
	 */
	public SecurityClient(String loginID, Integer id)
	{
		this.loginID = loginID;
		this.id = id;
	}// --------------------------------------------

	/**
	 * Constructor for ServiceClient initializes internal data settings.
	 * 
	 * @param loginID
	 */
	public SecurityClient(String loginID)
	{
		this.loginID = loginID;
	}// --------------------------------------------

	/**
	 * Implement the principal interface
	 * 
	 * @return the loginID
	 */
	public String getName()
	{
		return loginID;
	}// ------------------------------------------------

	/**
	 * Constructor for ServiceClient initializes internal data settings.
	 * 
	 * @param id
	 */
	public SecurityClient(Integer id)
	{
		this.id = id;
	}// --------------------------------------------

	/**
	 * Constructor for ServiceClient initializes internal data settings.
	 * 
	 * @param id
	 */
	public SecurityClient(int id)
	{
		this.id = Integer.valueOf(id);
	}// --------------------------------------------

	/**
	 * @return Returns the id.
	 */
	public String getId()
	{
		return id.toString();
	}// --------------------------------------------

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}// --------------------------------------------

	public void setId(String id)
	{
		if (id == null || id.length() == 0)
			throw new IllegalArgumentException(
			"id required in setId");

		this.setId(Integer.valueOf(id));
	}// --------------------------------------------

	/**
	 * @return Returns the loginID.
	 */
	public String getLoginID()
	{
		return loginID;
	}// --------------------------------------------

	/**
	 * @param loginID
	 *            The loginID to set.
	 */
	public void setLoginID(String loginID)
	{
		if (loginID == null)
			loginID = "";

		this.loginID = loginID;
	}// --------------------------------------------

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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((loginID == null) ? 0 : loginID.hashCode());
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
		SecurityClient other = (SecurityClient) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (loginID == null)
		{
			if (other.loginID != null)
				return false;
		}
		else if (!loginID.equals(other.loginID))
			return false;
		return true;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SecurityClient [loginID=").append(loginID).append(", id=").append(id).append("]");
		return builder.toString();
	}


	private String loginID = null;
	private Integer id = null;
	static final long serialVersionUID = SecurityClient.class.getName()
	.hashCode();

}
