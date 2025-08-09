/**
 * 
 */
package nyla.solutions.core.exception;

/**
 * @author Gregory Green
 *
 */
public class ConcurrencyException extends SystemException
{
	public static final String DEFAULT_ERROR_CODE = "CONCUR001";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8792691385402410012L;

	/**
	 * 
	 */
	public ConcurrencyException()
	{
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param message
	 */
	public ConcurrencyException(String message)
	{
		super(message);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public ConcurrencyException(String message, Throwable throwable)
	{
		super(message, throwable);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param throwable
	 */
	public ConcurrencyException(Throwable throwable)
	{
		super(throwable);
		this.setCode(DEFAULT_ERROR_CODE);
	}

}
