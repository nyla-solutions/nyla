package nyla.solutions.core.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * 
 * <pre>
 * 
 * 
 *  Updateable interface for objects that can be updated in the database
 * 
 *  
 * </pre>
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 *  
 */

public interface Updateable extends Serializable

{

   /**
    * 
    * 
    * 
    * @return the date the object was updated
    *  
    */

   public Date getUpdateDate();

   /**
    * 
    * 
    * 
    * @param date
    *           the date the object was updated
    *  
    */

   public void setUpdateDate(Date date);

   /**
    * Set the update user ID
    * 
    * @param aUpdateUserID
    *           the update user ID
    */
   public void setUpdateUserID(Object aUpdateUserID);

   /**
    * 
    * @return the user update ID
    */
   public Object getUpdateUserID();
}

