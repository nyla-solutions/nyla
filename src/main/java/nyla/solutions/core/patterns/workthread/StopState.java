package nyla.solutions.core.patterns.workthread;

/**
 * <pre>
 * StopState provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class StopState implements WorkState
{

   /**
    * 
    * @see nyla.solutions.core.patterns.workthread.WorkState#advise(nyla.solutions.core.patterns.workthread.SupervisedWorker)
    */
   public void advise(SupervisedWorker aWorker)
   {
      aWorker.setWorkState(this);
   }// --------------------------------------------


   /**
    * 
    * @see nyla.solutions.core.patterns.workthread.WorkState#getName()
    */
   public String getName()
   {
      return name;
   }// --------------------------------------------

   /**
    * 
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      return name;
   }
   private String name = "STOP";
}
