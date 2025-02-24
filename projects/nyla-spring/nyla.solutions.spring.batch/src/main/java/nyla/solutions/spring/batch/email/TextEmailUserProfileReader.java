package nyla.solutions.spring.batch.email;

import java.util.Iterator;
import java.util.Set;

import org.springframework.batch.item.ItemReader;

import nyla.solutions.core.security.user.conversion.TextEmailsToUserProfilesConverter;
import nyla.solutions.core.security.user.data.UserProfile;

/**
 * <pre>
 * Item reader implementation to convert a file or directory of text files to 
 * User profiles objects.
 * </pre>
 * @author Gregory Green
 *
 */
public class TextEmailUserProfileReader implements ItemReader<UserProfile>
{
	/**
	 * 
	 * @param path the file or directory path
	 */
	public TextEmailUserProfileReader(String path)
	{
		this.path = path;
	}//------------------------------------------------
	public UserProfile read()
	{
		synchronized (this)
		{
			if(converter == null)
			{
				converter = new TextEmailsToUserProfilesConverter();
				
				set = converter.convert(path);
				
				if(set == null || set.isEmpty())
					return null;
				
				iterator = set.iterator();
				
			}			
		}
		
		if(!iterator.hasNext())
			return null;
		
		return iterator.next();
		
	}//------------------------------------------------
	private Iterator<UserProfile> iterator = null;
	private Set<UserProfile> set = null;
	
	private final String path;
	private TextEmailsToUserProfilesConverter converter = null;

}
