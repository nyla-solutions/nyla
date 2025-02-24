package nyla.solutions.dao.exception;

import nyla.solutions.core.exception.SystemException;

/**
 * <pre>
 * JDOException represents data access exceptions
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class JDOException extends SystemException
{

   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = 1L;


   public JDOException()
   {
      super();
   }// --------------------------------------------


   public JDOException(String arg0, Throwable arg1)
   {
      super(arg0, arg1);
   }// --------------------------------------------


   public JDOException(String arg0)
   {
      super(arg0);
   }// --------------------------------------------


   public JDOException(Throwable arg0)
   {
      super(arg0);
   }

}
