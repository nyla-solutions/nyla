package nyla.solutions.commas;

import nyla.solutions.core.security.SecuredToken;

/**
 * Unique identifier for a set a container of commands
 * @author Gregory Green
 *
 */
public class ShellIdentifier
{
	/**
	 * 
	 * @param crateName
	 * @param securedToken
	 */
	ShellIdentifier(String shellName, SecuredToken securedToken)
	{
		super();
		this.shellName = shellName;
		this.securedToken = securedToken;
	}// -----------------------------------------------
	/**
	 * @return the shellName
	 */
	protected String getShellName()
	{
		return shellName;
	}
	/**
	 * @param shellName the shellName to set
	 */
	protected void setShellName(String shellName)
	{
		this.shellName = shellName;
	}
	/**
	 * @return the securedToken
	 */
	protected SecuredToken getSecuredToken()
	{
		return securedToken;
	}
	/**
	 * @param securedToken the securedToken to set
	 */
	protected void setSecuredToken(SecuredToken securedToken)
	{
		this.securedToken = securedToken;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((shellName == null) ? 0 : shellName.hashCode());
		result = prime * result
				+ ((securedToken == null) ? 0 : securedToken.hashCode());
		return result;
	}// --------------------------------------------------------
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
		ShellIdentifier other = (ShellIdentifier) obj;
		if (shellName == null)
		{
			if (other.shellName != null)
				return false;
		}
		else if (!shellName.equals(other.shellName))
			return false;
		if (securedToken == null)
		{
			if (other.securedToken != null)
				return false;
		}
		else if (!securedToken.equals(other.securedToken))
			return false;
		return true;
	}

	private String shellName;
	private SecuredToken securedToken;
}
