package nyla.solutions.core.exception.fault;

import nyla.solutions.core.exception.SetupException;

/**
 * A special setup exception related to configuration.
 * 
 * ERROR_CODE: DF011
 * 
 * @author Gregory Green
 *
 */
public class ConfigFaultException extends SetupException
{

	/**
	 * String DEFAULT_ERROR_CODE = "DF011"
	 */
	public static final String DEFAULT_ERROR_CODE = "DF011";
	
	/**
	 * @param message the configuration error message
	 */
	public ConfigFaultException(String message)
	{
		super(message);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
		
	}// --------------------------------------------------------

	/**
	 * 
	 * @param cause the cause
	 */
	public ConfigFaultException(Throwable cause)
	{
		super(cause);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY_NM);
		this.setCode(DEFAULT_ERROR_CODE);
	}// --------------------------------------------------------
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7715029939238736455L;
}
