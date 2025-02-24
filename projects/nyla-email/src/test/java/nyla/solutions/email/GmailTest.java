package nyla.solutions.email;

import nyla.solutions.core.util.Cryption;
import org.junit.BeforeClass;
import org.junit.Test;

import nyla.solutions.core.util.Config;

import java.nio.file.Paths;

public class GmailTest
{
	@BeforeClass
	public static void setUp()
	{
		System.setProperty(Config.SYS_PROPERTY, Paths.get("src/test/resources/gmail.properties").toString());
		Config.reLoad();
	}

	@SuppressWarnings("resource")
	@Test
	public void testSendEmail()
	throws Exception
	{
		Cryption cryption = new Cryption();
		System.out.println(cryption.encryptText(""));
		String to = "green_gregory@yahoo.com";
		String subject = "Test";
		String messageBody = "<b>Hello World</b>";
		
		Email email = new Email();
		email.setMailFromUser(Config.getProperty("junit.from.user"));
		email.sendMail(to, subject, messageBody);
	}

}
