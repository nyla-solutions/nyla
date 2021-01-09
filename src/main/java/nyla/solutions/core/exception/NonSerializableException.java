package nyla.solutions.core.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * NonSerializableException used to indicate that a given field is required.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class NonSerializableException extends DataException
{
   public NonSerializableException()
   {
   }

   public NonSerializableException(String message)
   {
      super(message);
   }

   public NonSerializableException(String message, Throwable aThrowable)
   {
      super(message, aThrowable);
   }
}
