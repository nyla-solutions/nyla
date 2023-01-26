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
		this.setCode(DEFAULT_ERROR_CODE);
		
		Debugger.printFatal(this);
	}

	/**
	 * @param message message
	 */
	public FatalException(String message)
	{
		super(message);
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);

		Debugger.printFatal(this);
	}

	/**
	 * @param exception
	 */
	public FatalException(Throwable exception)
	{
		super(exception);
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);
		Debugger.printFatal(this);
	}

	/**
	 * @param message the message
	 * @param exception the exception
	 */
	public FatalException(String message, Throwable exception)
	{
		super(message, exception);
		this.setCategory(DEFAULT_ERROR_CATEGORY);
		this.setCode(DEFAULT_ERROR_CODE);

		Debugger.printFatal(this);
	}

	public FatalException(String message, String notes, String programName,
			String functionName, String errorCategory, String errorCode)
	{
		super(message, notes, programName, functionName, errorCategory,
				errorCode);
	}

	static final long serialVersionUID = FatalException.class.getName()
			.hashCode();
}
