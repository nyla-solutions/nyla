package nyla.solutions.email;

import java.util.function.Function;

import nyla.solutions.core.patterns.decorator.TextDecorator;
import nyla.solutions.core.util.Config;

/**
 * @author Gregory Green
 * @param <ReturnType> the return type
 * @param <DecoratorType>  the wrapped object 
 *
 */
public class EmailCommand<ReturnType,DecoratorType> implements Function<DecoratorType,Object>
{
	/**
	 * Send the mail
	 */
	@Override
	public ReturnType apply(DecoratorType input)
	{
		textDecorator.setTarget(input);
		
		
		try(Email email = new Email())
		{
			email.sendMail(to, subject, textDecorator.getText());
		}
		catch(RuntimeException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e.getMessage(),e);
		}
		return null;
	}// --------------------------------------------------------
	
	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = Config.interpret(subject);
	}
	/**
	 * @return the to
	 */
	public String getTo()
	{
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to)
	{
		this.to = Config.interpret(to);
	}
	/**
	 * @return the textDecorator
	 */
	public TextDecorator<DecoratorType> getTextDecorator()
	{
		return textDecorator;
	}
	/**
	 * @param textDecorator the textDecorator to set
	 */
	public void setTextDecorator(TextDecorator<DecoratorType> textDecorator)
	{
		this.textDecorator = textDecorator;
	}


	private String subject;
	private String to;
	private TextDecorator<DecoratorType> textDecorator;

}
