package nyla.solutions.core.security.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class SecurityPermissionTest
{

	@Test
	public void testEquals()
	{
		assertEquals(new SecurityPermission(""),new SecurityPermission(""));
		
		assertNotEquals(new SecurityPermission(""),new SecurityPermission("not"));
		

		assertEquals(new SecurityPermission(null),new SecurityPermission(null));
		assertNotEquals(new SecurityPermission("p"),new SecurityPermission(null));
	}
	

}
