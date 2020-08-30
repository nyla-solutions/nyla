package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RegExpPermissionTest
{

	@Test
	public void testRegExpPermissionIsAuthorized()
	{
		assertTrue(new RegExpPermission(".*").isAuthorized(new SecurityPermission("ALL")));
		assertTrue(new RegExpPermission(".*").isAuthorized(new SecurityPermission("")));
		
		assertFalse(new RegExpPermission("NY.*").isAuthorized(new SecurityPermission("")));
		assertFalse(new RegExpPermission(".*").isAuthorized(new SecurityPermission(null)));
		assertFalse(new RegExpPermission("NY NJ NC.*").isAuthorized(new SecurityPermission("NY NJ")));
	}

}
