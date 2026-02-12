package nyla.solutions.core.exception;

import java.io.Serial;

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
   public static final String DEFAULT_INTEGRITY_CONSTRAINT_ERROR_MSG = "Integrity Constraint";

   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * 
    */
   public IntegrityConstraintException()
   {
      super(DEFAULT_INTEGRITY_CONSTRAINT_ERROR_MSG);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * @param message the error message
    */
   public IntegrityConstraintException(String message)
   {
      super(message);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * @param message the error message
    * @param throwableException the root cause exception
    */
   public IntegrityConstraintException(String message, Throwable throwableException)
   {
      super(message, throwableException);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for IntegrityConstraintException initializes internal 
    * data settings.
    * @param throwableException
    */
   public IntegrityConstraintException(Throwable throwableException)
   {
      super(throwableException);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   @Serial
   private static final long serialVersionUID = IntegrityConstraintException.class.getName()
   .hashCode();
}
