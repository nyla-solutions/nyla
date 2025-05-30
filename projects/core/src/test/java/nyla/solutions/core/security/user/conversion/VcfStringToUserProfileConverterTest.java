package nyla.solutions.core.security.user.conversion;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test VcfStringToUserProfileConverter
 * @author Gregory Green
 *
 */
public class VcfStringToUserProfileConverterTest
{
	/**
	 * Testing the conversion method
	 */
	@Test
	public void testConvert()
	{
		VcfStringToUserProfileConverter converter = new VcfStringToUserProfileConverter();
		
		
		assertNull(converter.convert(null));
		assertNull(converter.convert(""));
		assertNull(converter.convert(" "));
		assertNull(converter.convert("\t   "));
		
		String text = "begin:vcard\n"+
				"version:3.0\n"+
				"prodid:Microsoft-MacOutlook/f.15.1.160411\n"+
				"UID:86584FBF-3DA4-4ECD-AA68-0D4155EA1210\n"+
				"fn;charset=utf-8:1st Timothy\n"+
				"n;charset=utf-8:;;;;\n"+
				"org;charset=utf-8:1st Timothy;\n"+
				"adr;charset=utf-8;type=work;type=pref:;;215 Chancellor Ave;Newark;NJ;;\n"+
				"label;charset=utf-8;type=work;type=pref:215 Chancellor Ave Newark, NJ\n"+
				"tel;charset=utf-8;type=work:9739269375\n"+
				"email;charset =utf-8;type=internet;type=pref;type=other:Tim@nyla.com\n"+
				"end:vcard\n";
		
		
		UserProfile user = converter.convert(text);
		
		assertNotNull(user);
		
		assertEquals("1st Timothy", user.getFirstName());
		assertEquals("Tim@nyla.com", user.getEmail());
		
		
		text = new StringBuilder("BEGIN:VCARD")
		.append("VERSION:3.0\n")
				.append("PRODID:-//Apple Inc.//Mac OS X 10.11.6//EN\n")
				.append("N:Green;Gregory;;;\n")
				.append("FN:Gregory Green\n")
				.append("EMAIL;type=INTERNET;type=HOME;type=pref:green_gregory@yahoo.com\n")
				.append("X-ABUID:49C77E65-29D8-48C7-A524-CB8E42A912CC:ABPerson\n")
				.append("END:VCARD\n").toString();
		
		
		 user = converter.convert(text);
			
			assertNotNull(user);
			
			assertEquals("Gregory", user.getFirstName());
			assertEquals("Green", user.getLastName());
			assertEquals("green_gregory@yahoo.com", user.getEmail());
		
			
			
			text = new StringBuilder("begin:vcard")
			.append("version:3.0\n")
			.append("prodid:Microsoft-MacOutlook/f.15.1.160411\n")
			.append("UID:9A5A71F3-4553-465E-988E-DC69F1641F82\n")
			.append("fn;charset=utf-8:Stick\n")
			.append("n;charset=utf-8:;Stick;;;\n")
			.append("note;charset=utf-8: \n \n\n")
			.append("bday:2007-05-26\n")
			.append("tel;charset=utf-8;type=cell:(908) 403-3865\n")
			.append("email;charset=utf-8;type=internet;type=pref;type=other:stickman@people.net\n")
			.append("end:vcard\n").toString();

			user = converter.convert(text);
			
			assertNotNull(user);
			
			assertEquals("Stick", user.getFirstName());
			
			assertEquals("stickman@people.net", user.getEmail());
			
			
			text = new StringBuilder("BEGIN:VCARD\n")
					.append("VERSION:3.0\n")
							.append("PRODID:-//Apple Inc.//Mac OS X 10.11.6//EN\n")
							.append("N:;Stick;;;\n")
							.append("FN:Stick\n")
							.append("item1.EMAIL;type=INTERNET;type=pref:stickandme@comcast.net\n")
							.append("item1.X-ABLabel:_$!<Other>!$_\n")
							.append("TEL;type=CELL;type=VOICE;type=pref:(908) 403-3865\n")
							.append("NOTE:\n \n\n")
							.append("BDAY:2007-05-26\n")
							.append("X-ABUID:7F680101-8AA9-4637-A22B-60FBD0DEE276:ABPerson\n")
							.append("END:VCARD\n").toString();
			
			user = converter.convert(text);
			
			assertNotNull(user);
			
			assertEquals("Stick", user.getFirstName());
			
			assertEquals("stickandme@comcast.net", user.getEmail());		
			
			text = new StringBuilder("BEGIN:VCARD\n")
					.append("VERSION:3.0\n")
							.append("PRODID:-//Apple Inc.//Mac OS X 10.11.6//EN\n")
							.append("N:;Stick;;;\n")
							.append("FN:Stick\n")
							.append("item1.EMAIL;type=INTERNET;type= pref:	stickandme@comcast.net\n")
							.append("item1.X-ABLabel:_$!<Other>!$_\n")
							.append("TEL;type=CELL;type=VOICE;type=pref:(908) 403-3865\n")
							.append("NOTE:\n \n\n")
							.append("BDAY:2007-05-26\n")
							.append("X-ABUID:7F680101-8AA9-4637-A22B-60FBD0DEE276:ABPerson\n")
							.append("END:VCARD\n").toString();
			
			user = converter.convert(text);
			
			assertNotNull(user);
			
			assertEquals("Stick", user.getFirstName());
			
			assertEquals("stickandme@comcast.net", user.getEmail());	
		
	}

}
