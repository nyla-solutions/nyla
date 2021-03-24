package nyla.solutions.core.security.data;

import nyla.solutions.core.data.Textable;

import java.io.Serializable;

public interface Permission extends Serializable, Textable
{

	/**
	 * 
	 * @param permission the permission to check
	 * @return true if other permission allowed
	 */
	public boolean isAuthorized(Permission permission);
	
}
