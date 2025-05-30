package nyla.solutions.commas.remote.partitioning;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import nyla.solutions.commas.CatalogClassInfo;
import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommandAttribute;
import nyla.solutions.commas.CommandFacts;
import nyla.solutions.commas.CommasConstants;
import nyla.solutions.commas.annotations.Aspect;
import nyla.solutions.commas.aop.Advice;
import nyla.solutions.commas.remote.RemoteCommand;
import nyla.solutions.commas.remote.RemoteCommandProcessor;
import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.exception.CommunicationException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.net.rmi.RMI;
import nyla.solutions.core.patterns.SetUpable;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.workthread.ExecutorBoss;
import nyla.solutions.core.util.Debugger;
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

  Advice reference
  
@COMMAS
public class RealSingleRouteCommand
{  	
	@CMD(advice=RmiAllRoutesAdvice.ADVICE_NAME)
	public Collection<User> findUsersEveryWhere(Criteria criteria)
	{}
}

 * @author Gregory Green
 *
 */
@Aspect(name=RmiAllRoutesAdvice.ADVICE_NAME)
//@NotThreadSafe
public class RmiAllRoutesAdvice implements Advice, SetUpable
{
	private enum RmiAllRoutesAdviceReturnType
	{
		collection,
		set,
		tree,
		paging,
		single
	};
	
	/**
	 * ADVICE_NAME = "RmiOneRouteAdvice"
	 */
	public static final String ADVICE_NAME = "RmiAllRoutesAdvice";
	
	
	//initialize CommandFacts
	public RmiAllRoutesAdvice()
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
		return new RmiAllRoutesCommand(this.commandFacts, this.returnType);
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
	public CommandFacts getFacts()
	{
		return this.commandFacts;
	}// --------------------------------------------------------

	/**
	 * Add CommasProxyCommand.ADVISED_SKIP_METHOD_INVOKE_CMD_ATTRIB_NAME
	 * to command attributes
	 * @see nyla.solutions.commas.aop.Advice#setFacts(nyla.solutions.commas.CommandFacts)
	 */
	@Override
	public void setFacts(CommandFacts facts)
	{
		this.returnType = RmiAllRoutesAdviceReturnType.single;
		
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
			     //add advisedSkipMethodInvoke
				commandAttributes= Organizer.add(advisedSkipMethodInvoke, commandAttributes);
				facts.setCommandAttributes(commandAttributes);
		}
		
		this.commandFacts = facts;
		
		//Determine return class type
		CatalogClassInfo classInfo = this.commandFacts.getReturnClassInfo();
		
		String className = classInfo.getBeanClassName();
		if(Collection.class.getName().equals(className) ||
		   ArrayList.class.getName().equals(className)	||
		   List.class.getName().equals(className))
			returnType = RmiAllRoutesAdviceReturnType.collection;
		else if(Set.class.getName().equals(className))
			returnType = RmiAllRoutesAdviceReturnType.set;
		else if(Paging.class.getName().equals(className))
			returnType = RmiAllRoutesAdviceReturnType.paging;
		else if(TreeSet.class.getName().equals(className))
			returnType = RmiAllRoutesAdviceReturnType.tree;
		
	}// --------------------------------------------------------
	/**
	 * Before process to make Remote RMI call and 
	 * by pass local processing
	 * @author Gregory Green
	 *
	 */
	class RmiAllRoutesCommand implements Command<Serializable,Envelope<Serializable>>
	{
		RmiAllRoutesCommand(CommandFacts facts,RmiAllRoutesAdviceReturnType returnType)
		{
			this.commandFacts = facts;
			this.returnType = returnType;
		}// --------------------------------------------------------
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Serializable execute(Envelope<Serializable> env)
		{
			if(registry == null)
				setUp();
					
			//get lookup Id from payload
			
			Collection<String> locations = null;
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
				
				//get all routes
				 locations = registry.listRegisteredLocations();
				 
				
					
				if(locations == null || locations.isEmpty())
						throw new SetupException("No register location found. Please start a remote commas server.");
					
					
				 //get input 	
					
				HashMap<Object,Object> newHeader = new HashMap<Object,Object>();
				
				//put command header
				newHeader.put(CommasConstants.COMMAND_NAME_HEADER, commandFacts.getName());
				newHeader.put(CommasConstants.ALWAYS_EXECUTE_METHOD_HEADER, Boolean.TRUE);
					
				env.setHeader(newHeader);
				 
				 RemoteCommand<Serializable, Envelope<Serializable>> remoteCommand;
				 
				 ArrayList<Callable<Serializable>> callQueue = new ArrayList<Callable<Serializable>>();
				 
				 for (String location : locations)
				 {
					 remoteCommand = RMI.lookup(new URI(location));
					 
					 callQueue.add(new RemoteCommandProcessor<Serializable,Envelope<Serializable>>(remoteCommand, env));
				 }
				 
			
				 //start all processing
				 
				 ExecutorBoss boss = new ExecutorBoss(callQueue.size());
				 
				Collection<Serializable> results = boss.startWorking(callQueue);
				
				//Set header to skip local processing
				header.put(CommasConstants.ALWAYS_EXECUTE_METHOD_HEADER, Boolean.FALSE);
				env.setHeader(header);
				
				//Execute RemoteCommas
				
				if(results == null || results.isEmpty())
					return null;
				
			    if(RmiAllRoutesAdviceReturnType.single.equals(returnType))
			    {
			    	//return first record
			    	return results.iterator().next();
			    }
			    
			    
			    Collection<Serializable> flattedCollection = null;
			    
			    switch(returnType)
			    {
			    	case tree: flattedCollection = new TreeSet<Serializable>();break;
			    	case set: flattedCollection = new HashSet<Serializable>(results.size());break;
			    	case paging: return (Serializable)Organizer.flattenPaging((Collection)results);
			    	default: flattedCollection = new HashSet<Serializable>(results.size());
			    }
				
			    Organizer.flatten((Collection)results, flattedCollection);
			    
			    return (Serializable)flattedCollection;

			}
			catch (URISyntaxException e)
			{
				throw new SetupException("Invalid URI for in LOCATIONs:"+Debugger.toString(locations)+" ERROR:"+e.getMessage(),e);
			}
			catch (RemoteException e)
			{
				throw new CommunicationException("Remote communication error LOCATIONs:"+Debugger.toString(locations)+" ERROR:"+e.getMessage(),e);
			}
			
		}
		private final CommandFacts commandFacts;
		private final RmiAllRoutesAdviceReturnType returnType;    
		
	}// --------------------------------------------------------
	//private ServiceFactory factory;

	private  CommasRemoteRegistry<String,String> registry;
	private CommandFacts commandFacts;
	private RmiAllRoutesAdviceReturnType returnType = RmiAllRoutesAdviceReturnType.single;
}
