
package nyla.solutions.dao.hibernate;

import java.util.Collection;
import java.util.List;

import nyla.solutions.dao.OR.ORDAO;
import nyla.solutions.dao.OR.query.QueryBuilder;
import nyla.solutions.global.exception.NoDataFoundException;
import nyla.solutions.global.exception.RequiredException;
import nyla.solutions.global.patterns.Disposable;
import nyla.solutions.global.util.Config;
import nyla.solutions.global.util.Debugger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;

/**
 * 
 * <pre>
 * HibernateDAO is a data access object for Hibernate persistent framework.
 * 
 * Properties
 * 
 * hibernate.connection.driver_class jdbc driver class 
 * hibernate.connection.url jdbc URL
 * hibernate.connection.username database user
 * hibernate.connection.password database user password
 * hibernate.connection.pool_size maximum number of pooled connections
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class HibernateDAO implements Disposable, ORDAO
{
   /**
    * 
    * Constructor for HibernateDAO initalizes internal 
    * data settings.
    */
   public HibernateDAO()
   {
      super();
      init();
   }// --------------------------------------------
   /**
    * 
    * @param aClass the object that the query is based on
    * @return new HibernateQueryBuilder(session.createCriteria(aClass))
    */
   public QueryBuilder createQueryBuilder(Class aClass)
   {
      return new HibernateQueryBuilder(session.createCriteria(aClass));
   }// --------------------------------------------
   /**
    * Select objects based a query builder
    * @param aQueryBuilder the query builder
    * @return the results from the criteria
    * @throws NoDataFoundException
    */
   public Collection select(QueryBuilder aQueryBuilder)
   throws NoDataFoundException
   {
      if (aQueryBuilder == null)
         throw new RequiredException("aQueryBuilder in HibernateDAO.select");
      
      if (!(aQueryBuilder instanceof HibernateQueryBuilder))
      {
         throw new IllegalArgumentException("Query Builder must be instance of HibernateQueryBuilder");
      }
      
      HibernateQueryBuilder builder = (HibernateQueryBuilder)aQueryBuilder;
      List results = builder.getCriteria().list();
      
      if(results == null || results.isEmpty())
         throw new NoDataFoundException(builder.getCriteria().toString());
      
      return results;
      
   }// --------------------------------------------

   /**
    * Initialize the database session
    * and begin a transaction
    *
    */
   private void init()
   {
      session = getSessionFactoryInstance().getCurrentSession();
      
      session.beginTransaction();
   }// --------------------------------------------
   /**
    * 
    * Commit the current transaction
    */
   public void commit()
   {
      if(session != null && session.isConnected())
      {         
         session.getTransaction().commit();
                  
         init();
      }
   }// --------------------------------------------
   /**
    * Delete to object from the database
    * @param aObject the object to delete
    */
   public void deleteObject(Object aObject)
   {
      if (aObject == null)
         throw new IllegalArgumentException(
         "aObject required in HibernateDAO.deleteObject");
      
      this.session.delete(aObject);
   }// --------------------------------------------
   /**
    * Rollback to transaction
    *
    */
   public void rollback()
   {
      if(session != null)
      {
         session.getTransaction().rollback();
      }
   }// --------------------------------------------
   /**
    * 
    * @return the create instance of session factory
    */
   protected static SessionFactory getSessionFactoryInstance()
   {
      if(sessionFactory == null)
      {
         try 
         {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            
            configuration = configuration.configure();  //optional add xml file name
            
            configuration.setProperties(Config.getProperties());
                        
            sessionFactory = configuration.buildSessionFactory();
            
            
         } 
         catch (Throwable ex) 
         {
            Debugger.printError(ex);
            throw new ExceptionInInitializerError(Debugger.stackTrace(ex));
        }
      }
      return sessionFactory;
   }// --------------------------------------------
   /**
    * 
    * @param aQuery the hibernate query
    * @return the collection of results
    */
   protected Collection selectObjects(Class aClass)
   {
      if (aClass == null)
         throw new IllegalArgumentException(
         "aClass required in HibernateDAO.selectObjects");
      
      return this.session.createCriteria(aClass).list();
   }// --------------------------------------------
   /**
    * 
    * @param aClass the class to search for
    * @param aKey the key to search for 
    * @return
    */
   protected Object selectObjectByKey(Class aClass, String aKey)
   throws NoDataFoundException
   {
      if(aClass == null)
         throw new IllegalArgumentException("aClass required in HibernateDAO.selectByKey");
            
      Criteria crit = session.createCriteria(aClass);
      
      crit.add( Expression.eq("key", aKey) );
      crit.setMaxResults(1);
      List results = crit.list();
      if(results.isEmpty())
      {
         throw new NoDataFoundException("class="+aClass.getName()+" key="+aKey);
      }
      
      return results.iterator().next();
      
   }// --------------------------------------------
   /**
    * 
    * @param aClass the class to select
    * @param aProperty the class property to compare
    * @param aValue the value of the property
    * @return
    * @throws NoDataFoundException
    */
   protected Collection selectObjectsByProperty(Class aClass, String aProperty, Object aValue)
   throws NoDataFoundException
   {
      if(aClass == null)
         throw new IllegalArgumentException("aClass required in HibernateDAO.selectByKey");
            
      Criteria crit = session.createCriteria(aClass);
      
      crit.add( Expression.eq(aProperty, aValue) );
      List results = crit.list();
      if(results == null || results.isEmpty())
      {
         throw new NoDataFoundException("class="+aClass.getName()+aProperty+" ="+aValue);
      }
      
      return results;
      
   }// -------------------------------------------
   /**
    * 
    * @param aClass
    * @param aProperty
    * @param aValue
    * @return
    * @throws NoDataFoundException
    */
   protected Object selectObjectByProperty(Class aClass, String aProperty, Object aValue)
   throws NoDataFoundException
   {
      return this.selectObjectsByProperty(aClass, aProperty, aValue).iterator().next();
   }// --------------------------------------------

   /**
    * Select objects by property in
    * @param aClass the class to select
    * @param aProperty the property of the class
    * @param aValues the values to compare
    * @return Collection of the classes
    * @throws NoDataFoundException
    */
   protected Collection selectObjectsByPropertyIn(Class aClass, String aProperty, Object[] aValues)
   throws NoDataFoundException
   {
      if(aValues == null || aValues.length == 0)
         throw new RequiredException("Values in HibernateDAO.selectObjectsByPropertyIn");
      
      if(aClass == null)
         throw new IllegalArgumentException("aClass required in HibernateDAO.selectByKey");
            
      Criteria crit = session.createCriteria(aClass);
      
      crit.add( Expression.in(aProperty, aValues));
      
      List results = crit.list();
      if(results.isEmpty())
      {
         throw new NoDataFoundException("class="+aClass.getName()+aProperty+" ="+Debugger.toString(aValues));
      }
      
      return results;
      
   }// --------------------------------------------

   /**
    * 
    * @param aQuery the hibernate query
    * @return the collection of results
    */
   protected Collection selectObjects(String aQuery)
   {
      if (aQuery == null)
         throw new IllegalArgumentException(
         "aQuery required in HibernateDAO.selectObjects");
      
      return this.session.createQuery(aQuery).list();
   }// --------------------------------------------

   /**
    * Save the object to database
    * @param aObject the object to save
    */
   protected void save(Object aObject)
   {
      if (aObject == null)
         throw new IllegalArgumentException(
         "aObject required in HibernateDAO.save");
      
      this.session.saveOrUpdate(aObject);      
      
   }// --------------------------------------------
   /**
    * 
    * @param aObject insert the object into the defined table
    */
   protected void insert(Object aObject)
   {
      if (aObject == null)
         throw new IllegalArgumentException(
         "aObject required in HibernateDAO.save");
      
      this.session.save(aObject);
      
   }// --------------------------------------------
   /**
    * 
    * Dispose of the database session
    */
   public void dispose()
   {
      Debugger.println("DISPOSING START: "+this.getClass().getName());
      
      if(autoCommit)
         commit();
      
      if(session != null)
      {
         try
         {
            session.close();
            
            Debugger.println("DISPOSING  closed session");
         }
         catch(Exception e)
         {
            Debugger.printError(e);
         }
      }
      
      session = null;
      Debugger.println("END DISPOSING: "+this.getClass().getName());
   }// --------------------------------------------

   private boolean autoCommit = true;
   private Session session = null;
   private static SessionFactory sessionFactory = null;

}