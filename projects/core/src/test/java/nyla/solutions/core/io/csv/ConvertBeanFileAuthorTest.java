package nyla.solutions.core.io.csv;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.converter.ConversionFileAuthor;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConvertBeanFileAuthorTest
{

	@Test
	public void testAppendFile()
	throws IOException
	{
		BeanPropertiesToCsvConverter<UserProfile> converter = new BeanPropertiesToCsvConverter<>(UserProfile.class);
		File file = new File("runtime/tmp/csv/UserProfiles.csv");
		IO.dir().delete(file);
		
		ConversionFileAuthor<UserProfile> author = new ConversionFileAuthor<>(file, new BeanPropertiesToCsvHeaderConverter<>(),converter);
		
		UserProfile  user1 = new UserProfile("ggreen@pivotal.io","ggreenId","Greg","Green");
		UserProfile  user2 = new UserProfile("nyla@futurepivots.com","nyla","Nyla","Green");
		author.appendFile(user1);
		author.appendFile(user2);
		
		assertTrue(file.exists());
		
		String context = IO.reader().readTextFile(file.toPath());
		String header = converter.toHeaderRow(UserProfile.class);
		System.out.println("header:"+header);
		assertTrue(context.contains(header));
		assertTrue(context.contains(converter.convert(user1)));
		
		String row2 = converter.convert(user2);
		
		assertTrue(context.contains(row2.substring(0, row2.length()-2)),row2+" in "+context);
		
		
	}

}
