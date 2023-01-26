package nyla.solutions.core.exception;

/**
 *  System exception when too many rows where updated in the database.
 *  @author Gregory Green
 *  @version 1.0
 */
public class TooManyRowsException extends DataException
{
   public static final String TOO_MANY_ROWS_ERROR_MSG = "Too Many Rows";

   /**
    *     Sets the exception message to  "Duplicate Row Exception" 
    */
   public TooManyRowsException()
   {
      super(TOO_MANY_ROWS_ERROR_MSG);

   }

   public TooManyRowsException(String aMessage)
   {
      super(aMessage);
   }

   static final long serialVersionUID = TooManyRowsException.class.getName()
   .hashCode();
}
