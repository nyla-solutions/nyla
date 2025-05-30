package nyla.solutions.commas.json;

import java.io.Serializable;
import java.util.Map;

import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommasBridge;
import nyla.solutions.commas.CommasConstants;
import nyla.solutions.commas.ContentType;
import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.util.Config;
import nyla.solutions.global.json.JSON;



/**
 * <pre>
 * The JSON Bridge will provide a way for JSON based applications to call commands.
 * The JSON Bridge can be executed from a Java based client application. 
 * 
 * The JSON Bridge will allow a  Adapter to simply pass the data represented as JSON to the JSON Bridge. It uses the Grid Enterprise Data Integration to plug-in custom JSON transformation. This custom transformation will convert the JSON to and from grid Java objects.
 * Design Benefits
 * 	- Allows JSON client applications to use existing JSON data to make function calls
 * 	- Minimized changes needed to the clients for data changes
 * 	- Reuses the Command bridge design pattern
 * 
 * @author Gregory Green
 * @author Rick Farmer
 * </pre>
 */
public class JsonBridge extends CommasBridge
{
	
	/**
	 * Execute a JSON based function
	 * @param functionName the function to execute
	 * @param json the JSON file
	 * @return the JSON result
	 */
	public static String executeCommand(String commandName, String jsonText)
	{	
		
		//Create envelope
		Envelope<Serializable> env = new Envelope<Serializable>();
		constructAuthIdentity(env,actor);
		
		env.setPayload(jsonText);
		
		Map<Object,Object> header = env.getHeader();
		
		//add JSON content type
		header.put(CommasConstants.COMMAND_NAME_HEADER, commandName);
		header.put(CommasConstants.CONTENT_TYPE_HEADER, ContentType.JSON);
		
		Command<Object,Object> cmd = getCommasFactory().createCommand(commandName);
			
		Object response = cmd.execute(env);
		
		if(response instanceof String)
			return (String)response;
		
		return json.toJson(response);
		
	}// -----------------------------------------------
	private static String actor  = Config.getProperty(JsonBridge.class,"actor","json-bridge");
	private static JSON json = JSON.newInstance();


}
