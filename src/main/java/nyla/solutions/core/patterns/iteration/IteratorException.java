package nyla.solutions.core.patterns.iteration;

public class IteratorException extends Exception
{
   public IteratorException()
   {
   }//-----------------------------------
   public IteratorException(String msg)
   {
      super(msg);
   }//----------------------------------
   static final long serialVersionUID = IteratorException.class.getName()
   .hashCode();
}
