package nyla.solutions.core.exception.fault;




/**
 * Generic for missing Required data
 * 
 * @author Gregory Green
 *
 */
public class RequiredFaultException extends FaultException
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3062061475584757100L;

	/**
	 * Default constructor
	 */
	public RequiredFaultException()
	{
		this.setCategory(FaultException.DEFAULT_ERROR_CATEGORY_NM);
		this.setCode("REQ");
		
	}
	/**
	 * 
	 * @param exception the nested exception
	 */
	public RequiredFaultException(Exception exception)
	{
		super(exception);

		this.setCategory(FaultException.DEFAULT_ERROR_CATEGORY_NM);
		this.setCode("DF005");
		
	}

	/**
	 * 
	 * @param message the default message
	 */
	public RequiredFaultException(String message)
	{
		super(message);
		this.setCategory(FaultException.DEFAULT_ERROR_CATEGORY_NM);
		this.setCode("DF005");
	}


}
