package nyla.solutions.core.data;


import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * Createable interface for items that can be created.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */

public interface Createable extends Serializable

{

   /**
    * 
    * @return date when item was created
    */

   public Date getCreateDate();

   /**
    * Set create date
    * @param aCreateDate the create date
    */
   public void setCreateDate(Date aCreateDate);

   /**
    * @return the create user's primary key
    */
   public Object getCreateUserID();
   
   /**
    * set create user ID
    * @param aCreateUserID create user ID
    */
   public void setCreateUserID(Object aCreateUserID);

}

