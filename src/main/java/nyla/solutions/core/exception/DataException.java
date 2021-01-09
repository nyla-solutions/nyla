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

public class DataException extends FaultException
{	
	public static final String DEFAULT_ERROR_CODE = "D0000";
	public static final String DEFAULT_ERROR_CATEGORY = "DATA";

	/**
    * 
    */
	public DataException()
	{
		super("Internal system error.");
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}// ---------------------------------------------

	/**
	 * @param message the exception message
	 */
	public DataException(String message)
	{
		super(message);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}// --------------------------------------------

	/**
	 * @param arg0
	 * @param aThrowable
	 */
	public DataException(String arg0, Throwable aThrowable)
	{
		super(arg0, aThrowable);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);

	}// ---------------------------------------------

	/**
	 * @param aThrowable
	 */
	public DataException(Throwable aThrowable)
	{
		super(aThrowable);

		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}// ---------------------------------------------

	/**
	 * @param aID integer value in the SystemException.properties file
	 * @param aMessage the message
	 */
	public DataException(int aID, String aMessage)
	{
		this(String.valueOf(aID), aMessage);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}// ---------------------------------------------

	public DataException(String message, String notes, String programName,
			String functionName, String errorCategory, String errorCode)
	{
		super(message, notes, programName, functionName, errorCategory,
				errorCode);
	}

	public DataException(String message, String functionName,
			String errorCategory, String errorCode, String programName)
	{
		super(message, functionName, errorCategory, errorCode, programName);
	}

	public DataException(String aID, String aMessage)
	{
		super(aID, aMessage);
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}

	public DataException(String message, Throwable cause,
			String functionName, String errorCategory, String errorCode,
			String programName)
	{
		super(message, cause, functionName, errorCategory, errorCode,
				programName);
	}

	public DataException(Throwable cause, String functionName,
			String errorCategory, String errorCode, String programName)
	{
		super(cause, functionName, errorCategory, errorCode, programName);
	}

	/**
	 * 
	 * Constructor for SystemException initializes internal data settings.
	 * 
	 * @param aID the key in the exception property file
	 * @param aBindValues the bind exception
	 */
	public DataException(String aID, Map<Object, Object> aBindValues)
	{
		formatMessage(aID, aBindValues);
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
	}// --------------------------------------------

	static final long serialVersionUID = 1;

}
