package nyla.solutions.commas;

public interface CloneableExecutable extends Cloneable, Executable
{
   /**
    * 
    * @return the clone
    */
   public Object clone()
   throws CloneNotSupportedException;
}
