package nyla.solutions.commas.remote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCommand<ReturnType extends Serializable,InputType extends Serializable> 
extends Remote, Serializable
{
	ReturnType execute(InputType input)
	throws RemoteException;
	
}
