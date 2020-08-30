package nyla.solutions.core.patterns.creational.servicefactory;


import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the service factory
 * @author Gregory Green
 *
 */
public class ServiceFactoryTest
{
	/**
	 * Test get instance
	 */
	@Test
	public void testGetInstance()
	{
		assertNotNull(ServiceFactory.getInstance());
	}//------------------------------------------------

	@Test
	public void testGetInstanceString()
	{
		ServiceFactory factory = ServiceFactory.getInstance("config.properties");
		assertNotNull(factory);
	}//------------------------------------------------

	@Test
	public void testGetInstanceClassOfQ()
	{
		ServiceFactory factory = ServiceFactory.getInstance(ConfigServiceFactory.class);
		assertNotNull(factory);
		
	}//------------------------------------------------

	@Test
	public void testGetInstanceClassOfQString()
	{
		assertNotNull(ServiceFactory.getInstance("config.properties"));
	}//------------------------------------------------
	/**
	 * Testing factory.create(UserProfile.class, "userProfileTest");
	 */
	@Test
	public void testCreateClassOfQ()
	{
		ServiceFactory factory = ServiceFactory.getInstance();
		UserProfile users = factory.create(UserProfile.class, "userProfileTest");
		assertNotNull(users);
	}//------------------------------------------------
	/**
	 * testCreateClassOfQString
	 */
	@Test
	public void testCreateClassOfQString()
	{
		ServiceFactory factory = ServiceFactory.getInstance();
		UserProfile users = factory.create(UserProfile.class);
		assertNotNull(users);
	}//------------------------------------------------

	@Test
	public void testCreateString()
	{
		ServiceFactory factory = ServiceFactory.getInstance();
		assertNotNull(factory.create("userProfileTest"));
	}//------------------------------------------------

	@Test
	void when_invalid_property()
	{
		ServiceFactory factory= ServiceFactory.getInstance();
		assertTrue(factory instanceof ConfigServiceFactory);

		assertThrows(ConfigException.class,()-> factory.create("invalid"));
	}

	/**
	 * Test  createForNames
	 */
	@Test
	public void testCreateForNames()
	{
		ServiceFactory factory = ServiceFactory.getInstance();
		
		UserProfile[] users = new UserProfile[3];
		String[] names = {"userProfileTest","userProfileTest","userProfileTest"};
		factory.createForNames(names, users);
		
		for (UserProfile userProfile : users)
		{
			assertNotNull(userProfile);
		}
	}//------------------------------------------------

	@Test
	public void testCreateStringObjectArray()
	{
		ServiceFactory factory = ServiceFactory.getInstance();
		
	  String email = "email", loginID ="login", firstName = "fn",
		    lastName = "ln";
	   Object[] params = { email, loginID,firstName,lastName};
	   
		UserProfile user = factory.create("userProfileTest", params);
		
		assertNotNull(user);
		assertEquals("email", user.getEmail());
		assertEquals("login", user.getId());
		assertEquals("ln", user.getLastName());
		assertEquals("fn", user.getFirstName());
	}//------------------------------------------------
	/**
	 * Test create string object
	 */
	@Test
	public void testCreateStringObject()
	{
		ServiceFactory factory = ServiceFactory.getInstance();
		
		UserProfile users = factory.create("userProfileTest",null);
		assertNotNull(users);
	}//------------------------------------------------

}
