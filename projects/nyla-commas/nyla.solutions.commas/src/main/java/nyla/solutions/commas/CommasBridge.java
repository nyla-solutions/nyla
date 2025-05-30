package nyla.solutions.commas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import nyla.solutions.core.data.Envelope;

public abstract class CommasBridge
{

	/**
	 * Assert that the PROGRAM_NAME_HEADER header is added to the header
	 * @param env the envelope
	 */
	protected static final void constructAuthIdentity(Envelope<Serializable> env, String actor)
	{
		//construct header
		Map<Object,Object> headerMap = env.getHeader();
		
		if(headerMap == null)
		{
			headerMap = new HashMap<Object,Object>();
			env.setHeader(headerMap);
		}
		
		//TODO: headerMap.put(Constants.SECURITY_APPLICATION_HEADER, actor);
		
		//TODO: headerMap.put(Constants.VERSION_HEADER, DafVersion.VERSION);
		
	}// -----------------------------------------------
	protected static CommasServiceFactory getCommasFactory()
	{
		return commasFactory;
	}// --------------------------------------------------------
 	private static CommasServiceFactory commasFactory = CommasServiceFactory.getCommasServiceFactory();

}
