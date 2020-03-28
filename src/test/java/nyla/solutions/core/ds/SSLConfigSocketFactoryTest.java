package nyla.solutions.core.ds;

import org.junit.jupiter.api.Test;

import nyla.solutions.core.util.Config;

import static org.junit.jupiter.api.Assertions.*;
public class SSLConfigSocketFactoryTest
{

	@Test
	public void test()
	throws Exception
	{
		
		System.setProperty(SSLConfigSocketFactory.LDAP_SSL_KEYSTORE_PASSWORS_PROP, "pivotal");
		System.setProperty(SSLConfigSocketFactory.LDAP_SSL_TRUSTSTORE_PASSWORS_PROP, "pivotal");
		System.setProperty(SSLConfigSocketFactory.LDAP_SSL_KEYSTORE_PROP, "src/test/resources/security/trusted.keystore");
		System.setProperty(SSLConfigSocketFactory.LDAP_SSL_TRUSTSTORE_PROP, "src/test/resources/security/trusted.keystore");
		
		Config.reLoad();
		
		SSLConfigSocketFactory f = new SSLConfigSocketFactory();
		
		assertFalse(f.equals(null));
	}
	
	

}
