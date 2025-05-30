package nyla.solutions.commas.json;

import nyla.solutions.commas.Command;
import nyla.solutions.core.data.Envelope;
import nyla.solutions.global.json.JSON;


/**
 * @author Gregory Green
 *
 */
public class JsonAfterCommand implements Command<String,Envelope<Object>>
{

	/**
	 * Provide checks and transformation pass of the 
	 * @param resultAndOriginalArg array 0 - raw function result, 1 = Object[] containing the envelope and function context
	 */
	//@Override
	public String execute(Envelope<Object> env)
	{
		
		return json.toJson(env.getPayload());
			
	}// -----------------------------------------------

	private JSON json = JSON.newInstance();

}
