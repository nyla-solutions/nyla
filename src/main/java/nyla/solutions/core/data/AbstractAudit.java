package nyla.solutions.core.data;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * <pre>
 * 
 * 
 *  
 * 
 *  AbstractAudit is a value object representation of the  
 *  AbstractAudit table and associated entities.
 
 * </pre>
 * 
 * 
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 *  
 */

public abstract class  AbstractAudit extends Criteria
implements Auditable, Copier, Serializable
{

   /**
	 * 
	 */
	private static final long serialVersionUID = -7209646661008959133L;

/**
    * 
    * 
    *  
    */

   public AbstractAudit()

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

   public AbstractAudit(int aPK) throws IllegalArgumentException

   {

      super(aPK);

   }//--------------------------------------------

   /**
    * 
    * @param primaryKey the primary key
     * @throws IllegalArgumentException
    *  
    */

   public AbstractAudit(Criteria primaryKey) throws IllegalArgumentException

   {

      super(primaryKey);

   }//--------------------------------------------

   /**
    * 
    * @param aPK
    * 
    * @throws IllegalArgumentException
    *  
    */

   public AbstractAudit(String aPK) throws IllegalArgumentException

   {

      super(aPK);

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @see nyla.solutions.core.data.Criteria#setPrimaryKey(int)
    *  
    */

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

	   if(createDate == null)
		   return null;
	   
      return (Date)createDate.clone();

   }//--------------------------------------------

   /**
    * 
    * @param createDate   The createDate to set.
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
	   if(updateDate  == null)
		   return null;

      return (Date)updateDate.clone();

   }//--------------------------------------------

   /**
    * 
    * @param updateDate
    *           The updateDate to set.
    *  
    */

   public void setUpdateDate(Date updateDate)
   {

      this.updateDate = new Date(updateDate.getTime());

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
    * @param primaryKey the primary key
    * @throws IllegalArgumentException when primary if not provided or invalid
    *  
    */
   public void setPrimaryKeyString(String primaryKey) throws IllegalArgumentException
   {

      super.setPrimaryKeyString(primaryKey);

   }//----------------------------------------
   /**
    * 
    * @param primaryKey the primary key
    * @throws IllegalArgumentException
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
      if (deletedCode == null)
      {
         throw new IllegalArgumentException("deletedCode required");
      }

      if (!Data.NO.equals(deletedCode) && !Data.YES.equals(deletedCode))
      {
         throw new IllegalArgumentException(deletedCode
         + "is not valid. Expected valid is " + Data.NO + " or " + Data.YES);
      }

      this.deletedCode = deletedCode;

   }//--------------------------------------------

   /**
    * 
    * Copy attributes from a given object
    * @param aCopier the record to copy
    */
   public void copy(Copier aCopier)
   {
      if(!(aCopier instanceof AbstractAudit))
         return;
      
      AbstractAudit abstractAudit = (AbstractAudit)aCopier;
      
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
    * @return this.getPrimaryKey()  &lt; 1
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

   private Object createUserID = null;

   private Date createDate = null;

   private Object updateUserID = null;

   private Date updateDate = null;

   private String deletedCode = Data.NO;

}