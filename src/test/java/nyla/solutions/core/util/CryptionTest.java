package nyla.solutions.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Testing for Cryption
 * @author gregory green
 */
public class CryptionTest
{
	private String key = "THIS_IS_MY_KEY_FROM_JUNIT_TESTING";
	private Cryption subject;

	@BeforeEach
	void setUp() {
		subject = new Cryption(key);
	}

	@Test
	void given_unencryptedText_whenInterpretText_Then_Return_UnencryptedText() {

		String expected = "Hello";
		var actual = subject.interpretText(expected);
		assertEquals(expected, actual);
	}

	@Test
	void given_encryptedText_whenInterpretText_Then_Return_UnencryptedText() throws Exception {
		String expected = "Hello";
		var actual = subject.interpretText(Cryption.CRYPTION_PREFIX+subject.encryptText(expected));
		assertEquals(expected, actual);
	}

	@Test
	void given_null_whenInterpretText_Then_Return_null() throws Exception {
		assertNull(subject.interpretText(null));
	}

	@Test
	public void test_removePrefix()
	{
		assertNull(Cryption.removePrefix(null));
		assertEquals("",Cryption.removePrefix(""));
		assertEquals("",Cryption.removePrefix("{cryption}"));
		assertEquals("a",Cryption.removePrefix("{cryption}a"));
		assertEquals("a{cryption}",Cryption.removePrefix("{cryption}a{cryption}"));
		
	}//------------------------------------------------
	
	@Test
	public void test_newWithString()
	throws Exception
	{
		Cryption c = new Cryption("test");
		
		String text = "hello";
		assertEquals(text,c.decryptText(c.encryptText(text)));
		
	}
	
	@Test
	public void test_newWithPrefix()
	throws Exception
	{
		Cryption c = new Cryption("test");
		
		String text = "{cryption}"+c.encryptText("hello");
		assertEquals("hello",c.decryptText(text));
		
	}
	public void testCryption() throws Exception
	{
		//The cryption default constructor using the 
		//AES algorithm with a default key
		Cryption cryption = new Cryption();
		
		//Use the encryptText method to encrypt strings
		String original = "Text to encrypt";
		String encrypted = cryption.encryptText(original);
		
		System.out.println("encrypted:"+encrypted);
		assertTrue(!original.equals(encrypted));
		
		//Use the decryptText method to decrypt
		String decrypted = cryption.decryptText(encrypted);
		assertEquals(decrypted, original);
		
		//Use encrypt for bytes
		byte[] orginalBytes = original.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedBytes = cryption.encrypt(orginalBytes);
		
		//Use decrypt 
		byte[] decryptedBytes = cryption.decrypt(encryptedBytes);
		assertTrue(Arrays.equals(orginalBytes, decryptedBytes));
		
		
		//Create crypt with a specific key and algorithm
		byte[] keyBytes = {0x22, 0x15, 0x27, 0x36, 0x41, 0x11, 0x79, 0x76};
		Cryption desCryption = new Cryption(keyBytes,"DES"); 
		
		String desEncrypted = desCryption.encryptText(original);
		assertTrue(!original.equals(encrypted) && !desEncrypted.equals(encrypted));
		decrypted = desCryption.decryptText(desEncrypted);
		assertEquals(decrypted, original);
	
		
	}
	@Test
	public void testMain() throws Exception
	{
		String [] args = {"PASSWORD"};
		
		Cryption.main(args);
	}

}
