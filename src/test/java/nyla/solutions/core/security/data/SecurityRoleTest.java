package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SecurityRoleTest
{

	@Test
	public void testCheckPermission()
	{
		SecurityRole role = new SecurityRole("role");
		
		assertFalse(role.checkPermission(null));
		
		assertFalse(role.checkPermission(new SecurityPermission("")));
		role.addPermission(new SecurityPermission(""));
		assertTrue(role.checkPermission(new SecurityPermission("")));
		
	}

}
