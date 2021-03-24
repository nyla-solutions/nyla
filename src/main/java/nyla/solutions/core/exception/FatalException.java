package nyla.solutions.core.exception;

import nyla.solutions.core.exception.fault.FaultException;
import nyla.solutions.core.util.Debugger;


/**
 * <pre>
 * FatalException a fatal system exception
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class FatalException extends FaultException
{
	public static final String DEFAULT_ERROR_CODE = "F0000";
	public static final String DEFAULT_ERROR_CATEGORY = "FATAL";
	
	/**
    * 
    */
	public FatalException()
	{
		super();
		
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CATEGORY);
		
		Debugger.printFatal(this);
	}// -----------------------------

	/**
	 * @param aMessage message
	 */
	public FatalException(String aMessage)
	{
		super(aMessage);

		Debugger.printFatal(this);
	}// --------------------------------
	

	/**
	 * @param aThrowable
	 */
	public FatalException(Throwable aThrowable)
	{
		super(aThrowable);
		Debugger.printFatal(this);
	}// --------------------------------

	/**
	 * @param aMessage
	 * @param aThrowable
	 */
	public FatalException(String aMessage, Throwable aThrowable)
	{
		super(aMessage, aThrowable);

		Debugger.printFatal(this);
	}// --------------------------------

	public FatalException(String message, String notes, String programName,
			String functionName, String errorCategory, String errorCode)
	{
		super(message, notes, programName, functionName, errorCategory,
				errorCode);
		// TODO Auto-generated constructor stub
	}

	public FatalException(String message, String functionName,
			String errorCategory, String errorCode, String programName)
	{
		super(message, functionName, errorCategory, errorCode, programName);
		// TODO Auto-generated constructor stub
	}

	public FatalException(String aID, String aMessage)
	{
		super(aID, aMessage);
		// TODO Auto-generated constructor stub
	}

	public FatalException(String message, Throwable cause, String functionName,
			String errorCategory, String errorCode, String programName)
	{
		super(message, cause, functionName, errorCategory, errorCode,
				programName);
	}// --------------------------------------------------------
	public FatalException(Throwable cause, String functionName,
			String errorCategory, String errorCode, String programName)
	{
		super(cause, functionName, errorCategory, errorCode, programName);
		// TODO Auto-generated constructor stub
	}// --------------------------------------------------------

	static final long serialVersionUID = FatalException.class.getName()
			.hashCode();
}
