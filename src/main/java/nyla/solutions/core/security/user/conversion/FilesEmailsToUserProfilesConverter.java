package nyla.solutions.core.security.user.conversion;

import nyla.solutions.core.io.converter.FilesToEmailsConverter;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.security.user.data.UserProfile;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * Convert file/directories to UserProfile.
 * 
 * A user profile object will be created for each text file that has an email in the given text.
 * 
 * This object can be used to build user email data sets from text files
 * such as .csv, xml, json, yml, files
 * </pre> 
 * @author Gregory Green
 *
 */
public class FilesEmailsToUserProfilesConverter implements Converter<File, Set<UserProfile>>
{
	/**
	 * @return the set of user profiles populated with the email address
	 */
	@Override
	public Set<UserProfile> convert(File file)
	{
		Set<String> emails = converter.convert(file);
		
		if(emails == null)
			return null;
		
		return emails.stream().map(email -> new UserProfile(email, null, null, null))
		.collect(Collectors.toSet());
	}

	private final FilesToEmailsConverter converter = new FilesToEmailsConverter();
}
