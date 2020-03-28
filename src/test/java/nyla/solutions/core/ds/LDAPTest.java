package nyla.solutions.core.ds;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Hashtable;

import org.junit.jupiter.api.Test;

import nyla.solutions.core.util.Config;

public class LDAPTest
{

	@Test
	public void testSetupSecurityProperties()
	{
	 char[] pwd = null;
	Hashtable<String, Object> properties = new Hashtable<>();
	
		String userDN = null;
		LDAP.setupSecurityProperties(properties, userDN, pwd);
		
		assertTrue(properties.isEmpty());
		
		userDN = "imani";
		pwd = "test".toCharArray();
		
		LDAP.setupSecurityProperties(properties, userDN, pwd);
		assertFalse(properties.isEmpty());
		
		/*
		 * java.naming.ldap.factory.socket = SSLConfigSocketFactory
contextFactory.environment[java.naming.security.protocol] = ssl
		 */
		
		properties.put(LDAP.JAVA_NAMING_PROVIDER_URL,"ldaps://helloworld:389");
		LDAP.setupSecurityProperties(properties, userDN, pwd);
		
		assertNull(properties.get("java.naming.ldap.factory.socket"));
		assertNull(properties.get("java.naming.security.protocol"));
		
		System.setProperty("LDAP_USE_SSL_CONFIG_FACTORY", "true");
		Config.reLoad();
		
		LDAP.setupSecurityProperties(properties, userDN, pwd);
		
		assertEquals(properties.get("java.naming.ldap.factory.socket"),SSLConfigSocketFactory.class.getName());
		assertEquals(properties.get("java.naming.security.protocol"),"ssl");
	}

}
