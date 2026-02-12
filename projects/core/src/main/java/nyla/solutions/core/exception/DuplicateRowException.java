package nyla.solutions.core.exception;

import java.io.Serial;

/**
 *  System exception when a unique constraint error occurs in the database.
 *  @author Gregory Green
 *  @version 2.0
 */
public class DuplicateRowException extends DataException
{
	
	public static final String DEFAULT_ERROR_CODE = "DUP000";
	public static final String DEFAULT_DUPLICATE_ROW_MESSAGE = "Duplicate Entry Found";

	/**
    *     Sets the exception message to  "Duplicate Row Exception" 
    */
   public DuplicateRowException()
   {
      super(DEFAULT_DUPLICATE_ROW_MESSAGE);
      this.setCode(DEFAULT_ERROR_CODE);

   }
   /**
    * 
    * @param message the duplicate message
    */
   public DuplicateRowException(String message)
   {
      super(message);
      this.setCode(DEFAULT_ERROR_CODE);
   }
   /**
    * 
    * @param message the duplicate message
    * @param cause the exception/cause
    */
   public DuplicateRowException(String message, Throwable cause)
	{
		super(message, cause);
		this.setCode(DEFAULT_ERROR_CODE);
	}
	/**
	 * 
	 * @param cause the root cause
	 */
	public DuplicateRowException(Throwable cause)
	{
		super(cause);
		this.setCode(DEFAULT_ERROR_CODE);
	}
	
	@Serial
    private static final long serialVersionUID = 1;
	}
