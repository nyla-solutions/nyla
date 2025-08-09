package nyla.solutions.core.net.rmi;

//import java.rmi.Naming;

import nyla.solutions.core.data.Identifier;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.net.URI;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static nyla.solutions.core.util.Config.settings;


/**
 * <pre>
 * Starting the Server

Before starting the compute engine, you need to start the RMI registry. 
The RMI registry is a simple server-side bootstrap naming facility that enables remote clients 
to obtain a reference to an initial remote object. It can be started with the rmiregistry command. 
Before you execute rmiregistry, you must make sure that the shell or window in which you will run 
rmiregistry either has no CLASSPATH environment variable set or has a CLASSPATH environment variable that does not include the path to any classes that you want downloaded to clients of your remote objects.

To start the registry on the server, execute the rmiregistry command. This command produces no output and is typically run in the background. For this example, the registry is started on the host mycomputer.

Microsoft Windows (use javaw if start is not available):

start rmiregistry

By default, the registry runs on port 1099. 
To start the registry on a different port, specify the port number on the command line. 
Do not forget to unset your CLASSPATH environment variable.

Microsoft Windows:

start rmiregistry 2001


 rmi = new RMI("mycompany.com",27001);
 
 Remote command = rmi.lookup("commas");
</pre>
 * 
 * @author Gregory Green
 *
 */
public class RMI
{
	private final String host;
	private final int port;
	private final Registry registry;

	/**
	 * 
	 * @param host the registry host
	 * @param port the registry port
	 * @throws RemoteException
	 */
	public RMI(String host, int port)
	throws RemoteException
	{
		this(host,port,getRegistry(host, port));

	}// --------------------------------------------------------

	RMI(String host, int port, Registry registry)
	{
		this.host = host;
		this.port = port;
		this.registry = registry;
	}

	/**
    * 
    * @param <T> the lookup value type
    * @param name the rmi Url rmi://localhost:port/serviceName
    * @return the RemoteObject
    */
   @SuppressWarnings("unchecked")
   public <T> T lookup(String name)   
   {
	try
	{

        return (T)registry.lookup(name);
	}
	catch(Exception e)
	{
		String [] list = null;
		try
		{
			list = registry.list();
		}
		catch(Exception exp)
		{
			Debugger.printWarn(this,exp.getMessage());
		}
	   throw new ConnectionException("Cannot find:"+name+" in list "+Debugger.toString(list)+" rmi//:"+host+":"+port+"/"+name+" ERROR:"+e.getMessage(),e);
	}	
   }// ---------------------------------------------- 
   
   @SuppressWarnings("unchecked")
 public static <T> T lookup(URI rmiUrl)   
   {
	try
	{
	   return (T)Naming.lookup(rmiUrl.toString());
	}
	catch(Exception e)
	{
	   throw new ConnectionException(rmiUrl+" ERROR:"+Debugger.stackTrace(e));
	}	
   }// ---------------------------------------------- 
   /**
    * 
    * @param name the service name
    * @param remote the remote object to bind
    */
   public void rebind(String name,Remote remote)
   {
	try
	{

		Remote stub = UnicastRemoteObject.exportObject(remote, 0);
		
		this.registry.rebind(name, stub);
		
		Debugger.println(RMI.class,"Binded object:"+remote+" host:"+host+" port:"+port);
		
	} 
	catch (Exception e)
	{
	   throw new ConfigException(Debugger.stackTrace(e));
	}	
   }// ----------------------------------------------
   public String[] list ()
   {
	  try
		{
			return this.registry.list();
		}
	
		catch (Exception e)
		{
			return null;
		}
   }

   /**
    * Bind all object.
    * Note that the rmiUrl in located in the config.properties
    * 
    * ${class}.rmiUrl
    * 
    * Example: solutions.test.RemoteObjectText.bind.rmi.url=rmi://localhost:port/serviceName
    * 
    * Note that is the remote object is an instance of Identifier, 
    * the object will be binded based on its &quot;id&quot; property if the value is provided
    * 
    * @param remotes the object to rebind
    */
   public void rebind(Remote[] remotes)
   {
	String rmiUrl = null;
	//loop thru remote objects
	for (int i = 0; i < remotes.length; i++) 
	{
	   
	       //use is if instance of Identifier
	      if(remotes[i] instanceof Identifier 
	         && !Text.isNull(((Identifier)remotes[i]).getId()))
	      {
	         rmiUrl = ((Identifier)remotes[i]).getId();
	      }
	      else
	      {
		       //get rmiUrl
			rmiUrl = settings().getProperty(remotes[i].getClass(), "bind.rmi.url");
	         
	      }
		
		
		//rebind
		rebind(rmiUrl,remotes[i]);
	   
	}
   }// ----------------------------------------------
   /**
    * Create a new local registry on a local port
    * @return registry using configuration properties nyla.solutions.core.net.rmi.RMI.host nyla.solutions.core.net.rmi.RMI.port
    * @throws RemoteException unknown occurs RMI.
    */

   public static Registry getRegistry()
   throws RemoteException
	{
			return LocateRegistry.getRegistry(settings().getProperty(RMI.class,"host"),
					settings().getPropertyInteger(RMI.class,"port").intValue());
	}// ----------------------------------------------
   
   public static Registry getRegistry(String host, int port)
   throws RemoteException
   {
	return LocateRegistry.getRegistry(host, port);
   }// ----------------------------------------------
   public static void startRmiRegistry(int port)
   throws RemoteException
   {
	   LocateRegistry.createRegistry(port);
   }// --------------------------------------------------------
   public static void main(String[] args)
	{
	   
		try
		{
			RMI.startRmiRegistry(Integer.parseInt(args[0]));
			System.in.read();
		}
		catch (Exception e)
		{
		
			e.printStackTrace();
		}
	}

   
}
