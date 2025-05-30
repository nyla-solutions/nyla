package nyla.solutions.core.io.converter;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cases for FilesToEmailsConverter
 * 
 * @author Gregory Green
 *
 */
public class FilesToEmailsConverterTest
{
	/**
	 * Test the convert method
	 */
	@Test
	public void testConvert()
	{
		FilesToEmailsConverter converter = new FilesToEmailsConverter();
		Set<String> emails = converter.convert(new File("src/test/resources/iotest/emails"));
		
		assertNotNull(emails);
		assertTrue(!emails.isEmpty());
		
		assertTrue(emails.contains("nyla@green.com"));
		
	}

}
