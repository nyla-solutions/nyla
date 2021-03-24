package nyla.solutions.core.exception.fault;




/**
 * Security related error
 * ERROR
 * @author Gregory Green
 *
 */
public class FormatFaultException extends FaultException
{
	/**
	 * Constructor
	 */
	public FormatFaultException()
	{
		init();
		
	}// -----------------------------------------------

	/**
	 * Constructor
	 * @param exception the exception
	 */
	public FormatFaultException(Exception exception)
	{
		super(exception);

		init();
		
	}// -----------------------------------------------

	/**
	 * Constructor
	 * @param message the format message
	 */
	public FormatFaultException(String message)
	{
		super(message);
		init();
	}// -----------------------------------------------
	/**
	 * 
	 * @param message the message
	 * @param cause the exception cause
	 */
	public FormatFaultException(String message, Throwable cause)
	{
		super(message,cause);
		init();
	}// -----------------------------------------------
	private void init()
	{
		this.setCategory(FaultException.DEFAULT_ERROR_CATEGORY_NM);
		this.setCode("DF006");
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3062061475584757100L;

}
