package nyla.solutions.core.patterns.conversion.csv;


import nyla.solutions.core.io.csv.BeanPropertiesToCsvConverter;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanPropertiesToCsvConverterTest
{

	@Test
	public void testConvert()
	{
		UserProfile user = new UserProfile();
		
		user.setFirstName("Gregory");
		user.setLastName("Green");
		
		BeanPropertiesToCsvConverter<UserProfile> converter = new BeanPropertiesToCsvConverter<UserProfile>(UserProfile.class);
		
		String csv = converter.convert(user);
		System.out.println("csv:"+csv);
		
		assertTrue(csv.contains(user.getFirstName()));
		assertTrue(csv.contains(user.getLastName()));
	}

}
