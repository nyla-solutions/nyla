package nyla.solutions.commas.json;

import java.util.Map;

import com.google.gson.JsonParseException;

import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommasConstants;
import nyla.solutions.commas.CommasServiceFactory;
import nyla.solutions.commas.ContentType;
import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.exception.fault.FormatFaultException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.global.json.JSON;

/**
 * Used with the JsonAdvice and envelope to convert payloads to the Object
 * @author Gregory Green
 *
 */
public class JsonBeforeCommand implements Command<Object,Envelope<Object>>
{	
	
	/**
	 * Setup the before processing 
	 */
	public JsonBeforeCommand()
	{		
		json = JSON.newInstance();
	}// -----------------------------------------------

	/**
	 * Convert the Payload of an envelope to a Java object
	 * @see nyla.solutions.commas.Command#execute(java.lang.Object)
	 */
	public Object execute(Envelope<Object> envelope)
	{		
		Map<Object, Object> header = envelope.getHeader();
		
		Object payload = envelope.getPayload();
		
		if(header == null)
			return payload;
		
		//TODO: Check version 
		
		//Get function 
		String cmdName  = (String)header.get(CommasConstants.COMMAND_NAME_HEADER);
		
		//Get "Content-Type"       
		ContentType contentType = (ContentType)header.get(CommasConstants.CONTENT_TYPE_HEADER);
		
		if(!ContentType.JSON.equals(contentType))
		     return envelope; //not a JSON string so ignore

		//Convert 
		//Get first class for JSON string
		String jsonText = (String)payload;
				   
		try
		{
			CommasServiceFactory factory = CommasServiceFactory.getCommasServiceFactory();
			
			payload = json.fromJson(jsonText, 
							factory.getCommandFacts(cmdName)
							   .getArgumentClassInfo().getBeanClass());
			
			//TODO: may need to save original payload
		    
			return payload;
		}
		catch(JsonParseException e)
		{
					   Debugger.printError(e);
					  FormatFaultException exp = new FormatFaultException(jsonText+" ERROR:"+e.getMessage(),e);
					  throw exp;
	    }

	}// --------------------------------------------------------

	private transient JSON json = null;
}
