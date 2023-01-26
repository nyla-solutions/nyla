package nyla.solutions.core.exception;

import nyla.solutions.core.exception.fault.FaultException;



/**
 * Security related error
 * @author Gregory Green
 * @version 1.0
 *
 */
public class SecurityException extends FaultException
{
	public static final String DEFAULT_ERROR_CODE = "SC0000";
	public static final String DEFAULT_ERROR_CATEGORY = "SECURITY";
	
	public SecurityException()
	{
		super("Security Exception");
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	public SecurityException(Throwable exception)
	{
		super(exception);

		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
		
	}

	/**
	 * 
	 * @param message the message error
	 */
	public SecurityException(String message)
	{
		super(message);
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	public SecurityException(String message, Throwable e)
	{
		super(message, e);
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	private static final long serialVersionUID = -3062061475584757100L;

}
