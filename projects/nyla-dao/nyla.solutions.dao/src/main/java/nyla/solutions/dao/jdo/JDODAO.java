package nyla.solutions.dao.jdo;

import nyla.solutions.dao.SQL;
import nyla.solutions.dao.exception.JDOException;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.DuplicateRowException;
import nyla.solutions.core.exception.IntegrityConstraintException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SystemException;



import nyla.solutions.core.data.Attribute;
import nyla.solutions.core.data.Auditable;
import nyla.solutions.core.data.Copier;
import nyla.solutions.core.data.Data;
import nyla.solutions.core.data.Key;
import nyla.solutions.core.data.Nameable;
import nyla.solutions.core.data.NumberedProperty;
import nyla.solutions.core.data.PrimaryKey;
import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.core.security.data.*;
import nyla.solutions.core.util.Debugger;

import java.sql.SQLException;
import java.util.*;

import javax.jdo.PersistenceManager;


/**
 * 
 * 
 * 
 * <pre>
 * 
 * 
 *  JDODAO is a data access object for JDO object management
 * 
 *  
 * </pre>
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 *  
 */

public class JDODAO implements Disposable

{
   /**
    * 
    * Constructor for JDODAO initializes internal 
    * data settings.
    * 
    * Data Source is Supported
    *
    */
   protected JDODAO()
   {
      this(true);
   }//--------------------------------------------
   /**
    * 
    * Constructor for JDODAO initializes internal 
    * data settings.
    * @param aDataSourceSupported flag
    */
   protected JDODAO(boolean aDataSourceSupported)
   {
      this.dataSourceSupported = aDataSourceSupported;

      createdManager = false;

      try

      {

         setPersistenceManager(JDOFactory.getInstance()
         .createPersistenceManager(this));

         createdManager = true;

      }

      catch (Exception e)

      {

         throw new ConnectionException(Debugger.stackTrace(e));

      }

   }//--------------------------------------------
   /**
    * 
    * Constructor for JDODAO initalizes internal 
    * data settings.
    * Data Source is Supported
    * @param aUser the access use
    */
   protected JDODAO(SecurityCredential aUser)
   {
      this(aUser, true);
   }//--------------------------------------------
   /**
    * 
    * 
    * 
    * @param aUser
    *  
    */

   protected JDODAO(SecurityCredential aUser, boolean aDataSourceSupported)
   {

      user = null;

      try
      {
         this.dataSourceSupported = aDataSourceSupported;

         setUser(aUser);

         setPersistenceManager(JDOFactory.getInstance()
         .createPersistenceManager(this));

         createdManager = true;

      }

      catch (Exception e)

      {

         throw new ConnectionException(Debugger.stackTrace(e));

      }

   }//--------------------------------------------

   protected JDODAO(JDODAO aDAO)
   {

      user = null;

      createdManager = false;

      setPersistenceManager(aDAO.manager);

      user = aDAO.user;
      
      this.sql = aDAO.sql;
      this.autoCommit = aDAO.autoCommit;
      this.dataSourceSupported = aDAO.dataSourceSupported;
   }//--------------------------------------------
   /**
    * 
    * Constructor for JDODAO initalizes internal 
    * data settings.
    * 
    * The data source is supported
    * @param aUser the access user
    * @param aPersistenceManager JDO persistence manager
    */
   protected JDODAO(SecurityCredential aUser,
                    PersistenceManager aPersistenceManager)
   {
      this(aUser, aPersistenceManager, true);
   }//--------------------------------------------
   /**
    * 
    * Constructor for JDODAO initalizes internal 
    * data settings.
    * @param aUser  the access user
    * @param aPersistenceManager JDO persistence manager
    * @param aDataSourceSupported
    */
   protected JDODAO(SecurityCredential aUser,
   PersistenceManager aPersistenceManager, boolean aDataSourceSupported)
   {

      user = null;
      this.dataSourceSupported = aDataSourceSupported;

      createdManager = false;

      setPersistenceManager(aPersistenceManager);

      setUser(aUser);

   }//--------------------------------------------

   /**
    * 
    * Persist object to the data store
    * 
    * @param aObject the object to persist
    *  
    */

   protected void insert(Object aObject)
   throws DuplicateRowException, SQLException, IntegrityConstraintException, DuplicateRowException
   {
      try
      {

      //logger.debug("GCSM INSERTING "+Debugger.toString(aObject));

      //manager.currentTransaction().begin();
      
      
      if(aObject instanceof Auditable)
         this.setInsertAuditing((Auditable)aObject);
      
      manager.makePersistent(aObject);

      //manager.currentTransaction().commit();

      if (autoCommit)
         commit();
      }
      catch(Exception e)
      {
         if(e.toString().indexOf("unique") > -1)
         {
            throw new DuplicateRowException("aObject="+aObject+" error="+Debugger.stackTrace(e));
         }
         else if(e.toString().indexOf("integrity") > -1)
         {
            throw new IntegrityConstraintException("aObject="+aObject+" error="+Debugger.stackTrace(e));
         }         
         else
         {
            throw new SQLException("aObject="+aObject+" error="+Debugger.stackTrace(e));
         }
      }
   }//--------------------------------------------

   /**
    * 
    * Persist object to the data store
    * 
    * @param aObject
    *           the object to persist
    *  
    */

   protected Object update(Object aObject)

   throws NoDataFoundException, SQLException,IntegrityConstraintException, DuplicateRowException

   {
      try
      {
      
         if(aObject instanceof Auditable)
            this.setUpdateAuditing((Auditable)aObject);
   
         //manager.
   
         manager.makePersistent(aObject);
   
         if (autoCommit)
            commit();
         
         return aObject;
      }
      catch(Exception e)
      {
         Debugger.printError(this,"JDO error "+e);
         
         if(e.toString().indexOf("value too large") > -1)
         {
            throw new IllegalArgumentException("Object has value too large object="+aObject);
         }
         else if(e.toString().indexOf("integrity") > -1)
            throw new IntegrityConstraintException("object="+aObject+" "+Debugger.stackTrace(e));
         else if(e.toString().indexOf("unique") > -1)
            throw new DuplicateRowException("object="+aObject+" "+Debugger.stackTrace(e));
         else
         {
            if(e.toString().indexOf("object was never registered") > -1)
            {
               //try and remove object from cache topLink error
               try{ this.manager.refresh(aObject); } catch(Exception err){}
               
               throw new JDOException("object="+aObject+" "+Debugger.stackTrace(e));
            }
            
            throw new SQLException("object="+aObject+" "+Debugger.stackTrace(e));
         }
      }
   }//--------------------------------------------

   /**
    * 
    * Persist all object in the given array collection
    * 
    * @param aCollection
    *  
    */

   protected void update(Object aCollection[])

   throws NoDataFoundException, SQLException

   {

      manager.makePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   protected void update(Collection<?> aCollection)
                                                throws NoDataFoundException,
                                                SQLException
   {
      if (aCollection == null)
         throw new IllegalArgumentException(
         "aCollection required in JDODAO.update");

      manager.makePersistentAll(aCollection);

      //manager.

      if (autoCommit)
         commit();

   }//--------------------------------------------

   /**
    * 
    * Persist all object in the given array collection
    * 
    * @param aCollection
    *  
    */

   protected void insert(Object aCollection[])

   throws DuplicateRowException, SQLException

   {

      manager.makePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   protected void insert(Collection<?> aCollection)

   throws DuplicateRowException, SQLException

   {

      manager.makePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   /**
    * 
    * Persist object to the data store
    * 
    * @param aObject
    *           the object to persist
    *  
    */

   protected void save(Object aObject)
   throws NoDataFoundException, SQLException, DuplicateRowException, IntegrityConstraintException

   {

      manager.makePersistent(aObject);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   /**
    * 
    * Persist all object in the given array collection
    * 
    * @param aCollection
    *  
    */

   protected void save(Object aCollection[])

   throws NoDataFoundException, SQLException, DuplicateRowException

   {

      manager.makePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   protected void save(Collection<?> aCollection)

   throws NoDataFoundException, SQLException, DuplicateRowException

   {

      manager.makePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------
   protected void deleteAll(Collection<?> aCollection)
   throws NoDataFoundException, SQLException
   {
      manager.deletePersistentAll(aCollection);
      
      //manager.evictAll(aCollection);
      
      if (autoCommit)
         commit();
   }//--------------------------------------------
   protected void delete(Object aObject)

   throws NoDataFoundException, SQLException

   {

      manager.deletePersistent(aObject);
      
      //manager.evict(aObject);

      if (autoCommit)
         commit();

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @param aQueryBuilder
    *           the query builder
    * 
    * @return collections of objects
    * 
    * @throws NoDataFoundException
    *            when rows returned
    *  
    */

   protected Object select(JDOQueryBuilder aQueryBuilder)
   throws NoDataFoundException, SQLException
   {

      if (aQueryBuilder == null)

         throw new IllegalArgumentException("aQueryBuilder required in JDODAO");

      //manager.currentTransaction().setNontransactionalRead(false);

      Object results = aQueryBuilder.getQueryResults();

      if (results == null)

         throw new NoDataFoundException(aQueryBuilder.toString());

      if (results instanceof Collection)

      {

         Collection<?> collection = (Collection<?>) results;

         if (collection.isEmpty())

            throw new NoDataFoundException(aQueryBuilder.toString());

      }

      return results;

   }//--------------------------------------------

   /**
    * 
    * Delete all objects in the collection
    * 
    * @param aCollection
    *           the collection
    *  
    */

   protected void delete(Collection<?> aCollection)

   throws NoDataFoundException, SQLException

   {

      manager.deletePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   /**
    * 
    * Delete all objects in the array collection
    * 
    * @param aCollection
    *           the array collection
    *  
    */

   protected void delete(Object aCollection[])

   throws NoDataFoundException, SQLException

   {

      manager.deletePersistentAll(aCollection);

      if (autoCommit)

         commit();

   }//--------------------------------------------

   /**
    * 
    * Clean up DAO resources
    *  
    */

   public void dispose()
   {
      String className = this.getClass().getName();
      
      Debugger.println(this,"START disposing "+className);

      if (manager != null && createdManager)

         try

         {

            try

            {

               if (autoCommit)

                  commit();

            }
            catch (Exception commitError)
            {

               Debugger.printWarn(commitError);
            }

            manager.close();
            
            Debugger.println(this,"CLOSED PersistenceManager");
            
            if(this.sql != null)
            {
               sql.dispose();
            }

         }
         catch (Exception e)
         {
         }
         
      Debugger.println(this,"END disposing "+className);
   }//--------------------------------------------

   /**
    * 
    * Commit the current transaction
    * 
    * 
    *  
    */

   public void commit()
   {
      
      try
      {
         manager.currentTransaction().commit();
      }
      catch(Exception e)
      {
    	  Debugger.println(this,"JDO error "+e);
         
         if(e.toString().indexOf("integrity") > -1)
            throw new IntegrityConstraintException(Debugger.stackTrace(e));
         else if(e.toString().indexOf("unique") > -1)
            throw new DuplicateRowException(Debugger.stackTrace(e));
         else
         {
            if(e.toString().indexOf("object was never registered") > -1)
            {               
               throw new JDOException(Debugger.stackTrace(e));
            }
            
            throw new SystemException(Debugger.stackTrace(e));
         }
      }
      
   }//---------------------------------------------

   /**
    * 
    * Commit the current transaction
    * 
    * 
    *  
    */

   public void rollback()
   {
      try
      {
        manager.currentTransaction().rollback();
      }
      catch(Exception e)
      {
    	  Debugger.printWarn(e);
      }

   }//---------------------------------------------
   


   /**
    * 
    * Get instance of the JDOQueryBuilder
    * 
    * @param aClass
    *           the value object configured in the JDO configuration
    * 
    * @return instance of JDOQueryBuilder for the aClass value object
    *  
    */

   protected JDOQueryBuilder createQueryBuilder(Class<?> aClass)

   {

      return JDOFactory.getInstance().createQueryBuilder(aClass, this);

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @return the user for the DAO
    *  
    */

   protected SecurityCredential getUser()

   {

      return user;

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @param user
    *           the user of the DAO
    *  
    */

   protected void setUser(SecurityCredential user)

   {

      if (user == null)

      {

         throw new IllegalArgumentException("user required in TopLinkDAO");

      }

      else

      {

         this.user = user;

         return;

      }

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @return the JDO persist manager
    *  
    */

   PersistenceManager getPersistenceManager()

   {

      return manager;

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @param manager
    *           the persist manager to set
    *  
    */

   private void setPersistenceManager(PersistenceManager manager)

   {

      if (manager == null)

      {

         throw new IllegalArgumentException("manager required in TopLinkDAO");

      }
      else

      {

         this.manager = manager;

         return;

      }

   }//--------------------------------------------

   /**
    * 
    * @return Returns the autoCommit.
    *  
    */

   protected boolean isAutoCommit()

   {

      return autoCommit;

   }//--------------------------------------------

   /**
    * 
    * @param autoCommit
    *           The autoCommit to set.
    *  
    */

   protected void setAutoCommit(boolean autoCommit)

   {

      this.autoCommit = autoCommit;

   }//--------------------------------------------

   /**
    * 
    * 
    * 
    * @return Calendar.getInstance().getTime()
    *  
    */

   protected Date getCurrentTime()

   {

      return Calendar.getInstance().getTime();

   }//--------------------------------------------

   /**
    * 
    * @param aFrom
    *           form object
    * @param aTo
    *           object to copy into
    * @throws SQLException
    */
   protected void copy(Copier aFrom, Copier aTo) throws SQLException
   {
      try
      {
         aTo.copy(aFrom);
      }
      catch (Exception e)
      {
         throw new SQLException(Debugger.stackTrace(e));
      }
   }//--------------------------------------------
   /**
    * Insert or update a primary key object
    * @param aPrimaryKeys the primary key to save
    * @throws NoDataFoundException
    * @throws SQLException
    */
   protected void saveByPKs(Collection<?> aPrimaryKeys)
   throws NoDataFoundException, SQLException
   {
      if(aPrimaryKeys == null)
         return;
      
      PrimaryKey
      pk = null;
      for (Iterator<?> i = aPrimaryKeys.iterator(); i.hasNext();)
      {
         pk = (PrimaryKey) i.next();
         saveByPK(pk, (Copier)pk);
      }
      
   }//--------------------------------------------
   /**
    * @param aPrimaryKey
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected Object saveByPK(PrimaryKey aPrimaryKey, Copier aCopier) 
   throws SQLException, NoDataFoundException
   {
      if(aPrimaryKey.getPrimaryKey() > 0)
         return updateByPK(aPrimaryKey, aCopier);
      else
      {
         insert(aCopier);
         return aCopier;
      }
   }//--------------------------------------------
   /**
    * Insert or update collection of NumberedProperty
    * @param aNumberedProperties collection NumberedProperty
    */
   protected void saveNumberedProperties(Collection<?> aNumberedProperties)
   throws SQLException
   {      
      if(aNumberedProperties ==  null)
         return;
      
      NumberedProperty numberedProperty = null;
      for (Iterator<?> i = aNumberedProperties.iterator(); i.hasNext();)
      {
         numberedProperty = (NumberedProperty) i.next();
         saveNumberedProperty(numberedProperty);
      }
   }//--------------------------------------------  
   /**
    * Update or insert NumberedProperty
    * @param aNumberedProperty
    * @throws SQLException
    */
   protected void saveNumberedProperty(NumberedProperty aNumberedProperty)
   throws SQLException
   {      
      try
      {
         //select existing object
         NumberedProperty managedObject = (NumberedProperty)((Collection<?>)this.selectByNumberPropery(aNumberedProperty)).iterator().next();
         copy(aNumberedProperty, managedObject);
         
         this.update(managedObject);
      }
      catch(NoDataFoundException e)
      {
         //INSERT OBJECT
         this.insert(aNumberedProperty);         
      }
   }//--------------------------------------------
   /**
    * Retrieve all details of NumberedProperty object
    * @param aNumberedProperty the name and number object to select
    * @return managed instance of the object
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected Object selectByNumberPropery(NumberedProperty aNumberedProperty)
   throws SQLException, NoDataFoundException
   {
      JDOQueryBuilder query = this.createQueryBuilder(aNumberedProperty.getClass());
      JDOQueryBuilder numberQuery = query.getColumn("number").equal(aNumberedProperty.getNumber());
      JDOQueryBuilder nameQuery = query.getColumn("name").equal(aNumberedProperty.getName());
      
      return this.select(numberQuery.and(nameQuery));
   }//--------------------------------------------
    /** Updated an object based on its primary key
    * @param aPrimaryKey the primary key to update
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected Object updateByPK(PrimaryKey aPrimaryKey, Copier aCopier)
   throws SQLException, NoDataFoundException
   {
      Copier managedObject = (Copier)((Collection<?>)this.selectByPK(aPrimaryKey)).iterator().next();
      
      //copy properties
      copy(aCopier, managedObject);
      
      return update(managedObject);
   }//--------------------------------------------
   /**
    * 
    * @param aID the primary key
    * @param aClass the PrimaryKey class instance
    * @return the PrimaryKey instance
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected PrimaryKey selectPrimaryKeyByPK(Integer aID, Class<?> aClass)
   throws SQLException, NoDataFoundException
   {
      JDOQueryBuilder query = this.createQueryBuilder(aClass);
      query.getColumn("primaryKey").equal(aID);
      return (PrimaryKey)((Collection<?>)
       this.select(query.getColumn("primaryKey").equal(aID))).iterator().next();
   }//--------------------------------------------   
   /**
    * Select object data by primaryKey attribute
    * @param aPrimaryKey the primary key
    * @return managed object data
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected Collection<?> selectByPK(PrimaryKey aPrimaryKey)
   throws SQLException, NoDataFoundException
   {
      JDOQueryBuilder query = this.createQueryBuilder(aPrimaryKey.getClass());
      return (Collection<?>)this.select(query.getColumn("primaryKey").equal(aPrimaryKey.getPrimaryKey()));
   }//--------------------------------------------
   protected Collection<?> selectByKey(Key<?> aKey)
   throws SQLException, NoDataFoundException
   {
      JDOQueryBuilder query = this.createQueryBuilder(aKey.getClass());
      return (Collection<?>)this.select(query.getColumn("key").equal(aKey.getKey()));
   }//--------------------------------------------
   
   /**
    * 
    * @param aAuditable populate object update info
    */
   protected void setInsertAuditing(Auditable aAuditable)
   {
      if (aAuditable == null)
         throw new IllegalArgumentException(
         "aAuditable required in JDODAO.setInsertAuditing");
      
      java.util.Date now = this.getCurrentTime();
      
      if(aAuditable.getCreateDate() == null)
         aAuditable.setCreateDate(now);
      
      if(aAuditable.getUpdateDate() == null)
         aAuditable.setUpdateDate(now);
      
      if(this.getUser() == null)
      {
         throw new SystemException("DAO user required for insert/update/delete operations");
      }
      
      if(aAuditable.getCreateUserID() == null)
         aAuditable.setCreateUserID(this.getUser().getLoginID());
      
      
      
      
   }//--------------------------------------------
   /**
    * 
    */
   protected void setUpdateAuditing(Collection<?> aAuditables)
   {
      if (aAuditables == null)
         throw new IllegalArgumentException(
         "aAuditables required in QuestionDAO.setUpdateAuditing");
      
      Auditable auditable = null;
      for (Iterator<?> i = aAuditables.iterator(); i.hasNext();)
      {
         auditable = (Auditable) i.next();
         setUpdateAuditing(auditable);
      }
      
   }//--------------------------------------------
   /**
    * 
    */
   protected void setInsertAuditing(Collection<?> aAuditables)
   {
      if (aAuditables == null)
         throw new IllegalArgumentException(
         "aAuditables required in QuestionDAO.setInsertAuditing");
      
      Auditable auditable = null;
      for (Iterator<?> i = aAuditables.iterator(); i.hasNext();)
      {
         auditable = (Auditable) i.next();
         setInsertAuditing(auditable);
      }
      
   }//--------------------------------------------
   /**
    * 
    * @param aClass the Attribute class instance
    * @param aName the name of the attribute
    * @return the attribute object instance
    * @throws SQLException
    * @throws NoDataFoundException
    */
   @SuppressWarnings("unchecked")
protected <K,V> Attribute<K,V> selectAttributeByName(Class<?> aClass, String aName)
   throws SQLException, NoDataFoundException
   {
      JDOQueryBuilder query = this.createQueryBuilder(aClass);
      return (Attribute<K,V>)((Collection<?>)this.select(
                query.getColumn("name").equal(aName))).iterator().next();
   }//--------------------------------------------
   /**
    * 
    * @param aAuditable populate object update info
    */
   protected void setUpdateAuditing(Auditable aAuditable)
   {
      if (aAuditable == null)
         throw new IllegalArgumentException(
         "aAuditable required in QuestionDAO.setUpdateAuditing");      
                  
      if(this.getUser() == null)
      {
         throw new SystemException("DAO user required for insert/update/delete operations");
      }
      
      java.util.Date now = this.getCurrentTime();
      
      
      if(aAuditable.getCreateDate() == null ||
         aAuditable.getCreateUserID() == null )
         this.setInsertAuditing(aAuditable);
      
      
      if(aAuditable.getUpdateDate() == null)
         aAuditable.setUpdateDate(now);
      
      
      if(aAuditable.getUpdateUserID() == null)
         aAuditable.setUpdateUserID(this.getUser().getLoginID());
      

   }//--------------------------------------------
   /**
    * 
    * @param aNameable the name to search for updating
    * @param aCopier the data copier
    * @return object
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected Object updateByNameable(Nameable aNameable, Copier aCopier)
   throws SQLException, NoDataFoundException
   {
      Copier managedObject = (Copier)((Collection<?>)this.selectByNameable(aNameable)).iterator().next();
      
      //copy properties
      copy(aCopier, managedObject);
      
      return update(managedObject);
   }//--------------------------------------------
   /**
    * 
    * @param aNameable nameable
    * @return the nameable
    * @throws SQLException
    * @throws NoDataFoundException
    */
   protected Collection<?> selectByNameable(Nameable aNameable)
   throws SQLException, NoDataFoundException
   {
      JDOQueryBuilder query = this.createQueryBuilder(aNameable.getClass());
      return (Collection<?>)this.select(query.getColumn("name").equal(aNameable.getName()));
   }//--------------------------------------------
   /**
    * Wraps  manager makePersistent functions
    * @param aObject
    */
   protected void makePersistent(Object aObject)
   {
      if (aObject == null)
         throw new IllegalArgumentException(
         "aObject required in JDODAO.makePersistent");
      
      if(aObject instanceof Auditable)
         this.setUpdateAuditing((Auditable)aObject);
      
      this.manager.makePersistent(aObject);
   }//--------------------------------------------
   /**
    * @param aObjects the objects to persist
    */
   protected void makePersistentAll(Object[] aObjects)
   {
      manager.makePersistentAll(aObjects);
   }//--------------------------------------------
   /**
    * @param aObjects the objects to persist
    */
   protected void makePersistentAll(Collection<?> aObjects)
   {
      manager.makePersistentAll(aObjects);
   }//--------------------------------------------
   /**
    * @param aQuery the JDOQueryBuilder
    * @return aQuery.getColumn("deletedCode").equal(Data.NO)
    */
   protected JDOQueryBuilder createNotDeletedQuery(JDOQueryBuilder aQuery)
   {
      return aQuery.getColumn("deletedCode").equal(Data.NO);
   }//--------------------------------------------
   
   /**
    * @return Returns the dataSourceSupported.
    */
   protected boolean isDataSourceSupported()
   {
      return dataSourceSupported;
   }//--------------------------------------------
   /**
    * @param dataSourceSupported The dataSourceSupported to set.
    */
   protected void setDataSourceSupported(boolean dataSourceSupported)
   {
      this.dataSourceSupported = dataSourceSupported;
   }//--------------------------------------------
   /**
    * @return this.getSQL().selectIntegers(aSQL)
    */
   protected Integer[] selectIntegers(String aSQL)
   throws SQLException, NoDataFoundException
   {
      return this.getSQL().selectIntegers(aSQL);
   }//--------------------------------------------
   private SQL getSQL()
   {
     if(sql == null)
        sql = SQL.getInstance();
     
     return sql;
   }//--------------------------------------------


   private transient SecurityCredential user = null;

   private transient PersistenceManager manager = null;

   private transient boolean createdManager;

   private transient boolean autoCommit = true;
   private boolean dataSourceSupported = true;
   private SQL sql = null;
}
