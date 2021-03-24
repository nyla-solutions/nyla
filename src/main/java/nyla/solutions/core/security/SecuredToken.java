package nyla.solutions.core.security;

import java.io.Serializable;
import java.security.Principal;

public interface SecuredToken extends Serializable , Principal
{
	/**
	 * 
	 * @return the generated unique token
	 */
	public String getToken();
	
	/**
	 * 
	 * @return get encrypted password
	 */
	public char[] getCredentials();
	
}
