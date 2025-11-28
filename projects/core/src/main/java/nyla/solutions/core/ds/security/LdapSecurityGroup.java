package nyla.solutions.core.ds.security;

import nyla.solutions.core.security.data.SecurityGroup;

import java.security.Principal;

public class LdapSecurityGroup extends SecurityGroup
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9061501642280480803L;

	public LdapSecurityGroup(String dn)
	{
		this(dn,"CN");
	}// --------------------------------------------------------------
	public LdapSecurityGroup(String dn, String attributeName)
	{
		super(dn);
		
		dn = this.getName();
		
		if(attributeName == null)
			this.primaryLdapGroupName = dn;
		else
		{
	
			String startsWith = new StringBuilder(attributeName).append("=").toString();
			
			if(!dn.startsWith(startsWith))
			{
				//try uppercase DN
				String upperCaseStartsWith = startsWith.toUpperCase();
				if(dn.startsWith(upperCaseStartsWith))
					startsWith = upperCaseStartsWith;
			}

			String results = nyla.solutions.core.util.Text.parser().parseText(dn, startsWith, ",");
			
			if(results == null || results.length() == 0)
				results = dn;
			else
				results = results.toUpperCase();
			
			this.primaryLdapGroupName = results;

		}
	
			
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
				+  (primaryLdapGroupName == null ? 0 :
					primaryLdapGroupName.hashCode());
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
		if (super.equals(obj))
			return true;
		
		if (!SecurityGroup.class.isAssignableFrom(obj.getClass()))
			return false;
		SecurityGroup other = (SecurityGroup) obj;
		
		String otherGroupName = other.getName();
		if(otherGroupName == null)
			return false;
		
		if (primaryLdapGroupName == null)
			return false;
		
		return primaryLdapGroupName.equals(otherGroupName);
	}

	@Override
	public Boolean apply(Principal obj)
	{
		if (this == obj)
			return true;
		if (super.equals(obj))
			return true;
		
		if (!SecurityGroup.class.isAssignableFrom(obj.getClass()))
			return false;
		SecurityGroup other = (SecurityGroup) obj;
		
		String otherGroupName = other.getName();
		if(otherGroupName == null)
			return false;
		
		if (primaryLdapGroupName == null)
			return false;
		
		return primaryLdapGroupName.equals(otherGroupName);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("LdapSecurityGroup [primaryLdapGroupName=")
				.append(this.primaryLdapGroupName).append(", getName()=")
				.append(getName()).append("]");
		return builder.toString();
	}
	

	/**
	 * @return the primaryLdapGroupName
	 */
	public String getPrimaryLdapGroupName()
	{
		return primaryLdapGroupName;
	}


	private final String primaryLdapGroupName;
}
