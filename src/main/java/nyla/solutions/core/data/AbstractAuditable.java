package nyla.solutions.core.data;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * <pre>
 * 
 * 
 *  AbstractAudit is a value object representation of the 
 *  AbstractAudit table and associated entities.
 *  
 * </pre>
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 *  
 */

public abstract class  AbstractAuditable extends Criteria
implements Auditable, Copier, Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = 4877170428593347353L;

   /**
    * aAuditable.setDeletedCode(Data.NO)
    * @param aAuditable object to update
    */
   public static void unDelete(Auditable aAuditable)
   {
         aAuditable.setDeletedCode(Data.NO);
   }//--------------------------------------------
   /**
    * 
    * Values Y= Yes N=No
    * 
    * @param auditable the auditable object
    * @param deletedCode
    *           The deletedCode to set.
    *  
    */

   public static void setDeletedCode(Auditable auditable, String deletedCode)
   {
      if(auditable == null)
         return;

         checkDeletedCode(deletedCode);

         auditable.setDeletedCode(deletedCode);
   }//--------------------------------------------
   /**
    * 
    * @param aAuditable the auditable object
    * @return Data.YES.equals(aAuditable.getDeletedCode())
    */
   public static boolean isDelete(Auditable aAuditable)
   {
      return Data.YES.equals(aAuditable.getDeletedCode());
   }//--------------------------------------------
   /**
    * aAuditable.setDeletedCode(Data.YES)
    * @param aAuditable object to update
    */
   public static void delete(Auditable aAuditable)
   {
         aAuditable.setDeletedCode(Data.YES); 
   }//--------------------------------------------
   /**
    * @param aDeletedCode
    * @throws IllegalArgumentException if deleted code is invalid
    */
   public static void checkDeletedCode(String aDeletedCode)
   throws IllegalArgumentException
   {
      if (aDeletedCode == null)
      {
         throw new IllegalArgumentException("deletedCode required");
      }

      if (!Data.NO.equals(aDeletedCode) && !Data.YES.equals(aDeletedCode))
      {
         throw new IllegalArgumentException(aDeletedCode
         + "is not valid. Expected valid is " + Data.NO + " or " + Data.YES);
      }
   }//--------------------------------------------
   /**
    * 
    * @param aFrom auditing data to copy form
    * @param aTo auditing date to copy
    */
   public static void copy(Auditable aFrom, Auditable aTo)
   {
      if (aFrom == null || aTo == null)
         return;     

       if(aTo.getCreateDate() == null &&
          aFrom.getCreateDate() != null)
       {
          aTo.setCreateDate(aFrom.getCreateDate());
       }
          
      
       if(aTo.getUpdateDate() == null &&
       aFrom.getUpdateDate() != null)
       {
          aTo.setUpdateDate(aFrom.getUpdateDate());
       }
      

       if(aTo.getCreateUserID() == null &&
          aFrom.getCreateUserID() != null)
       {
          aTo.setCreateUserID(aFrom.getCreateUserID());
       }
       
       if(aTo.getUpdateUserID() == null &&
       aFrom.getUpdateUserID() != null)
       {
          aTo.setUpdateUserID(aFrom.getUpdateUserID());
       }

   }//--------------------------------------------


   /**
    * 
    * 
    *  
    */

   public AbstractAuditable()
   {
      super();

   }//--------------------------------------------

   /**
    * 
    * @param aPK
    * 
    * @throws IllegalArgumentException
    *  
    */
   public AbstractAuditable(int aPK) throws IllegalArgumentException
   {

      super(aPK);

   }//--------------------------------------------

   /**
    * 
    * @param aPK
    * 
    * @throws IllegalArgumentException
    *  
    */

   public AbstractAuditable(Criteria aPK) throws IllegalArgumentException

   {

      super(aPK);

   }//--------------------------------------------

   /**
    * 
    * @param aPK
    * 
    * @throws IllegalArgumentException
    *  
    */

   public AbstractAuditable(String aPK) throws IllegalArgumentException
   {
      super(aPK);

   }//--------------------------------------------
   public void setPrimaryKey(int primaryKey) throws IllegalArgumentException

   {

      super.setPrimaryKey(primaryKey);

   }//----------------------------------------

   /**
    * 
    * @return Returns the createDate.
    *  
    */

   public Date getCreateDate()

   {

	   if(this.createDate == null)
		   return null;
	   
      return (Date)this.createDate.clone();

   }//--------------------------------------------

   /**
    * 
    * @param createDate
    *           The createDate to set.
    *  
    */

   public void setCreateDate(Date createDate)
   {
	   if(createDate == null)
		   this.createDate = null;
	   else
		   this.createDate = new Date(createDate.getTime());

   }//--------------------------------------------------

   /**
    * 
    * @return Returns the createUserID.
    *  
    */

   public Object getCreateUserID()
   {

      return createUserID;

   }//--------------------------------------------

   /**
    * 
    * @param createUserID
    *           The createUserID to set.
    *  
    */

   public void setCreateUserID(Object createUserID)
   {

      this.createUserID = createUserID;

   }//--------------------------------------------

   /**
    * 
    * @return Returns the updateDate.
    *  
    */

   public Date getUpdateDate()
   {
      if (updateDate == null) return null;
      return new java.util.Date(updateDate.getTime());

   }//--------------------------------------------

   /**
    * 
    * @param updateDate
    *           The updateDate to set.
    *  
    */
   public void setUpdateDate(Timestamp updateDate)
   {
	   if(updateDate == null)
		   this.updateDate = null;
	   else
		   this.updateDate = (Timestamp)updateDate.clone();

   }//--------------------------------------------
   public void setUpdateDate(Date updateDate)
     {

        this.updateDate = new java.sql.Timestamp(updateDate.getTime());

     }//--------------------------------------------

   /**
    * 
    * @return Returns the updateUserID.
    *  
    */

   public Object getUpdateUserID()

   {

      return updateUserID;

   }//--------------------------------------------

   /**
    * 
    * @param updateUserID
    *           The updateUserID to set.
    *  
    */

   public void setUpdateUserID(Object updateUserID)
   {

      this.updateUserID = updateUserID;

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @param primaryKey the primary key to set
    * @throws IllegalArgumentException PrimaryKey is invalid
    *  
    */

   public void setPrimaryKeyString(String primaryKey) throws IllegalArgumentException

   {

      super.setPrimaryKeyString(primaryKey);

   }//----------------------------------------

   /**
    * 
    * 
    * 
   * @param primaryKey the primary key to set
    * @throws IllegalArgumentException PrimaryKey is invalid
    *  
    */

   public void setPrimaryKey(Criteria primaryKey)
   throws IllegalArgumentException

   {

      super.setPrimaryKeyObject(primaryKey);

   }//--------------------------------------------

   /**
    * 
    * Values Y= Yes N=No
    * 
    * @return Returns the deletedCode.
    *  
    */

   public String getDeletedCode()

   {

      return deletedCode;

   }//--------------------------------------------

   /**
    * 
    * Values Y= Yes N=No
    * 
    * @param deletedCode
    *           The deletedCode to set.
    *  
    */

   public void setDeletedCode(String deletedCode)
   {
      checkDeletedCode(deletedCode);

      this.deletedCode = deletedCode;

   }//--------------------------------------------

   /**
    *
    * @param aCopier
    */
   public void copy(Copier aCopier)
   {
      if(!(aCopier instanceof AbstractAuditable))
         return;
      
      AbstractAuditable abstractAudit = (AbstractAuditable)aCopier;
      
      if (this == abstractAudit)
         return;

      super.copy(abstractAudit);

      //may need to clone object

      this.createUserID = abstractAudit.createUserID;

      this.createDate = abstractAudit.createDate;

      this.updateUserID = abstractAudit.updateUserID;

      this.updateDate = abstractAudit.updateDate;

      this.deletedCode = abstractAudit.deletedCode;

   }//--------------------------------------------
   /**
    * 
    * @return this.getPrimaryKey()  &gt; 1
    */
   public boolean isNew()
   {
      return this.getPrimaryKey()  < 1;
   }//--------------------------------------------
   /**
    * 
    * @return Data.YES.equals(this.deletedCode)
    */
   public boolean isDeleted()
   {
      return Data.YES.equals(this.deletedCode);
   }//--------------------------------------------
   /**
    * this.setDeletedCode(Data.YES);
    *
    */
   public void delete()
   {
      this.setDeletedCode(Data.YES);
   }//-------------------------------------------- 
   /**
    * this.setDeletedCode(Data.NO);
    *
    */
   public void unDelete()
   {
      this.setDeletedCode(Data.NO);
   }//--------------------------------------------

   /**
    * @return the createUserLogin
    */
   public final Object getCreateUserLogin()
   {
      return createUserLogin;
   }
   /**
    * @param createUserLogin the createUserLogin to set
    */
   public final void setCreateUserLogin(String createUserLogin)
   {
      if (createUserLogin == null)
         createUserLogin = "";
   
      this.createUserLogin = createUserLogin;
   }

   private Object createUserLogin = null;
   
   private Object createUserID = null;

   private Date createDate = null;

   private Object updateUserID = null;

   private Timestamp updateDate = null;

   private String deletedCode = Data.NO;
}