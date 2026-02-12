package nyla.solutions.core.exception;

/**
 * @author Gregory Green
 * @version 1.0
 *
 * <b>SystemException</b> default system exception runtime exception
 * 
 */

import nyla.solutions.core.exception.fault.FaultException;

import java.io.Serial;

public class DataException extends FaultException
{
	public static final String DEFAULT_MESSAGE = "Internal system error.";
	public static final String DEFAULT_ERROR_CODE = "D0000";
	public static final String DEFAULT_ERROR_CATEGORY = "DATA";

	/**
    * 
    */
	public DataException()
	{
		super(DEFAULT_MESSAGE);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}// ---------------------------------------------

	/**
	 * @param message the exception message
	 */
	public DataException(String message)
	{
		super(message);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}// --------------------------------------------

	/**
	 * @param message the data message error
	 * @param aThrowable
	 */
	public DataException(String message, Throwable aThrowable)
	{
		super(message, aThrowable);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);

	}// ---------------------------------------------

	/**
	 * @param aThrowable
	 */
	public DataException(Throwable aThrowable)
	{
		super(aThrowable);

		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}// ---------------------------------------------

	public DataException(String message, String notes, String programName,
			String functionName, String errorCategory, String errorCode)
	{
		super(message, notes, programName, functionName, errorCategory,
				errorCode);
	}


	@Serial
    private static final long serialVersionUID = 1;

}
