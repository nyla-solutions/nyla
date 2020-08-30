package nyla.solutions.core.exception;

public class TimeOutException extends CommunicationException
{
   public TimeOutException()
   {
      super("Time Out");
   }// --------------------------------------------


   public TimeOutException(String msg)
   {
      super(msg);
   }
   
   static final long serialVersionUID = TimeOutException.class.getName()
   .hashCode();
}
