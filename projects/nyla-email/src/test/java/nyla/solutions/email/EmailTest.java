package nyla.solutions.email;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import nyla.solutions.core.util.Config;

@Ignore
public class EmailTest
{
	/**
	 * Test for the authentications of the email information
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	@Test
	public void testIsAuthenicationRequired()
	throws Exception
	{
		
		
		System.setProperty("mail.auth.required", "true");
		Assert.assertEquals(System.getProperty("mail.auth.required"), "true");
		
		System.setProperty(EmailTags.MAIL_AUTHENICATION_REQUIRED_PROP, "true");
		Config.reLoad();
		Email email = new Email();
		
		Assert.assertTrue(Config.getPropertyBoolean(EmailTags.MAIL_AUTHENICATION_REQUIRED_PROP));
		
		Assert.assertTrue(email.isAuthenicationRequired());
		
		System.setProperty(EmailTags.MAIL_AUTHENICATION_REQUIRED_PROP, "false");
		Config.reLoad();
		email = new Email();
		
		Assert.assertTrue(!email.isAuthenicationRequired());
	}//------------------------------------------------
	
	@SuppressWarnings("resource")
	@Test
	public void testSendEmail()
	{
		String to = "green_gregory@yahoo.com";
		String subject = "Test";
		String messageBody = "<b>Hello World</b>";
		
		Email email = new Email();
		email.setMailFromUser(Config.getProperty("junit.from.user"));
		email.sendMail(to, subject, messageBody);
	}

}
