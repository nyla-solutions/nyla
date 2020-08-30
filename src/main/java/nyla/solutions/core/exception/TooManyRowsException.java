package nyla.solutions.core.exception;

/**
 *  System exception when too many rows where updated in the database.
 *  @author Gregory Green
 *  @version 1.0
 */
public class TooManyRowsException extends DataException
{
   /**
    *     Sets the exception message to  "Duplicate Row Exception" 
    */
   public TooManyRowsException()
   {
      super("Too Many Rows");

   }//----------------------------------------------
   public TooManyRowsException(String aMessage)
   {
      super(aMessage);
   }//----------------------------------------------
   static final long serialVersionUID = TooManyRowsException.class.getName()
   .hashCode();
}
