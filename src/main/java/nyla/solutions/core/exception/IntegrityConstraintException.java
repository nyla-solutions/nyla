package nyla.solutions.core.exception;

/**
 * <pre>
 * IntegrityConstraintException represents a data exception
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class IntegrityConstraintException extends DataException
{
	public static final String DEFAULT_ERROR_CODE = "IC001";
   

   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * 
    */
   public IntegrityConstraintException()
   {
      super(" Integrity Constraint");
      this.setCode(DEFAULT_ERROR_CODE);
   }//--------------------------------------------
   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * @param aMessage
    */
   public IntegrityConstraintException(String aMessage)
   {
      super(aMessage);
      this.setCode(DEFAULT_ERROR_CODE);
   }//--------------------------------------------
   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * @param aMessage
    * @param aThrowable
    */
   public IntegrityConstraintException(String aMessage, Throwable aThrowable)
   {
      super(aMessage, aThrowable);
      this.setCode(DEFAULT_ERROR_CODE);
   }//--------------------------------------------
   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * @param aThrowable
    */
   public IntegrityConstraintException(Throwable aThrowable)
   {
      super(aThrowable);
      this.setCode(DEFAULT_ERROR_CODE);
   }//--------------------------------------------
   static final long serialVersionUID = IntegrityConstraintException.class.getName()
   .hashCode();
}
