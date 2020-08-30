package nyla.solutions.core.security.user.conversion;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cased on for TextEmailsToUserProfilesConverter
 * @author Gregory Green
 *
 */
public class TextEmailsToUserProfilesConverterTest
{

	@Test
	public void testConvert()
	{
		TextEmailsToUserProfilesConverter converter = new TextEmailsToUserProfilesConverter();
		
		
		assertNull(converter.convert(null));
		assertNull(converter.convert(""));
		UserProfile up = new UserProfile("g@green.com", null, null, null);
		
		assertEquals(converter.convert("g@green.com"),Collections.singleton(up));
		
		assertEquals(converter.convert("g@green.com").size(),1);
		
		assertEquals(converter.convert("g@green.com;rgreen@my.com").size(),2);
		
		assertEquals(converter.convert("g@green.com").iterator().next().getEmail(),"g@green.com");
	}

}
