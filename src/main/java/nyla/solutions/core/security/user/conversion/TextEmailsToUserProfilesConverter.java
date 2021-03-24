package nyla.solutions.core.security.user.conversion;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.conversion.TextToEmailsConverter;
import nyla.solutions.core.security.user.data.UserProfile;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <pre>
 * Convert text to UserProfile.
 * </pre> 
 * @author Gregory Green
 *
 */
public class TextEmailsToUserProfilesConverter implements Converter<String, Set<UserProfile>>
{
	/**
	 * @return the set of user profiles populated with the email address
	 */
	@Override
	public Set<UserProfile> convert(String text)
	{
		Set<String> emails = converter.convert(text);
		
		if(emails == null)
			return null;
		
		return emails.stream().map(email -> new UserProfile(email, null, null, null))
		.collect(Collectors.toSet());
	}

	private final TextToEmailsConverter converter = new TextToEmailsConverter();
}
