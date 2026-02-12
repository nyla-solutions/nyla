package nyla.solutions.core.exception;

import java.io.Serial;

/**
 * <pre>
 * FormatException exception related for parsing or formatting data.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class FormatException extends SystemException
{
	public static final String DEFAULT_ERROR_CODE = "FORMAT01";
	
   /**
    * Comment for <code>serialVersionUID</code>
    */
   @Serial
   private static final long serialVersionUID = -4907281152649411791L;

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    */
   public FormatException()
   {
      super();
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    * @param message
    */
   public FormatException(String message)
   {
      super(message);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    * @param message
    * @param exception
    */
   public FormatException(String message, Throwable exception)
   {
      super(message, exception);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    * @param aThrowable
    */
   public FormatException(Throwable aThrowable)
   {
      super(aThrowable);
      this.setCode(DEFAULT_ERROR_CODE);
   }


}
