package nyla.solutions.core.ds.security;

import nyla.solutions.core.security.data.SecurityUser;

import java.security.Principal;


public class LdapSecurityUser extends SecurityUser
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1090403112093383311L;


	public LdapSecurityUser(String uid)
	{
		this(uid,null);
	}// --------------------------------------------------------------
	public LdapSecurityUser(String uid,String dn)
	{
		super(uid);
		
		this.dn = dn;
			
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("LdapSecurityUser [dn=").append(dn)
				.append(", getName()=").append(getName())
				.append(", getGroups()=").append(getGroups()).append("]");
		return builder.toString();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (super.equals(obj))
			return true;
		if (getClass() != obj.getClass())
			return false;
		LdapSecurityUser other = (LdapSecurityUser) obj;
		if (dn == null)
		{
			if (other.dn != null)
				return false;
		}
		else if (!dn.equals(other.dn))
			return false;
		return true;
	}

	@Override
	public Boolean apply(Principal principal)
	{
		if (this == principal)
			return Boolean.TRUE;
		
		if (Boolean.TRUE.equals(super.apply(principal)))
			return Boolean.TRUE;
		
		if (getClass() != principal.getClass())
			return false;
		LdapSecurityUser other = (LdapSecurityUser) principal;
		if (dn == null || other.dn == null)
		{
				return false;
		}

		return dn.equals(other.dn);
	}

	private final String dn;
}
