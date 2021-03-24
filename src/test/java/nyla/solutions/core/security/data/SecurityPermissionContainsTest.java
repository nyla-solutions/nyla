package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testing for SecurityPermissionContains 
 * @author Gregory Green
 *
 */
public class SecurityPermissionContainsTest
{

	@Test
	public void testIsAuthorized()
	{
		//assertEquals( new SecurityPermissionContains("GREEN"), new SecurityPermission("GREEN"));
		//assertEquals( new SecurityPermissionContains("GREEN"), new SecurityPermissionContains("GREEN"));
		assertTrue(new SecurityPermissionContains("NYLA").isAuthorized(new SecurityPermission("NYLA GREEN")));
		assertFalse(new SecurityPermissionContains("GREG").isAuthorized(new SecurityPermission("NYLA GREEN")));
		
	}
	
	@Test
	public void testAuthorized_Multi() throws Exception
	{
		assertTrue(new SecurityPermissionContains("READ,DATA,CLUSTER").isAuthorized(new SecurityPermission("READ")));
		assertTrue(new SecurityPermissionContains("READ,DATA,CLUSTER").isAuthorized(new SecurityPermission("CLUSTER")));
		assertTrue(new SecurityPermissionContains("DATA").isAuthorized(new SecurityPermission("READ,DATA,CLUSTER")));
		assertTrue(new SecurityPermissionContains("READ,DATA").isAuthorized(new SecurityPermission("READ,DATA,CLUSTER")));
		assertTrue(new SecurityPermissionContains("READ,DATA,CLUSTER").isAuthorized(new SecurityPermission("DATA,CLUSTER")));
		assertTrue(new SecurityPermissionContains("READ,DATA,CLUSTER").isAuthorized(new SecurityPermission("DATA")));
		assertTrue(new SecurityPermissionContains("READ,DATA,CLUSTER").isAuthorized(new SecurityPermission("CLUSTER")));
		assertTrue(new SecurityPermissionContains("READ|DATA|CLUSTER").isAuthorized(new SecurityPermission("READ")));
		
		assertFalse(new SecurityPermissionContains("READ").isAuthorized(new SecurityPermission("NONE")));
		assertFalse(new SecurityPermissionContains("READ").isAuthorized(new SecurityPermission(null)));
		assertFalse(new SecurityPermissionContains("none").isAuthorized(new SecurityPermission(null)));
		

	}

}
