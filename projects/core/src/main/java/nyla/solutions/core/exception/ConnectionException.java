
package nyla.solutions.core.exception;

import java.io.Serial;

/**
 * @author Gregory Green
 * @version 1.0
 *
 * <b>ConnectionException</b>  
 * 
 */
public class ConnectionException extends FatalException
{
	public static final String DEFAULT_ERROR_CODE = "COM000";
	
   /**
    * 
    */
   public ConnectionException()
   {
      super();
      this.setCode(DEFAULT_ERROR_CODE);
   }
   /**
    * @param message the exception message
    */
   public ConnectionException(String message)
   {
      super(message);
      this.setCode(DEFAULT_ERROR_CODE);
   }
   /**
    * @param message the error message
    * @param exception the root cause exception
    */
   public ConnectionException(String message, Throwable exception)
   {
      super(message, exception);
      this.setCode(DEFAULT_ERROR_CODE);
   }
   /**
    * @param arg0
    */
   public ConnectionException(Throwable arg0)
   {
      super(arg0);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   @Serial
   private static final long serialVersionUID = 1;
}
