package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AllPermissionTest
{

	@Test
	public void testIsAuthorized()
	{
		assertTrue(new AllPermission().isAuthorized(new SecurityPermission("TEST")));
		assertTrue(new AllPermission().isAuthorized( new SecurityPermissionContains("TEST")));
		assertTrue(new AllPermission().isAuthorized(new SecurityPermission("ALL")));
		assertTrue(new AllPermission().isAuthorized(new AllPermission()));
		
		assertFalse(new SecurityPermissionContains("All").isAuthorized(new AllPermission()));

	}

}
