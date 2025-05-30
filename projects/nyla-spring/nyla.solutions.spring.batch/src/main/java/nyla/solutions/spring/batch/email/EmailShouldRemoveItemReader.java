package nyla.solutions.spring.batch.email;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import nyla.solutions.email.Email;

/**
 * @author Gregory Green
 *
 */
public class EmailShouldRemoveItemReader implements ItemReader<String>
{
	/**
	 * 
	 */
	@Override
	public String read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException
	{
		if(i == null)
		{
			Collection<String> emails = email.shouldRemoveEmails();	
			if(emails == null)
				return null;
			
			this.i = emails.iterator();
		}
		
		if(i.hasNext())
			return i.next();
		return null;
	}
	private Iterator<String> i= null;
	private Email email = new Email();
}
