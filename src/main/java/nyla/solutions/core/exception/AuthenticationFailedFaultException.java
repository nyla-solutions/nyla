package nyla.solutions.core.exception;



/**
 * Security related error
 * @author Gregory Green
 *
 */
public class AuthenticationFailedFaultException extends SecurityException
{
	public static final String DEFAULT_ERROR_CODE = "SC0003";
	

	public AuthenticationFailedFaultException(Exception exception, String username)
	{
		super(exception);

		this.setCode(DEFAULT_ERROR_CODE);
		this.username = username;
		
	}// -----------------------------------------------


	public AuthenticationFailedFaultException(String message, String username)
	{
		super(message+" user:"+username);
		this.setCode(DEFAULT_ERROR_CODE);
		this.username = username;
	}// -----------------------------------------------


	public AuthenticationFailedFaultException(String message, Exception e, String username)
	{
		super(message+" user:"+username, e);
		this.setCode(DEFAULT_ERROR_CODE);
		this.username = username;
	}// -----------------------------------------------
	
	/**
	 * 
	 * @param username the user name
	 */
	public AuthenticationFailedFaultException(String username)
	{
		super("Authentication Failed for user:"+username);
		this.setCode(DEFAULT_ERROR_CODE);
		this.username = username;
	}// -----------------------------------------------
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "AuthenticationFailedException [username=" + username
				+ ", toString()=" + super.toString() + "]";
	}// -----------------------------------------------

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}


	private String username = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3062061475584757100L;

}
