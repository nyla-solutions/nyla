package nyla.solutions.core.security.user.conversion;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.security.user.data.UserProfile;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 * Support converting VCF to an UserProfile instance.
 * 
 * Example
 * 
 * <code>
 * VcfFileToUserProfileConverter converter = new VcfFileToUserProfileConverter();
   File file = new File("src/test/resources/security/user/vcard1.vcf");
   UserProfile user = converter.convert(file);
 * </code>
 * </pre>
 * @author Gregory Green
 *
 */
public class VcfFileToUserProfileConverter implements Converter<File, UserProfile>
{
	/**
	 * Convert the user profile
	 */
	@Override
	public UserProfile convert(File file)
	{
		if(file == null)
			return null;
		
		try
		{
			String text = IO.readFile(file);
			
			if(text == null ||  text.length() == 0)
				return null;
			
			return converter.convert(text);
		}
		catch (IOException e)
		{
			throw new SystemException("Unable to convert file:"+
						file.getAbsolutePath()+" to user profile ERROR:"+e.getMessage(),e);
		}
	}//------------------------------------------------
	/**
	 * @param userProfileClass
	 * @see nyla.solutions.core.security.user.conversion.VcfStringToUserProfileConverter#setUserProfileClass(java.lang.Class)
	 */
	public void setUserProfileClass(Class<? extends UserProfile> userProfileClass)
	{
		converter.setUserProfileClass(userProfileClass);
	}//------------------------------------------------



	private VcfStringToUserProfileConverter converter = new VcfStringToUserProfileConverter();

}
