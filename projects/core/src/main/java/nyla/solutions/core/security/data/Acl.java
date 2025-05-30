package nyla.solutions.core.security.data;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

public interface Acl extends Serializable
{
	String getName();
	

	
	boolean checkPermission(Principal principal, Permission permission);
	
	boolean checkPermission(Set<SecurityGroup> groups, Permission permission);


	
	boolean checkPermission(Principal aPrincipal, String aPermission);
	
	boolean isEmpty();


}