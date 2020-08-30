package nyla.solutions.core.patterns.jmx;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * 
 * Provides password encryptions based on GemFire conventions
 */
public class JmxSecurity
{
	
	/**
	 * JMX_USERNAME_PROP = "jmx-username"
	 */
	public static final String JMX_USERNAME_PROP = "jmx-security-username";
	
	/**
	 * JMX_PASSWORD_PROP = "jmx-security-userPassword"
	 */
	public static final String JMX_PASSWORD_PROP = "jmx-security-userPassword";
	
	/**
	 * ENCRYPTED_PASSWORD_PREFIX = "encrypted("
	 */
	private static final String ENCRYPTED_PASSWORD_PREFIX = "encrypted(";

	/**
	 * ENCRYPTED_PASSWORD_SUFFIX = ")"
	 */
	private static final String ENCRYPTED_PASSWORD_SUFFIX = ")";

	/**
	 * CIPHER_INSTANCE = "Blowfish"
	 */
	private static final String CIPHER_INSTANCE = "Blowfish";

	private static byte[] init = "string".getBytes(Charset.forName("UTF8"));
	
	//private static String jmxPropertyFilePath = null;
	
	/**
	 * JMX_PROPERTY_NM = "jmx.properties"
	 */
	public static final String JMX_PROPERTY_FILE_NM = "jmx.properties";

	/**
	 * Example return encrypted(........)
	 * 
	 * @param password String to be encrypted
	 * @return String encrypted String
	 */
	public static char[] encrypt(char[] password)
	{
		if (password == null || password.length == 0)
		{
			return null;
		}

		try
		{
			String encryptedString = null;

			SecretKeySpec key = new SecretKeySpec(init, "Blowfish");
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encrypted = cipher.doFinal(String.valueOf(password).getBytes(Charset.forName("UTF8")));
			encryptedString = byteArrayToHexString(encrypted);

			return encryptedString.toCharArray();
		}
		catch (Exception e)
		{
			throw new SecurityException("Unable to encrypt password", e);
		}
	}// --------------------------------------------------------

	/**
	 * Surround encrypted password with a prefix and suffix to indicate it has been encrypted
	 * 
	 * @param password the password string
	 * @return encrypted password surrounded by the prefix and suffix
	 */
	public static String decorateEncryption(char[] password)
	{
		if(password == null || password.length == 0)
			return null;
		
		return new StringBuilder(ENCRYPTED_PASSWORD_PREFIX)
				.append(encrypt(password)).append(ENCRYPTED_PASSWORD_SUFFIX)
				.toString();

	}// --------------------------------------------------------


	
	/**
	 * 
	 * @return the resource 
	 */
	private static synchronized ResourceBundle getProperties()
	{
		if(_bundle == null)
		{
			//URL url = JmxSecurity.class.getResource(JMX_PROPERTY_FILE_NM);
			
			//if(url == null)
			//	jmxPropertyFilePath = JMX_PROPERTY_FILE_NM;
			//else
			//	jmxPropertyFilePath = url.toString();
			
			//System.out.println(new StringBuilder("Searching classpath for ")
			//		.append(jmxPropertyFilePath).toString());
			
			_bundle = ResourceBundle.getBundle("jmx");
			
			
		}
		
		return _bundle;		
	}// --------------------------------------------------------
	/**
	 * decrypt an encrypted password string.
	 * 
	 * @param password String to be decrypted
	 * @return String decrypted String
	 */
	public static char[] decrypt(char[] password)
	{
		if(password == null || password.length == 0)
			return null;
		

		String passwordString = String.valueOf(password);
		try
		{
			byte[] decrypted = null;
			if (passwordString.startsWith("encrypted(")
					&& passwordString.endsWith(")"))
			{
				passwordString = passwordString.substring(10,
							passwordString.length() - 1);
			}
			
			SecretKeySpec key = new SecretKeySpec(init, "Blowfish");
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypted = cipher.doFinal(hexStringToByteArray(passwordString));
			

			return  new String(decrypted, Charset.forName("UTF8")).toCharArray();
		
		}
		catch (Exception e)
		{
			throw new SecurityException(
					"Unable to decrypt password. Check that the password has been encrypted.", e);
		}
	}// --------------------------------------------------------
	/**
	 * 
	 * @return the user name from property file
	 */
	public static String getJmxUserName()
	{
		try
		{
			String username = getProperties().getString(JMX_USERNAME_PROP);
			if(username == null || username.length() == 0)
				return null;
			
			return username;
		}
		catch (MissingResourceException e)
		{
			return null;
		}
	}// --------------------------------------------------------
	/**
	 * 
	 * @return unencrypted password from jmx.properties
	 */
	public static char[] getJmxPassword()
	{
		try
		{
			String password = getProperties().getString(JMX_PASSWORD_PROP);
			if(password == null || password.length() == 0)
				return null;
			
			return decrypt(password.toCharArray());
			
		}
		catch (MissingResourceException e)
		{
			return null;
		}
		
	}// --------------------------------------------------------
	private static String byteArrayToHexString(byte[] b)
	{
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			int v = b[i] & 0xff;
			if (v < 16)
			{
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}// --------------------------------------------------------
	private static byte[] hexStringToByteArray(String s)
	{
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++)
		{
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}
	
	private static ResourceBundle _bundle = null;
}
