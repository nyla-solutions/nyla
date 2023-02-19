package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * Attribute provides a set of functions to
 * </pre> 
 * 
 * @author Gregory Green
 * @version 1.0
 * @param <K> the key class
 * @param <V> the value class
 */
public interface Attribute<K,V> extends Serializable, Mappable<K,V>, Nameable
{
   /**
    * 
    * @return attribute name
    */
   public String getName();
   
//   /**
//    *
//    * @param aName the name
//    */
//   public void setName(String aName);
   
//   /**
//    *
//    * @param aValue the value
//    */
//   public void setValue(Serializable aValue);
   
//   /**
//    *
//    * @param aValue the property value
//    * @return true if string version of the property value
//    * equals (ignore case) aValue
//    */
//   public boolean equalsValueIgnoreCase(Object aValue);
   
}
