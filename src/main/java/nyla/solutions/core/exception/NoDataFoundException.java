package nyla.solutions.core.exception;

/**
 *  Business exception when no data is found from a SQL query or external user management system
 *  @author Gregory Green
 *  @version 2.0
 */
public class NoDataFoundException extends DataException 
{
   /**
    * Sets the exception message to  "No Data Found" 
    */
   public NoDataFoundException()
   {
      super("No Data Found");

   }//------------------------------------------------
   /**
    * @param aMessage the exception message
    */
   public NoDataFoundException(String aMessage)
   {
      super(aMessage);
   }//------------------------------------------------------------
   static final long serialVersionUID = NoDataFoundException.class.getName()
   .hashCode();
}
