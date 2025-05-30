package nyla.solutions.core.exception;

/**
 *  Business exception when no data is found from a SQL query or external user management system
 *  @author Gregory Green
 *  @version 2.0
 */
public class NoDataFoundException extends DataException 
{
   public static final String DEFAULT_NO_DATA_ERROR_MSG = "No Data Found";

   /**
    * Sets the exception message to  "No Data Found" 
    */
   public NoDataFoundException()
   {
      super(DEFAULT_NO_DATA_ERROR_MSG);
   }

   /**
    * @param message the exception message
    */
   public NoDataFoundException(String message)
   {
      super(message);
   }

   static final long serialVersionUID = NoDataFoundException.class.getName()
   .hashCode();
}
