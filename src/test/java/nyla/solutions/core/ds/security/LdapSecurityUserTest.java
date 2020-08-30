package nyla.solutions.core.ds.security;

import nyla.solutions.core.security.data.SecurityUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LdapSecurityUserTest
{

	@Test
	public void testApply()
	{
		
		assertTrue(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new LdapSecurityUser("user1")).booleanValue());
		assertTrue(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new LdapSecurityUser("USER1")));
	
		assertTrue(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new LdapSecurityUser(" USER1")));
		assertTrue(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new LdapSecurityUser("USER1 ")));

		assertTrue(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new LdapSecurityUser(" USER1 ")));

		assertFalse(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new SecurityUser("aBc ")));
	
		assertFalse(new LdapSecurityUser("CN=abc,CN=user.com").apply(new SecurityUser("com")));

		assertTrue(new LdapSecurityUser("user1","CN=abc,CN=user.com").apply(new LdapSecurityUser("unknown","CN=abc,CN=user.com")));
		
		assertFalse(new LdapSecurityUser("CN=abc,CN=user.com").apply(new LdapSecurityUser("CN=xyz,CN=xyz.com")));
	}

}
