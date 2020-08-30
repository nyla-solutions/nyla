
package nyla.solutions.core.exception;

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
    * @param arg0
    */
   public ConnectionException(String arg0)
   {
      super(arg0);
      this.setCode(DEFAULT_ERROR_CODE);
   }
   /**
    * @param arg0
    * @param arg1
    */
   public ConnectionException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
      this.setCode(DEFAULT_ERROR_CODE);
   }
   /**
    * @param arg0
    */
   public ConnectionException(Throwable arg0)
   {
      super(arg0);
      this.setCode(DEFAULT_ERROR_CODE);
   }// --------------------------------------------

   static final long serialVersionUID = 1;
}
