package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * CodeKey  represents and object with a code
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public interface Codeable extends Serializable
{
   /**
    * 
    * @return the code
    */
   public String getCode();
   
   /**
    * 
    * @param aCode the code to set
    */
   public void setCode(String aCode);
}
