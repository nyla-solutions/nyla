package nyla.solutions.core.exception;

/**
 * @author Gregory Green
 * @version 1.0
 *
 * <b>SystemException</b> default system exception runtime exception
 * 
 */

import nyla.solutions.core.exception.fault.FaultException;

import java.util.Map;

public class SystemException extends FaultException
{	
	public static final String DEFAULT_ERROR_CODE = "S0000";
	public static final String DEFAULT_ERROR_CATEGORY = "SYSTEM";
	public static final String INTERNAL_ERROR_MSG = "Internal system error.";

	/**
	 * Default constructor
	 */
	public SystemException()
	{
		super(INTERNAL_ERROR_MSG);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param message the exception message
	 */
	public SystemException(String message)
	{
		super(message);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param message the error message
	 * @param exception the cause exception
	 */
	public SystemException(String message, Throwable exception)
	{
		super(message, exception);

		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);

	}

	/**
	 * @param exception the cause exception
	 */
	public SystemException(Throwable exception)
	{
		super(exception);

		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
	}


	public SystemException(String message, String notes, String programName,
			String functionName, String errorCategory, String errorCode)
	{
		super(message, notes, programName, functionName, errorCategory,
				errorCode);
	}

	static final long serialVersionUID = 1;

}
