package nyla.solutions.core.ds.security;

import nyla.solutions.core.security.data.SecurityGroup;
import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LdapSecurityGroupTest
{

	@BeforeAll

	public static void setUP()
	{
		Text.toStrings(""); //load Text object
	}
	@Test
	public void testEqualsObject()
	{
		
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new LdapSecurityGroup("abc"));
		
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new LdapSecurityGroup("ABC"));
		
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new SecurityGroup("abc"));
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new SecurityGroup("abC "));
		
		
		assertNotEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new SecurityGroup("com"));
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new LdapSecurityGroup("CN=abc,CN=user.com"));
		
		assertNotEquals(new LdapSecurityGroup("CN=abc,CN=user.com"), new LdapSecurityGroup("CN=xyz,CN=xyz.com"));
	}
	
	@Test
	public void testHashCode()
	{
		
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new LdapSecurityGroup("CN=abc,CN=user.com").hashCode());
		
		int code1 =  new LdapSecurityGroup("abc").hashCode();
		int code2 =  new LdapSecurityGroup("CN=abc,CN=user.com").hashCode();
		
		assertEquals(code1,code2);
		
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new LdapSecurityGroup("ABC").hashCode());
		
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new SecurityGroup("abc").hashCode());
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new SecurityGroup("abC ").hashCode());
		
		
		assertNotEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new SecurityGroup("com").hashCode());
		assertEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new LdapSecurityGroup("CN=abc,CN=user.com").hashCode());
		
		assertNotEquals(new LdapSecurityGroup("CN=abc,CN=user.com").hashCode(), new LdapSecurityGroup("CN=xyz,CN=xyz.com").hashCode());
	}

	@Test
	public void testParsing()
	{
		/*
		 * dapSecurityGroup [primaryLdapGroupName==NAMG_CLOUD_ENGINEERING_TEAM, getName()=CN=NAMG_CLOUD_ENGINEERING_TEAM,OU=ITROM,OU=GROUPS,
		 * OU=ACCOUNTS,DC=NAM,DC=ENT,DC=DUKE-ENERGY,DC=COM], 
		 */
		
		LdapSecurityGroup group1 = new LdapSecurityGroup("CN=NAMG_CLOUD_ENGINEERING_TEAM,OU=ITROM,OU=GROUPS,OU=ACCOUNTS,DC=NAM,DC=ENT,DC=DUKE-ENERGY,DC=COM");
		assertEquals("NAMG_CLOUD_ENGINEERING_TEAM",group1.getPrimaryLdapGroupName());
		
		LdapSecurityGroup group2 = new LdapSecurityGroup("CN=NAMG_CLOUD_ENGINEERING_TEAM,OU=ITROM,OU=GROUPS,OU=ACCOUNTS,DC=NAM,DC=ENT,DC=DUKE-ENERGY,DC=COM","CN");
		assertEquals("NAMG_CLOUD_ENGINEERING_TEAM",group2.getPrimaryLdapGroupName());

		LdapSecurityGroup group3 = new LdapSecurityGroup("CN=NAMG_CLOUD_ENGINEERING_TEAM,OU=ITROM,OU=GROUPS,OU=ACCOUNTS,DC=NAM,DC=ENT,DC=DUKE-ENERGY,DC=COM","cn");
		assertEquals("NAMG_CLOUD_ENGINEERING_TEAM",group3.getPrimaryLdapGroupName());
		
		
		assertEquals(group1,group2);
		assertEquals(group3,group2);		
		
	}
}
