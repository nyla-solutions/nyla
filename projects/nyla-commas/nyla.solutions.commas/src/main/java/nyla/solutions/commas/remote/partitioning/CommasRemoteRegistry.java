package nyla.solutions.commas.remote.partitioning;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import nyla.solutions.commas.CommandFacts;

public interface CommasRemoteRegistry<KeyType,LocationType> extends Remote
{
	/**
	 * 
	 * @param key the routing key
	 * @param commandFacts the Command facts
	 * @return
	 * @throws RemoteException
	 */
	LocationType whereIs(KeyType key, CommandFacts commandFacts)
	throws RemoteException;
	
	
	
	void registerKey(KeyType key, CommandFacts commandFacts, LocationType location )
	throws RemoteException;
	
	//TODO: may add to add command facts
	public void registerLocation(LocationType location)
	throws RemoteException;
	
	public Collection<LocationType> listRegisteredLocations()
	throws RemoteException;
	
	/**
	 * Unregister a location
	 * @param location remote all references to the location
	 * @throws RemoteException
	 */
	public void unregisterLocation(LocationType location)
	throws RemoteException;
	
	
	
}
