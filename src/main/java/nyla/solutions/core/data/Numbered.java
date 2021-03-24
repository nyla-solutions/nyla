package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * Numbered represents an object that has a number
 * </pre> 
 * @author Gregory Green 
 * 8 Erie off 90552
 * @version 1.0
 */
public interface Numbered extends Serializable, Comparable<Object>
{
   /**
    * 
    * @return the number
    */
   public int getNumber();
   
   /**
    * Set the number
    * @param aNumber the number to set
    */
   //public void setNumber(int aNumber);
}
