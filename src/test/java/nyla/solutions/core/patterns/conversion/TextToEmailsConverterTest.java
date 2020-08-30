package nyla.solutions.core.patterns.conversion;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for TextToEmailsConverter
 * @author Gregory Green
 *
 */
public class TextToEmailsConverterTest
{

	@Test
	public void testConvert()
	throws Exception
	{
		String email =  "";
		TextToEmailsConverter converter = new TextToEmailsConverter();
		Collection<String> emails = converter.convert(email);
		assertNull(emails);
		
		email =  "nyla";
		emails = converter.convert(email);
		assertNull(emails);
		
		email =  "nyla@solu";
		emails = converter.convert(email);
		assertNull(emails);

		email =  "nyla@solu.com";
		emails = converter.convert(email);
		assertNotNull(emails);
		assertTrue(emails.contains("nyla@solu.com"));
		
		email =  "rgreen@excellency.org";
		emails = converter.convert(email);
		assertNotNull(emails);
		assertTrue(!emails.contains("nyla@solu.com"));
		assertTrue(emails.contains(email));

		email =  " rgreen@excellency.org";
		emails = converter.convert(email);
		assertTrue(emails.contains("rgreen@excellency.org"));

		email =  " rgreen@excellency.org ";
		emails = converter.convert(email);
		assertTrue(emails.contains("rgreen@excellency.org"));

		email =  " rgreen@excellency.org and nyla@solu.com ";
		emails = converter.convert(email);
		assertTrue(emails.size() == 2);
		assertTrue(emails.contains("rgreen@excellency.org"));
		assertTrue(emails.contains("nyla@solu.com"));
		
		
		email =  "Please remove rgreen@excellency.org and nyla@solu.marketing ";
		emails = converter.convert(email);
		assertEquals(2,emails.size());
		assertTrue(emails.contains("nyla@solu.marketing"));
		
		String text = "<bevans@aol.com>: host core-acb05c.mail.aol.com[172.27.24.55] said: 554 5.7.1";
		
		emails = converter.convert(text);
		assertNotNull(emails);
		assertTrue(emails.contains("bevans@aol.com"));
		
		text = "Final-Recipient: rfc822;roncheve@msn.com";
			
		emails = converter.convert(text);
		assertNotNull(emails);
		assertTrue(emails.contains("roncheve@msn.com"));
	}
}
