package nyla.solutions.commas.remote.partitioning;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommandAttribute;
import nyla.solutions.commas.CommandFacts;
import nyla.solutions.commas.CommasConstants;
import nyla.solutions.commas.annotations.Aspect;
import nyla.solutions.commas.aop.Advice;
import nyla.solutions.commas.remote.RemoteCommand;

//import javax.annotation.concurrent.NotThreadSafe;

import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.exception.CommunicationException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.net.rmi.RMI;
import nyla.solutions.core.patterns.SetUpable;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Organizer;

/**
 * Used int Partition Data Services design implementation
 * for RMI.
 * <ul>
 *  <li>get lookup Id from payload</li>
	 <li>get routing key by lookup Id and commasProperties</li>
	 <li>lookup by routingKey URL</li>
	 <li>configure routing information in Envelope</li>
	 <li>Execute RemoteCommas</li>
	 <li>skip over execute</li>
	 </ul>
	 
 * @author Gregory Green
 *
 */
@Aspect(name=RmiOneRouteAdvice.ADVICE_NAME)
//@NotThreadSafe
public class RmiOneRouteAdvice implements Advice, SetUpable
{
	/**
	 * ADVICE_NAME = "RmiOneRouteAdvice"
	 */
	public static final String ADVICE_NAME = "RmiOneRouteAdvice";
	
	/**
	 * LOOKUP_PROP_ATTRIB_NAME = "lookupPropertyName"
	 */
	public static final String LOOKUP_PROP_ATTRIB_NAME = "lookupPropertyName";
	
	//initialize CommandFacts
	public RmiOneRouteAdvice()
	{
	}// --------------------------------------------------------
	
	@Override
	public void setUp()
	{
		if(this.registry != null)
			return;
		
		
		try
		{
			//this.factory =  ServiceFactory.getInstance(this.getClass());
			
			//this.registry = this.factory.create(PartitionCommasRemoteRegistry.class);
			
			this.registry =  PartitionCommasRemoteRegistry.getRegistry();
		}
		catch (RemoteException e)
		{
			throw new SetupException("Unable to get remote commas registry ",e);
		}
		
	}// --------------------------------------------------------
	
	/**
	 * @return rmiOneRouteCommand
	 * @see nyla.solutions.commas.aop.Advice#getBeforeCommand()
	 */
	@Override
	public Command<?, ?> getBeforeCommand()
	{
		return new RmiOneRouteCommand(this.commandFacts,this.lookupPropertyName);
	}// --------------------------------------------------------

	/**
	 * No after processing
	 * @return null
	 * @see nyla.solutions.commas.aop.Advice#getAfterCommand()
	 */
	@Override
	public Command<?, ?> getAfterCommand()
	{
		return null;
	}// --------------------------------------------------------
	/**
	 * 
	 * @see nyla.solutions.commas.aop.Advice#getFacts()
	 * @return this.commandFacts;
	 */
	@Override
	public  CommandFacts getFacts()
	{
		return this.commandFacts;
	}// --------------------------------------------------------

	/**
	 * Add CommasProxyCommand.ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME
	 * to command attributes
	 * @see nyla.solutions.commas.aop.Advice#setFacts(nyla.solutions.commas.CommandFacts)
	 */
	@Override
	public  void setFacts(CommandFacts facts)
	{
		CommandAttribute advisedSkipMethodInvoke = new CommandAttribute(
				CommasConstants.ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME, 
				Boolean.class.getName(), Boolean.TRUE.toString());
		
		if(facts == null)
		{
			facts = new CommandFacts();
		
			CommandAttribute[] commandAttributes = { advisedSkipMethodInvoke};
		
			facts.setCommandAttributes(commandAttributes);
			
	     }
	
		//process advisedSkipMethodInvoke
		CommandAttribute[] commandAttributes = facts.getCommandAttributes();
			
		if(commandAttributes == null)
		{
				CommandAttribute[] commandAttributesArray = { advisedSkipMethodInvoke};
				
				facts.setCommandAttributes(commandAttributesArray);
		}
		else
		{
			//get lookup key
			for (int i = 0; i < commandAttributes.length; i++)
			{
				if(LOOKUP_PROP_ATTRIB_NAME.equals(commandAttributes[i].getName()))
						this.lookupPropertyName = commandAttributes[i].getValue();
			}
			
			     //add advisedSkipMethodInvoke
				commandAttributes= Organizer.add(advisedSkipMethodInvoke, commandAttributes);
				facts.setCommandAttributes(commandAttributes);
		}

		
		this.commandFacts = facts;
		
	}// --------------------------------------------------------
	/**
	 * Before process to make Remote RMI call and 
	 * by pass local processing
	 * @author Gregory Green
	 *
	 */
	class RmiOneRouteCommand implements Command<Serializable,Envelope<Serializable>>
	{
		public RmiOneRouteCommand(CommandFacts facts,String lookupPropertyName)
		{
			this.commandFacts = facts;
			this.lookupPropertyName = lookupPropertyName;
		}// --------------------------------------------------------
		@Override
		public Serializable execute(Envelope<Serializable> env)
		{
			if(registry == null)
				setUp();
			
			if(this.lookupPropertyName == null || lookupPropertyName.length() == 0)
				throw new SetupException("Missing Command Atttribute:"+LOOKUP_PROP_ATTRIB_NAME+" see "+this.getClass().getName()+"LOOKUP_PROP_ATTRIB_NAMEv");
			
			//get lookup Id from payload
			
			String location = null;
			String lookupId = null;
			try
			{
				//configure routing information in Envelope
				Map<Object,Object> header = env.getHeader();
				
				
				Serializable payload = env.getPayload();
				
				if(header == null)
				{
						header = new HashMap<Object,Object>();
						env.setHeader(header);
				}
				else
				{
					String command = (String)header.get(CommasConstants.COMMAND_NAME_HEADER);
					
					if(command != null)
						return payload; //skip processing (this is a one server call)
				}
				
				
				
				Object lookupIdObject = JavaBean.getProperty(payload, lookupPropertyName);
				
				if (lookupIdObject == null)
					throw new RequiredException("Property:"+lookupPropertyName+" in payload object:"+payload);
				
				lookupId = lookupIdObject.toString();
				
				//get routing key by lookup Id and commasProperties
				 location = registry.whereIs(lookupId, commandFacts);
				
				//lookup by routingKey URL
				RemoteCommand<Serializable, Envelope<Serializable>> remoteCommand = RMI.lookup(new URI(location));
	
				HashMap<Object,Object> newHeader = new HashMap<Object,Object>();
				//put command header
				newHeader.put(CommasConstants.COMMAND_NAME_HEADER, commandFacts.getName());
				newHeader.put(CommasConstants.ALWAYS_EXECUTE_METHOD_HEADER, Boolean.TRUE);
				
				env.setHeader(newHeader);
				
				//Execute RemoteCommas
				Serializable results = remoteCommand.execute (env);

				//Set header to skip local execution
				header.put(CommasConstants.ALWAYS_EXECUTE_METHOD_HEADER, Boolean.FALSE);
				env.setHeader(header);
				
				return results;
				
			}
			
			catch (RemoteException e)
			{
				throw new CommunicationException("Remote communication error LOCATION:"+location+" lookupId:"+lookupId+" ERROR:"+e.getMessage(),e);
			}
			catch (URISyntaxException e)
			{
				throw new SetupException("Invalid URI for LOCATION:"+location+" lookupId:"+lookupId+" ERROR:"+e.getMessage(),e);
			}
			
		}
		private final CommandFacts commandFacts;
		private final String lookupPropertyName;
		
	}// --------------------------------------------------------
	//private ServiceFactory factory;

	private  CommasRemoteRegistry<String,String> registry;
	private CommandFacts commandFacts;
	private String lookupPropertyName = null;
	//private final RmiOneRouteCommand rmiOneRouteCommand;

}
