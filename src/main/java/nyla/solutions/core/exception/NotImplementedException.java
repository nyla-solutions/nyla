package nyla.solutions.core.exception;

import java.util.Map;

/**
 * 
 * @author Gregory Green
 *
 */
public class NotImplementedException extends SystemException
{

   public NotImplementedException()
   {
      super("Not Implemented");
   }

   public NotImplementedException(int aid, String message)
   {
      super(aid, message);
   }

   public NotImplementedException(String aid, Map<Object,Object> bindValues)
   {
      super(aid, bindValues);
   }

   public NotImplementedException(String aid, String message)
   {
      super(aid, message);
   }

   public NotImplementedException(String arg0, Throwable throwable)
   {
      super(arg0, throwable);
   }

   public NotImplementedException(String messsage)
   {
      super(messsage);
   }

   public NotImplementedException(Throwable throwable)
   {
      super(throwable);
   }

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

}
