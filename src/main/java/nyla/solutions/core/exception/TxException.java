package nyla.solutions.core.exception;

import nyla.solutions.core.exception.fault.FaultException;




public class TxException extends FaultException 
{

	/**
	 * ERROR_CODE = "GT000"
	 */
	public static final String ERROR_CODE = "GT000";
	
	/**
	 * ERROR_CATEGORY  = GRID_TX_ERROR_CATEGORY
	 */
	public static final String ERROR_CATEGORY  = "TX";
	
	private static final long serialVersionUID = 1L;

	public TxException() {
		super("GridQueryException");
		
		
	}

	public TxException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public TxException(String message) {
		super(message);
	}

	public TxException(Throwable cause) {
		super("GridQueryException",cause);
	}
	
	/**
	 * @return fixed ERROR_CODE
	 */
	@Override
	public String getCode()
	{
		
		return ERROR_CODE;
	}
	
	/**
	 * @return fixed ERROR_CATEGORY
	 */
	@Override
	public String getCategory()
	{
		return ERROR_CATEGORY;
	}

}
