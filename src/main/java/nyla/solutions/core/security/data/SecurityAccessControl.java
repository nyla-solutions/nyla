package nyla.solutions.core.security.data;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 * SecurityAccessControl access control list entry
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class SecurityAccessControl implements Serializable, AccessControl
{
	/**
	 * 
	 * Constructor for SecurityAccessControl initializes internal data settings.
	 * 
	 * @param principal
	 *            the principal that has the security access
	 */
	public SecurityAccessControl(Principal principal)
	{
		this.principal = principal;
	}// --------------------------------------------

	/**
	 * 
	 * Constructor for SecurityAccessControl initializes internal data settings.
	 *
	 */
	public SecurityAccessControl()
	{
		principal = null;
		negative = false;
	}// --------------------------------------------

	public SecurityAccessControl(Principal principal, Permission permission)
	{
		if (principal == null)
			throw new IllegalArgumentException("principal is required");

		if (permission == null)
			throw new IllegalArgumentException("permission is required");

		this.principal = principal;
		this.permissions.add(permission);
	}// ------------------------------------------------

	public SecurityAccessControl(Principal principal, boolean negative, String permission)
	{
		this(principal,permission);
		this.negative = negative;
	}

	public SecurityAccessControl(Principal principal, String permission)
	{
		this(principal, new SecurityPermission(permission));
	}// ------------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#addPermission(nyla.solutions.core.security.data.Permission)
	 */
	@Override
	public boolean addPermission(Permission permission)
	{
		if(permissions.contains(permission))
			return false;
		
		return this.permissions.add(permission);
	}// --------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#addPermissions(java.util.Collection)
	 */
	@Override
	public void addPermissions(Collection<Permission> aPermssions)
	{
		if (aPermssions == null)
			throw new IllegalArgumentException(
			"aPermssions required in SecurityAccessControl");

		// SecurityPermission element = null;
		for (Iterator<Permission> i = aPermssions.iterator(); i.hasNext();)
		{
			addPermission((SecurityPermission) i.next());
		}
	}// --------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#removePermission(nyla.solutions.core.security.data.Permission)
	 */
	@Override
	public boolean removePermission(Permission permission)
	{
		return permissions.remove(permission);
	}// --------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#checkPermission(nyla.solutions.core.security.data.Permission)
	 */
	@Override
	public boolean checkPermission(Permission permission)
	{
		if (permission == null)
			return false;

		boolean authorized = false;
		for (Permission accessPermission : permissions)
		{
			if(accessPermission.isAuthorized(permission))
			{
				authorized = true;
				break;
			}
		}
		
		if(negative)
			return !authorized;
		else
			return authorized;
	}// --------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#getPermissions()
	 */
	@Override
	public synchronized List<Permission> getPermissions()
	{
		if (permissions == null || permissions.isEmpty())
			return null;

		return new ArrayList<Permission>(this.permissions);
	}// --------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SecurityAccessControl [principal=").append(principal).append(", permissions=").append(permissions)
		.append(", negative=").append(negative).append("]");
		return builder.toString();
	}
	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#setNegativePermissions()
	 */
	@Override
	public void setNegativePermissions()
	{
		this.negative = true;
	}// --------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#isNegative()
	 */
	@Override
	public boolean isNegative()
	{
		return negative;
	}// --------------------------------------------

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#getPrincipal()
	 */
	@Override
	public Principal getPrincipal()
	{
		return principal;
	}// --------------------------------------------b

	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#setPermissions(java.util.Collection)
	 */
	@Override
	public synchronized void setPermissions(Collection<Permission> aPermissions)
	{
		this.permissions.clear();
		if (aPermissions == null || aPermissions.isEmpty())
			return;

		List<Permission> set =new ArrayList<Permission>(aPermissions);

		this.permissions = set;

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
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		result = prime * result + ((principal == null) ? 0 : principal.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/* (non-Javadoc)
	 * @see nyla.solutions.core.security.data.AccessControl#equals(java.lang.Object)
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
		SecurityAccessControl other = (SecurityAccessControl) obj;
		if (permissions == null)
		{
			if (other.permissions != null)
				return false;
		}
		else if (!permissions.equals(other.permissions))
			return false;
		if (principal == null)
		{
			if (other.principal != null)
				return false;
		}
		else if (!principal.equals(other.principal))
			return false;
		return true;
	}

	/**
	 * @param principal
	 *            the principal to set
	 * @return true if principal edited
	 */
	public boolean setPrincipal(Principal principal)
	{
		this.principal = principal;

		return true;
	}
	
	
	

	private Principal principal;
	private List<Permission> permissions = new ArrayList<Permission>(10);
	private boolean negative = false;
	static final long serialVersionUID = 1;
}