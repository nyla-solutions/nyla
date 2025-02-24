package nyla.solutions.core.security.data;

import java.security.Principal;

public interface AclEditor
{

	boolean addEntry(Principal caller, Principal principal, Permission permission);
	
	boolean addEntry(Principal caller, Principal principal, String permission);
	
	boolean addEntry(Principal caller, Principal principal, boolean negative, String permission);

	boolean addEntry(Principal caller, AccessControl accessControl);

	void mergePermissions(AccessControl from, AccessControl to);
	
	boolean revokeAccess(Principal caller, AccessControl accessControl);
	
	
	void clear();
}
