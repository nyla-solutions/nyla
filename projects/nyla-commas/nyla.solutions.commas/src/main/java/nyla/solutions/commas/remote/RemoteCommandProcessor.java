package nyla.solutions.commas.remote;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import nyla.solutions.core.exception.CommunicationException;


/**
 * Generic runner to execute a RemoteCommand.
 * 
 * This is usage when using the Worker Thread for multi-cast calls
 * 
 * @author Gregory Green
 *
 * @param <ReturnType>
 * @param <InputType>
 */
public class RemoteCommandProcessor<ReturnType extends Serializable,InputType extends Serializable>  implements Runnable, Callable<ReturnType>
{
	/**
	 * 
	 * @param remoteCommand the remote command to execute
	 * @param input the input the pass to the remote command
	 */
	public RemoteCommandProcessor(RemoteCommand<ReturnType,InputType>  remoteCommand,InputType input)
	{
		this.remoteCommand = remoteCommand;
		this.input = input;
	}// --------------------------------------------------------

	/**
	 * Execute the remote command
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		try
		{
			this.results = call();
			
		}
		catch (RemoteException e)
		{
			throw new CommunicationException(e);
		}

	}// --------------------------------------------------------
	@Override
	public ReturnType call() throws RemoteException
	{
		// TODO Auto-generated method stub
		return this.remoteCommand.execute(input);
	}
	
	/**
	 * @return the results
	 */
	public ReturnType getResults()
	{
		return results;
	}



	private final RemoteCommand<ReturnType,InputType> remoteCommand;
	private final InputType input;
	private ReturnType results;
	

}
