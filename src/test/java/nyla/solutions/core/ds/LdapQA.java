package nyla.solutions.core.ds;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LdapQA
{

	public void verifyAuthenicateStringCharArray()
	throws Exception
	{
		String adminDN = "uid=admin,ou=system";
		char[] adminPwd  ="secret".toCharArray();
		String user = "gfadmin";
		char[] pwd = "password".toCharArray();
		
		try(LDAP ldap = new LDAP("ldap://localhost", adminDN, adminPwd))
		{
			Principal principal = ldap.authenicate(user,pwd,"ou=system","uid","memberOf","CN",100);
			assertEquals("GFADMIN",principal.getName());
		}
		
		try(LDAP ldap = new LDAP("ldap://localhost", adminDN, adminPwd))
		{
			Principal principal = ldap.authenicate(user,pwd,"ou=system",null,null,null,100);
			assertEquals("GFADMIN",principal.getName());
		}
		
	}

	public static void main(String[] args)
	throws Exception
	{
		LdapQA ldapQA = new LdapQA();
		
		ldapQA.verifyAuthenicateStringCharArray();
		
		
	}
}
