package nyla.solutions.core.security.data;

import nyla.solutions.core.exception.SecurityException;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SecurityAclTest
{

	@Test
	public void testCheckPermissionPrincipalString()
	throws Exception
	{
		 Principal caller = null;
	    Principal principal = null;
	    
		SecurityAcl securityAcl = new SecurityAcl();
		assertFalse(securityAcl.checkPermission(principal, "CLUSTER"));
		
		principal = new SecurityClient("greenID");
		caller = new SecurityClient("admin");
		securityAcl.addEntry(caller,principal, "CLUSTER");
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));
		
		assertFalse(securityAcl.checkPermission(new SecurityGroup(principal.getName()), "CLUSTER"));
		
	}
	
	@Test
	public void test_multple_permissions_per_acl()
	throws Exception
	{
		 Principal caller = null;
	    Principal principal = null;
	    
	    SecurityAcl securityAcl = new SecurityAcl();
		
		principal = new SecurityClient("greenID");
		caller = new SecurityClient("admin");

		securityAcl.addEntry(caller,principal, "CLUSTER");
		securityAcl.addEntry(caller,principal, "MANAGE");
		
		assertTrue(securityAcl.checkPermission(principal, "MANAGE"));
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));
		
		assertFalse(securityAcl.checkPermission(principal, "ALL"));
		
	}
	
	@Test
	public void testCheckPermissionGroupString()
	throws Exception
	{
		 Principal caller = null;
	    Principal principal = null;
	    
		SecurityAcl securityAcl = new SecurityAcl();
		
		principal = new SecurityGroup("group");
		caller = new SecurityClient("admin");
		securityAcl.addEntry(caller,principal, "CLUSTER");
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));
		
		assertFalse(securityAcl.checkPermission(new SecurityClient(principal.getName()), "CLUSTER"));
		
	}
	@Test
	public void testCheckPermissionSecurityUser()
	throws Exception
	{
		 Principal caller = null;
	    Principal principal = null;
	    
	    SecurityAcl securityAcl = new SecurityAcl();
		
		SecurityGroup group = new SecurityGroup("group");
		principal = new SecurityUser("user",Collections.singleton(group));
		
		caller = new SecurityClient("admin");
		assertFalse(securityAcl.checkPermission(principal, "CLUSTER"));
		
		securityAcl.addEntry(caller,group, "CLUSTER");
		
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));
		securityAcl.addEntry(caller,group, false,"CLUSTER");
		assertTrue(securityAcl.checkPermission(group, "CLUSTER"));
		
		securityAcl.addEntry(caller,principal, "CLUSTER");
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));

		
	}

	@Test
	public void testNegativeCheckPermissionGroupString()
	throws Exception
	{
		 Principal caller = null;
	    Principal principal = null;
	    
	    SecurityAcl securityAcl = new SecurityAcl();
		
		principal = new SecurityGroup("group");
		caller = new SecurityClient("admin");
		securityAcl.addEntry(caller,principal, false,"CLUSTER");
		assertTrue(securityAcl.checkPermission(principal, "CLUSTER"));
		
		try{
			securityAcl.addEntry(caller,principal, true,"CLUSTER");			
		}
		catch(SecurityException e)
		{}
		
		//assertFalse(securityAcl.checkPermission(principal, "CLUSTER"));
		
		principal =  new SecurityGroup("group2");
		
		securityAcl.addEntry(caller,principal, true,"DATA-EDIT");
		assertFalse(securityAcl.checkPermission(principal, "DATA-EDIT"));	
	}
}
