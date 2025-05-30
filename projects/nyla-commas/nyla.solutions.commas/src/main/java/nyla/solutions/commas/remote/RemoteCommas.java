package nyla.solutions.commas.remote;

import java.io.Serializable;
import java.rmi.RemoteException;

import nyla.solutions.core.data.Envelope;
import nyla.solutions.core.net.rmi.RMI;

public class RemoteCommas implements RemoteCommand<Serializable, Envelope<Serializable>>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2334655654914904628L;

	/**
	 * 
	 * @see nyla.solutions.commas.remote.RemoteCommand#execute(java.io.Serializable)
	 */
	@Override
	public Serializable execute(Envelope<Serializable> input) 
			throws RemoteException
	{	
		return input;
	}// --------------------------------------------------------
	
	/**
	 * Usage: RemoteCommas host port name
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args.length !=  3)
		{
			System.err.println("Usage: java "+RemoteCommas.class.getName()+" host port name");
			System.exit(-1);
		}
		
			try
			{
				//RMI.createRegistry(27001);
				RemoteCommas commas = new RemoteCommas();
				
				String host = args[0];
				int port = Integer.parseInt(args[1]);
				String name = args[2];
				
				RMI rmi = new RMI(host,port);
				
				rmi.rebind(name, commas);
				
				/*RemoteCommand<Serializable, Serializable> stub =
				        (RemoteCommand<Serializable, Serializable>) UnicastRemoteObject.exportObject(commas, 0);
				
				
				Registry registry = LocateRegistry.getRegistry("usxxgreeng3m1.corp.emc.com",27001);
			    registry.rebind("commas", stub);
			    
				    
				    
				RMI.rebind(rmiUrl, commas);
				*/
			}
			catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
	}
	
}
