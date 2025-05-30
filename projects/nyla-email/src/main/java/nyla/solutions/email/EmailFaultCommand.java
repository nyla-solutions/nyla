package nyla.solutions.email;

import java.io.IOException;

import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.fault.Fault;
import nyla.solutions.core.exception.fault.FaultFormatTextDecorator;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Text;

/**
 * <pre>
 * The Email fault command that initializes a FaultFormatTextDecorator.
 * 
 * The following are sample configuration properties;
 * 
 * solutions.global.exception.EmailFaultCommand.TEMPLATE_NAME=ASAP_ERROR
 * solutions.global.exception.EmailFaultCommand.TO=gregory.green@emc.com
 * solutions.global.exception.EmailFaultCommand.SUBJECT=ASAP ERROR
 * </pre>
 * @author Gregory Green
 *
 */
public class EmailFaultCommand extends EmailCommand<Object, Fault>
{
	/**
	 * TO = Config.getProperty(EmailFaultCommand.class,"TO");
	 */
	public static final String TO = Config.getProperty(EmailFaultCommand.class,"TO");
	
	/**
	 * SUBJECT = Config.getProperty(EmailFaultCommand.class,"SUBJECT")
	 */
	public static final String SUBJECT = Config.getProperty(EmailFaultCommand.class,"SUBJECT");
	
	
	/**
	 * TEMPLATE_NAME = Config.getProperty(EmailFaultCommand.class,"TEMPLATE_NAME")
	 */
	public static final String TEMPLATE_NAME = Config.getProperty(EmailFaultCommand.class,"TEMPLATE_NAME");
	
	
	/**
	 * Initialize
	 */
	public EmailFaultCommand()
	{
		try
		{
			FaultFormatTextDecorator htmlEmailFaultDecorator = new FaultFormatTextDecorator();
			htmlEmailFaultDecorator.setTemplate(Text.loadTemplate(TEMPLATE_NAME));
			
			super.setTextDecorator(htmlEmailFaultDecorator);
			
			super.setSubject(SUBJECT);
			super.setTo(TO);
			
		}
		catch(IOException e)
		{
			throw new ConfigException(e);
		}
	}// --------------------------------------------------------b

}
