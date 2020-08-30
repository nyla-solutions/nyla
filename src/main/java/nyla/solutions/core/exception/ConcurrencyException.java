/**
 * 
 */
package nyla.solutions.core.exception;

import java.util.Map;

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
	 * @param messsage
	 */
	public ConcurrencyException(String messsage)
	{
		super(messsage);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param arg0
	 * @param throwable
	 */
	public ConcurrencyException(String arg0, Throwable throwable)
	{
		super(arg0, throwable);
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

	/**
	 * @param aid
	 * @param message
	 */
	public ConcurrencyException(int aid, String message)
	{
		super(aid, message);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param aid
	 * @param message
	 */
	public ConcurrencyException(String aid, String message)
	{
		super(aid, message);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}

	/**
	 * @param aid
	 * @param bindValues
	 */
	public ConcurrencyException(String aid, Map<Object,Object> bindValues)
	{
		super(aid, bindValues);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}

}
