package nyla.solutions.spring.batch;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import nyla.solutions.core.exception.fault.Fault;
import nyla.solutions.core.exception.fault.FaultFormatTextDecorator;
import nyla.solutions.core.exception.fault.FaultMgr;
import nyla.solutions.core.exception.fault.FaultService;
import nyla.solutions.core.patterns.decorator.ToStringTextDecorator;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.email.EmailCommand;
import nyla.solutions.spring.batch.SpringBatchErrorListener;


@Ignore
public class SpringBatchErrorListenerTest
{
	@Test
	public void testOnProcessErrorIException()
	{
		SpringBatchErrorListener<Object, Object> listener 
		= new SpringBatchErrorListener<Object, Object>();
		
		//The email command can be configured with a fault service
		FaultService faultService = new FaultMgr();
		
		listener.setFaultService(faultService);
		
		//Create Email Command to handle read errors
		EmailCommand<Object,Fault> onProcessCmd = new EmailCommand<Object,Fault>();
		//TODO: listener.setProcessErrorCmd(onProcessCmd);
		
		
		onProcessCmd.setSubject("Spring Batch Error Notification");
		onProcessCmd.setTo("gregory.green@emc.com");

		//The Email Command can be configured with a decorator to format the fault
		FaultFormatTextDecorator htmlEmailFaultDecorator = new FaultFormatTextDecorator();	

		onProcessCmd.setTextDecorator(htmlEmailFaultDecorator);
	
		//The decorator can use a template to embed HTML to format to the sent email message.
		//The following is an example of template string.
		//The ${message}, ${category}, ${code} will be place with the corresponding properties from the fault 
		htmlEmailFaultDecorator.setTemplate(
				         "<div><b>MODULE</b> :${module} </div>"+
				         "<div><b>OPERATION</b> :${operation} </div>"+
				         "<div><b>MESSAGE</b> :${message} </div>"+
				         "<div><b>CATEGORY</b> :${category} </div>"+
				         "<div><b>CODE</b> :${code} </div>"+
				         "<div><b>argument</b> :${argument} </div>"+
				         "<div><b>error strack trace</b> :${errorStackTrace} </div>"+
				         "<div><b>notes</b> :${notes} </div>"
				);
		
		//The argument to fault can have a dedicated decorator
		///The results can be placed in the <div><b>argument</b> :${argument} </div> portion of the above template
		htmlEmailFaultDecorator.setArgumentTextDecorator(new ToStringTextDecorator());
		
		
		//Fault  service can be used to wraps objects into a fault with a code/category
	    IOException e = new IOException();
	    
	    htmlEmailFaultDecorator.setTarget(faultService.raise(e));
	    
	    String results = htmlEmailFaultDecorator.getText();
	    
	    Assert.assertTrue(results != null && results.length() > 0);
		
	    
	    //The following is called by the Spring Batch Framework
		listener.onProcessError("Sample Input argument", e);
	}// --------------------------------------------------------

	@Test
	public void testOnReadErrorException()
	{

		SpringBatchErrorListener<Object, Object> listener = ServiceFactory.getInstance().create("ON_READ_ERROR_LISTENER");
		
		Assert.assertNotNull(listener.getFaultService());
		
		listener.onReadError( new IOException("Testing IO exception"));
	}

	@Test
	public void testOnWriteErrorExceptionListOfQextendsO()
	{
		Assert.fail("Not yet implemented");
	}

}
