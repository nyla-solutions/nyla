package nyla.solutions.core.security.data;

import nyla.solutions.core.exception.SecurityException;

import java.security.Principal;
import java.util.*;

/**
 * <pre>
 * SecurityAcl security acl
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class SecurityAcl implements Acl, AclEditor
{
	/**
	* 
	*/
	private static final long serialVersionUID = 15616038981342591L;

	public SecurityAcl()
	{
		this("SecurityAcl");
	}// --------------------------------------------

	/**
	 * Constructor for SecurityAcl initializes internal data settings.
	 * @param aName the acl name
	 * 
	 */
	public SecurityAcl(String aName)
	{
		name = aName;
	}// --------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#getName()
	 */

	@Override
	public synchronized String getName()
	{
		return name;
	}// --------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nyla.solutions.core.security.data.Acl#addEntry(java.security.Principal,
	 * java.security.Principal, nyla.solutions.core.security.data.Permission)
	 */

	@Override
	public synchronized boolean addEntry(Principal caller, Principal principal, Permission permission)
	{
		return addEntry(caller, new SecurityAccessControl(principal, permission));
	}// ------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nyla.solutions.core.security.data.Acl#addEntry(java.security.Principal,
	 * java.security.Principal, java.lang.String)
	 */

	@Override
	public synchronized boolean addEntry(Principal caller, Principal principal, String permission)
	{
		return addEntry(caller, new SecurityAccessControl(principal, permission));
	}// ------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nyla.solutions.core.security.data.Acl#addEntry(java.security.Principal,
	 * java.security.Principal, boolean, java.lang.String)
	 */

	@Override
	public synchronized boolean addEntry(Principal caller, Principal principal, boolean negative, String permission)
	{
		return addEntry(caller, new SecurityAccessControl(principal, negative, permission));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nyla.solutions.core.security.data.Acl#addEntry(java.security.Principal,
	 * nyla.solutions.core.security.data.AccessControl)
	 */
	@Override
	public synchronized boolean addEntry(Principal caller, AccessControl aclEntry)
	{
		if (aclEntry == null)
			return false;

		Principal principal = aclEntry.getPrincipal();

		Set<AccessControl> currentAcl = this.entries.get(principal);

		// find older aclEntry

		if (currentAcl != null)
		{
			for (AccessControl currentEntry : currentAcl)
			{
				if (principal.equals(currentEntry.getPrincipal()))
				{
					// updated old
					mergePermissions(aclEntry, currentEntry);
				}

			}
		}
		else
		{
			Set<AccessControl> set = new HashSet<AccessControl>();
			set.add(aclEntry);
			this.entries.put(principal, set);

		}

		return true;

	}// --------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nyla.solutions.core.security.data.Acl#mergePermissions(nyla.solutions.
	 * core.security.data.AccessControl,
	 * nyla.solutions.core.security.data.AccessControl)
	 */
	@Override
	public void mergePermissions(AccessControl from, AccessControl to)
	{
		if (from == null || to == null)
			return;

		List<Permission> otherPermissions = from.getPermissions();

		if (otherPermissions == null)
			return;

		boolean changeNegative = from.isNegative() != to.isNegative();

		if (changeNegative)
			throw new SecurityException(
			"Cannot change or mixed different negative property for ACL from:" + from + " to ACL:" + to
			+ "  permission must be either all negative or all positive for a principal");

		Iterator<Permission> i = otherPermissions.iterator();

		while (i.hasNext())
		{

			to.addPermission(i.next());
		}
	}// --------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#revokeAccess(java.security.
	 * Principal, nyla.solutions.core.security.data.AccessControl)
	 */

	@Override
	public synchronized boolean revokeAccess(Principal caller, AccessControl aclEntry)
	{
		if (aclEntry == null)
			return false;

		Principal principal = aclEntry.getPrincipal();

		if (principal == null)
			return false;

		Set<AccessControl> set = this.entries.get(aclEntry.getPrincipal());
		if (set == null || set.isEmpty())
			return false;
		boolean r = set.remove(aclEntry);

		this.entries.put(principal, set);

		return r;
	}// --------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#checkPermission(java.security.
	 * Principal, nyla.solutions.core.security.data.Permission)
	 */
	@Override
	public synchronized boolean checkPermission(Principal principal, Permission permission)
	{

		if (principal == null)
			return false;

		if (permission == null)
			return false;

		Set<AccessControl> set = this.entries.get(principal);

		if (set != null && !set.isEmpty())
		{
			for (AccessControl accessControl: set)
			{
				if (accessControl.checkPermission(permission))
					return true;
			}
		}

		// did not find for user, not check for groups
		if (SecurityUser.class.isAssignableFrom(principal.getClass()))
		{
			return checkPermission(((SecurityUser) principal).getGroups(), permission);
		}
		return false;
	}// --------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#checkPermission(java.util.Set,
	 * nyla.solutions.core.security.data.Permission)
	 */

	@Override
	public boolean checkPermission(Set<SecurityGroup> groups, Permission permission)
	{
		if (groups == null || groups.isEmpty())
			return false;

		for (SecurityGroup group : groups)
		{
			if (checkPermission(group, permission))
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#checkPermission(java.security.
	 * Principal, java.lang.String)
	 */
	@Override
	public synchronized boolean checkPermission(Principal aPrincipal, String aPermission)
	{
		if (aPermission == null)
			return false;

		SecurityPermission securityPermission = new SecurityPermission(aPermission);

		return this.checkPermission(aPrincipal, securityPermission);
	}// --------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return this.entries == null || this.entries.isEmpty();
	}// --------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see nyla.solutions.core.security.data.Acl#clear()
	 */
	@Override
	public void clear()
	{
		this.entries.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SecurityAcl [entries=").append(entries).append(", name=")
		.append(name).append("]");
		return builder.toString();
	}

	private Map<Principal, Set<AccessControl>> entries = new HashMap<Principal, Set<AccessControl>>();
	private final String name;
}
