package nyla.solutions.core.data;


/**
 * <pre>
 * Mappable object that has a name and a value.
 *  * 
 * </pre>
 * @param <K> the key
 * @param <V> the value 
 * @author Gregory Green
 * @version 1.0
 */

public interface Mappable<K,V> extends Key<K>
{
  
   /**
    * 
    * @return the value
    */
   public V getValue();   

}

