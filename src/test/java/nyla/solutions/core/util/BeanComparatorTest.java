package nyla.solutions.core.util;

import nyla.solutions.core.data.MapEntry;
import nyla.solutions.core.security.user.data.User;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit test for the BeanComparator
 * @author Gregory Green
 *
 */
public class BeanComparatorTest
{
	
	@Test
	public void testCompare_descending()
	{
		//The constructor accepts a JavaBean property name
				BeanComparator beanComparator = new BeanComparator("firstName",true);
				
				//The following are two sample user profile beans
				UserProfile josiah = new UserProfile();
				josiah.setFirstName("Josiah");
				
				 UserProfile nyla = new UserProfile();
				 nyla.setFirstName("Nyla");
				 
				 //Reflection is used to compare the properties of the beans
				 assertTrue(beanComparator.compare(josiah, nyla) > 0);
				 
				 //The following shows how the BeanComparator.sort method can be used
				 
				 //This method can be used to sort an given collection based on the JavaBean properties 
				 //of objects in the collection
				 ArrayList<UserProfile> unSorted = new ArrayList<UserProfile>();
				 unSorted.add(0, nyla);
				 unSorted.add(1, josiah);
				 
				 //Setting the descending will determine the sort order
				 beanComparator.setDescending(true);
				 beanComparator.sort(unSorted);

				 assertTrue(unSorted.get(0) == nyla);
				 
				 //Changing the descending flag changes the output of the sort method
				 beanComparator.setDescending(true);
				 beanComparator.sort(unSorted);
				 assertTrue(unSorted.get(0) == nyla);
		
	}//------------------------------------------------
	@Test
	public void testCompare()
	{
		//The constructor accepts a JavaBean property name
		BeanComparator beanComparator = new BeanComparator("firstName");
		
		//The following are two sample user profile beans
		UserProfile josiah = new UserProfile();
		josiah.setFirstName("Josiah");
		
		 UserProfile nyla = new UserProfile();
		 nyla.setFirstName("Nyla");
		 
		 //Reflection is used to compare the properties of the beans
		 assertTrue(beanComparator.compare(josiah, nyla) < 0);
		 
		 //The following shows how the BeanComparator.sort method can be used
		 
		 //This method can be used to sort an given collection based on the JavaBean properties 
		 //of objects in the collection
		 ArrayList<UserProfile> unSorted = new ArrayList<UserProfile>();
		 unSorted.add(0, nyla);
		 unSorted.add(1, josiah);
		 
		 //Setting the descending will determine the sort order
		 beanComparator.setDescending(true);
		 beanComparator.sort(unSorted);

		 assertTrue(unSorted.get(0) == nyla);
		 
		 //Changing the descending flag changes the output of the sort method
		 beanComparator.setDescending(false);
		 beanComparator.sort(unSorted);
		 assertTrue(unSorted.get(0) == josiah);
	}

	@Test
	public void testNestedCompare()
	{
		//The constructor accepts a JavaBean property name
		BeanComparator beanComparator = new BeanComparator("value.lastName");
		
		Collection<Map.Entry<String,User>> entries = new TreeSet<Map.Entry<String,User>>(beanComparator);
		
		entries.add(new MapEntry<String,User>("A", new UserProfile("email", "loginID", "firstName", "ZLastName")));
		entries.add(new MapEntry<String,User>("A", new UserProfile("email", "loginID", "firstName", "ALastName")));
		
		
		assertEquals("ALastName", entries.iterator().next().getValue().getLastName());
	}
}
