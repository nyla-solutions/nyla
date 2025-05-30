package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * Versioned represents an object with a version number
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface Versioned extends Serializable
{
   /**
    * 
    * @return version number
    */
   public Integer getVersion();
   
   
   /**
    * 
    * @param aVersion the version to set
    */
   public void setVersion(Integer aVersion); 
   
}
