package nyla.solutions.commas.remote.partitioning;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import nyla.solutions.commas.Command;
import nyla.solutions.commas.CommasConstants;
import nyla.solutions.commas.CommasServiceFactory;
import nyla.solutions.commas.MacroCommand;
import nyla.solutions.commas.remote.RemoteCommand;
import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.net.rmi.RMI;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;



/**
 * 
 * RMI Server wrapper for execute COMMANDS using the COMMAS framework.
 * 
 * This can be execute stand alone.
 * 
 * Example:
 * 
 * java -Xms512 -Xmx3gb nyla.solutions.global.patterns.command.remote.partitioning.RemoteCommasServer wpp commasRegistry localhost 27001 solutions.office.msoffice.excel.patterns.ExcelFileDirDbLoaderCommand.execute
 * 
 * @author Gregory Green
 *
 */
public class RemoteCommasServer implements RemoteCommand<Serializable, Envelope<Serializable>>
{	
	/**
	 * serialVersionUID = 5429390116183075302L
	 */
	private static final long serialVersionUID = 5429390116183075302L;

	/**
	 * Execute a Command based on Envelope HEADER value "cmd"
	 * @see nyla.solutions.commas.remote.RemoteCommand#execute(java.io.Serializable)
	 */
	public Serializable execute(Envelope<Serializable> env) 
	throws java.rmi.RemoteException 
	{
		try
		{
			Map<Object,Object> header = env.getHeader();
			
			String commandName = null;
			try
			{
			   commandName = (String)header.get(CommasConstants.COMMAND_NAME_HEADER);
			   
			   if(commandName == null)
					throw new RequiredException("Envelope Header:"+CommasConstants.COMMAND_NAME_HEADER+" required");
			   
			}
			catch(NullPointerException e)
			{
				throw new RequiredException("Missing Envelope header "+CommasConstants.COMMAND_NAME_HEADER);
			}
			
			
			
			CommasServiceFactory factory = CommasServiceFactory.getCommasServiceFactory();
			

			
			Command<Serializable,Envelope<Serializable>> cmd = factory.createCommand(commandName);
			
			return cmd.execute(env);
		}
		catch (RuntimeException e)
		{
			Debugger.printError(e);
			throw e;
		}
	}// --------------------------------------------------------
	/**
	 * Run the remote commas server as a standalone Java application
	 * @param args the input arguments [0]=name [1]registryName [2]=host [3]=port (loadCommandsSepartedBy|)* (cmdArgs)*"
	 */
	public static void main(String[] args)
	{
		if(args.length < 4 )
		{
			System.out.println("Usage java "+RemoteCommasServer.class.getName()+" name registryName host port (loadCommandsSepartedBy|)* (cmdArgs)*");
			return;
		}
		
		String name = args[0];
		
		String registryName = args[1];
		
		String host = args[2];
		
		int port = Integer.parseInt(args[3]);
		
		String commas = null;
		if(args.length > 4)
		{
			commas = args[4];
		}
		
		startServer(host,port,name,registryName,commas);
		
	}// --------------------------------------------------------
	public static void startServer(String host, int port, String name, String registryName, String commas)
	{
		try
		{
			
			Collection<?> collection = CommasServiceFactory.getCommasServiceFactory().getCommasInfos();
			
			if(collection == null || collection.isEmpty())
				throw new RuntimeException("No commands registered with the @COMMAS annotation");
			
			
			//TODO: execute startup commands
			if(commas != null)
			{
				
				MacroCommand<Object,String[]> macroCmd = CommasServiceFactory.getCommasServiceFactory().createCommandMacro(Text.split(commas, "|"));
				macroCmd.execute(null);
			}
			

			//RMI.startRmiRegistry(port);
		
			Remote remote = new RemoteCommasServer();
			
			RMI rmi =new RMI(host,port);
				
			rmi.rebind(name, remote);

			CommasRemoteRegistry<?, String> registry = rmi.lookup(registryName);
			
			//rmi.rebind(registryName, registry);
			
			registry.registerLocation(new StringBuilder("rmi://").append(host).append(":").append(port).append("/").append(name).toString());
			
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}// --------------------------------------------------------
	
 
}
