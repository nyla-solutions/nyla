package nyla.solutions.core.ds;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
