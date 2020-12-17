package nyla.solutions.core.ds;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LdapQA
{

	public void verifyAuthenicateStringCharArray()
	throws Exception
	{

		char[] adminPwd  ="secret".toCharArray();
		String user = "admin";
		String adminDN = "uid="+user+",ou=system";
		char[] pwd = "secret".toCharArray();
		
		try(LDAP ldap = new LDAP("ldap://localhost", adminDN, adminPwd))
		{
			Principal principal = ldap.authenicate(user,pwd,"ou=system","uid","memberOf","CN",100);
			assertEquals(user.toLowerCase(),principal.getName().toLowerCase());
		}
		
		try(LDAP ldap = new LDAP("ldap://localhost", adminDN, adminPwd))
		{
			Principal principal = ldap.authenicate(user,pwd,"ou=system",null,null,null,100);
			assertEquals(user.toLowerCase(),principal.getName().toLowerCase());
		}
		
	}

	public static void main(String[] args)
	throws Exception
	{
		LdapQA ldapQA = new LdapQA();
		
		ldapQA.verifyAuthenicateStringCharArray();
		
		
	}
}
