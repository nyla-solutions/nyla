package nyla.solutions.core.exception;

/**
 * @author gregory green
 * @version 1.0
 */
public class TimeOutException extends CommunicationException
{
   public static final String TIMEOUT_MSG ="Time Out";

   public TimeOutException()
   {
      super(TIMEOUT_MSG);
   }

   public TimeOutException(String msg)
   {
      super(msg);
   }
   
   static final long serialVersionUID = TimeOutException.class.getName()
   .hashCode();
}
