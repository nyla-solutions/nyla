package nyla.solutions.core.exception;
/**
 *  System exception when a unique constraint error occurs in the database.
 *  @author Gregory Green
 *  @version 2.0
 */
public class DuplicateRowException extends DataException
{
	
	public static final String DEFAULT_ERROR_CODE = "DUP000";
	
   /**
    *     Sets the exception message to  "Duplicate Row Exception" 
    */
   public DuplicateRowException()
   {
      super("Duplicate Entry Found");
      this.setCode(DEFAULT_ERROR_CODE);

   }// --------------------------------------------------------
   /**
    * 
    * @param aMessage the duplicate message
    */
   public DuplicateRowException(String aMessage)
   {
      super(aMessage);
      this.setCode(DEFAULT_ERROR_CODE);
   }// --------------------------------------------
   /**
    * 
    * @param message the duplicate message
    * @param cause the exception/cause
    */
   public DuplicateRowException(String message, Throwable cause)
	{
		super(message, cause);
		this.setCode(DEFAULT_ERROR_CODE);
	}// --------------------------------------------------------
	/**
	 * 
	 * @param cause the root cause
	 */
	public DuplicateRowException(Throwable cause)
	{
		super(cause);
		this.setCode(DEFAULT_ERROR_CODE);
	}
	
	static final long serialVersionUID = 1;
	}
