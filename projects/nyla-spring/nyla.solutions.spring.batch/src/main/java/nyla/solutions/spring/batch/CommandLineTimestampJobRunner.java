package nyla.solutions.spring.batch;

import java.util.Calendar;
import java.util.Date;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.fault.FaultService;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;


/**
 * Automatically add a time stamp to the parameters
 * 
 * Used {@link SpringBootApp}
 * @author Gregory Green
 *
 */
public class CommandLineTimestampJobRunner
{
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
	throws Exception
	{
		if(args.length < 2)
		{
			throw new RequiredException("Usage: java "+CommandLineTimestampJobRunner.class.getName()+" job.xml jobid");
		}
		
		String[] commandLineArgs = new String[args.length+1];
		
		Date start = Calendar.getInstance().getTime();
		
		String timestamp = Text.formatDate(timestampFormat, start);
		
		System.arraycopy(args, 0, commandLineArgs, 0, args.length);
		
		commandLineArgs[args.length] = new StringBuilder().append(timestampParameterName).append("=").append(timestamp).toString();
		
		try
		{
			//Test Service Factory creation
			ServiceFactory.getInstance();
			
			CommandLineJobRunner.main(commandLineArgs);
		}
		catch (Exception e)
		{
			throw handleJobException(e);

		}
		
	}// --------------------------------------------------------
	private static Exception handleJobException(Exception e)
	{
			 
			 //Get fault service
			 if(faultService == null)
			 {
				 if(faultServiceClassName ==null || faultServiceClassName.length() == 0)
					 return e; //not fault service
			    
				 try
				 {
					 faultService = ClassPath.newInstance(faultServiceClassName);
				 }
				 catch(Exception error)
				 {
					 Debugger.printWarn(e);
				 }
			 }
			 
			 if(faultService == null)
				 return e;
			 
			 //use fault service to raise exception			 
			 throw faultService.raise(e);
		
	}// --------------------------------------------------------

	private static FaultService faultService = null;
	private static final String faultServiceClassName = Config.getProperty(CommandLineTimestampJobRunner.class,"faultServiceClassName","");
	private static final String timestampFormat = Config.getProperty(CommandLineTimestampJobRunner.class,"timestampFormat","MM-dd-yyyy HH:mm:ss:SS");
	
	private static final String timestampParameterName = Config.getProperty(CommandLineTimestampJobRunner.class,"timestampParameterName","RUN_TIMESTAMP");
	
}
