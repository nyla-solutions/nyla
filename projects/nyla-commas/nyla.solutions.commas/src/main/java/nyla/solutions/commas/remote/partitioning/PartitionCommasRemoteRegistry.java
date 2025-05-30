package nyla.solutions.commas.remote.partitioning;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import nyla.solutions.commas.CommandFacts;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.net.rmi.RMI;
import nyla.solutions.core.patterns.loadbalancer.LoadBalanceRegistry;
import nyla.solutions.core.patterns.servicefactory.ServiceFactory;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

/**
 * Configuration properties
 * 
 * 
	nyla.solutions.global.patterns.command.remote.partitioning.RemoteCommasRegistry.host=localhost
	nyla.solutions.global.patterns.command.remote.partitioning.RemoteCommasRegistry.port=27001
	nyla.solutions.global.patterns.command.remote.partitioning.RemoteCommasRegistry.name=commasRegistry

 * @author Gregory Green
 *
 */
public class PartitionCommasRemoteRegistry implements CommasRemoteRegistry<String,String>, Remote
{
	public PartitionCommasRemoteRegistry()
	{
		ServiceFactory factory = ServiceFactory.getInstance(PartitionCommasRemoteRegistry.class);
		
		this.loadBalanceRegistry = factory.create(LoadBalanceRegistry.class);
	}// -------------------------------------------------------
	public String whereIs(String key, CommandFacts commandFacts)
	throws RemoteException
	{
		
		//Build property based on region name
		String lookupKey = constructLookupKey(key, commandFacts);
		
		return this.loadBalanceRegistry.lookup(lookupKey);
			
	}// --------------------------------------------------------

	/**
	 * List registered locations
	 * @see nyla.solutions.commas.remote.partitioning.CommasRemoteRegistry#listRegisteredLocations()
	 */
	@Override
	public Collection<String> listRegisteredLocations()
	throws RemoteException
	{
		return this.loadBalanceRegistry.listRegistered();
	}// --------------------------------------------------------
	@Override
	public void unregisterLocation(String location) throws RemoteException
	{
		//remove all keys
		this.loadBalanceRegistry.unregister(location);
	}// --------------------------------------------------------
	
	/**
	 * 
	 * @param type
	 * @param key
	 * @param remote
	 */
	public void registerKey(String key, CommandFacts commandFacts, String location )
	throws RemoteException
	{
		this.loadBalanceRegistry.register(key, location);
	}// --------------------------------------------------------
	/**
	 * Register a location
	 * @see nyla.solutions.commas.remote.partitioning.CommasRemoteRegistry#registerLocation(java.lang.Object)
	 */
	public void registerLocation(String location)
	throws RemoteException
	{
		loadBalanceRegistry.register(location);
	}// --------------------------------------------------------

	public static CommasRemoteRegistry<String,String> getRegistry()
	throws RemoteException
	{
		RMI rmi = new RMI(host,port);
		try
		{
			CommasRemoteRegistry<String,String> remote = rmi.lookup("commasRegistry");
				
			return remote;
		}
		catch (Exception e)
		{
			throw new ConnectionException("name:"+name+" ERROR:"+e.getMessage()+" rmi"+Debugger.toString(rmi.list()),e);
		}
	}// --------------------------------------------------------

	/**
	 * 
	 * @param args 0=host 1=port 2=name
	 */
	public static void main(String[] args)
	{
		if(args.length != 3)
		{
			System.err.println("Usage "+PartitionCommasRemoteRegistry.class.getSimpleName()+" host port name");
			return;
		}
		
		String host =args[0];
		int port = Integer.parseInt(args[1]);
		String name = args[2];
		
		startRegistry(host,port, name);
	}// --------------------------------------------------------
	/**
	 * Starts the partition commas registry
	 * @param host the host name where the RMI registry is running
	 * @param port the port the RMI port
	 * @param name the name of the PartitionCommasRemoteRegistry to registerb
	 */
	public static void startRegistry(String host,int port, String name)
	{
		try
		{
			 
			RMI rmi = new RMI(host,port);
			PartitionCommasRemoteRegistry registry = new PartitionCommasRemoteRegistry();
			
			rmi.rebind(name, registry);
		
		}
		catch (Exception e)
		{
			Debugger.printError(e);
		}
	}// --------------------------------------------------------
	
	private String constructLookupKey(String key, CommandFacts commandFacts)
	{
		try
		{
			return new StringBuilder(commandFacts.getTargetName()).append(".").append(key).toString();
		}
		catch(NullPointerException e)
		{
			throw new RequiredException("key:"+key+" commandFacts(region):"+commandFacts);
		}
	}// --------------------------------------------------------

	private static final String host = Config.getProperty(PartitionCommasRemoteRegistry.class,"host");
	private static final int port = Config.getPropertyInteger(PartitionCommasRemoteRegistry.class,"port").intValue();
	private static final String name = Config.getProperty(PartitionCommasRemoteRegistry.class,"name");
	private final LoadBalanceRegistry<String, String> loadBalanceRegistry;
	
}
