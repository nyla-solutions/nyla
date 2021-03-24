package nyla.solutions.core.security.user.conversion;

import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for VcfFileToUserProfileConverter
 * @author Gregory Green
 *
 */
public class VcfFileToUserProfileConverterTest
{
	/**
	 * Test the conversion from a file VCF
	 */
	@Test
	public void testConvert()
	{
		VcfFileToUserProfileConverter converter = new VcfFileToUserProfileConverter();
		
		converter.setUserProfileClass(VcfTestPerson.class);
		File file = new File("src/test/resources/security/user/vcard1.vcf");
		
		UserProfile user = converter.convert(file);
		
		assertTrue(user instanceof VcfTestPerson);
		
		assertEquals("ggreen@nyla.com", user.getEmail());
		assertEquals("Greg", user.getFirstName());
		assertEquals("Green", user.getLastName());
		
	}//------------------------------------------------
	
	public static class VcfTestPerson extends UserProfile
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7411811054048170140L;
		
	}

}
