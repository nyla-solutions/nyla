package nyla.solutions.core.exception;

/**
 * A special version setup exception related to configuration.
 * 
 * ERROR_CODE: DF012
 * 
 * @author Gregory Green
 *
 */
public class VersionException extends SetupException
{

	/**
	 * String DEFAULT_ERROR_CODE = "DF012"
	 */
	public static final String DEFAULT_ERROR_CODE = "DF012";
	
	/**
	 * @param message the configuration error message
	 */
	public VersionException(String message)
	{
		super(message);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY_NM);
		this.setCode(DEFAULT_ERROR_CODE);
		
	}// --------------------------------------------------------

	/**
	 * 
	 * @param cause the cause
	 */
	public VersionException(Throwable cause)
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
