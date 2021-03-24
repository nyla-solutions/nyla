package nyla.solutions.core.exception.fault;



/**
 * ERROR Code: DF008
 * Connection closed error
 * @author Gregory Green
 *
 */
public class ClosedFaultException extends FaultException
{
	/**
	 * Constructor
	 */
	public ClosedFaultException()
	{
	}// -----------------------------------------------
	/**
	 * Constructor
	 * @param exception the exception
	 */
	public ClosedFaultException(Exception exception)
	{
		super(exception);

		this.setCategory(FaultException.DEFAULT_ERROR_CATEGORY_NM);
		this.setCode("DF008");
		
	}// -----------------------------------------------


	/**
	 * Constructor
	 * @param message the error message
	 * @param e the root exception
	 */
	public ClosedFaultException(String message, Exception e)
	{
		super(message, e);
		this.setCategory(FaultException.DEFAULT_ERROR_CATEGORY_NM);
		this.setCode("DF008");
	}// -----------------------------------------------
	
	private static final long serialVersionUID = -3062061475584757100L;

}
