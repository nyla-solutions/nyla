package nyla.solutions.spring.batch.email;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.springframework.batch.item.ItemReader;

import nyla.solutions.core.security.user.conversion.FilesEmailsToUserProfilesConverter;
import nyla.solutions.core.security.user.data.UserProfile;

/**
 * <pre>
 * Item reader implementation to convert a file or directory of text files to 
 * User profiles objects.
 * </pre>
 * @author Gregory Green
 *
 */
public class FilesTextEmailUserProfileReader implements ItemReader<UserProfile>
{
	/**
	 * 
	 * @param path the file or directory path
	 */
	public FilesTextEmailUserProfileReader(File file)
	{
		this.file = file;
		
	}//------------------------------------------------
	public UserProfile read()
	{
		if(file == null || !file.exists())
			return null;
		
		synchronized (this)
		{
			if(converter == null)
			{
				converter = new FilesEmailsToUserProfilesConverter();
				
				set = converter.convert(file);
				
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
	
	private final File file;
	private FilesEmailsToUserProfilesConverter converter = null;

}
