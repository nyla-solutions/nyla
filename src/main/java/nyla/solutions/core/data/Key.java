package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * Any object that has a key.
 * @param <T> the key type
 * 
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface Key<T> extends Serializable
{

   /**
    * 
    * @return the object's key
    */
   public T getKey();

   
}
