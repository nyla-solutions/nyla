package nyla.solutions.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;

import nyla.solutions.dao.OR.query.AbstractQueryBuilder;
import nyla.solutions.dao.OR.query.QueryBuilder;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;





/**
 * <pre>
 * 
 *  
 *   
 *    Query Builder using the Hibernate criteria object.
 *    
 *    Criteria crit = session.createCriteria(Cat.class);
 *    crit.add( Expression.eq( "color", eg.Color.BLACK ) );
 *    crit.setMaxResults(10);
 *    List cats = crit.list();
 *    
 *   
 *  
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */

public class HibernateQueryBuilder extends AbstractQueryBuilder
{
   /**
    * 
    * Constructor for HibernateQueryBuilder initalizes internal 
    * data settings.
    */
   public HibernateQueryBuilder(Criteria aCriteria)
   {
      setCriteria(aCriteria);
   }// --------------------------------------------
   /**
    * 
    * 
    * @see org.solutions.dao.OR.query.AbstractQueryBuilder#getColumn(java.lang.String)
    */
   public QueryBuilder getColumn(String aAttributName)
   {
      if (aAttributName == null)
         throw new IllegalArgumentException(
         "aAttributName required in HibernateQueryBuilder.getColumn");
      
      currentColumn = aAttributName;
      
      return this;
   }// --------------------------------------------   
   /**
    * 
    * 
    * @see org.solutions.dao.OR.query.AbstractQueryBuilder#equal(java.lang.Object)
    */
   public QueryBuilder equal(Object aArgument)
   {
      add(Restrictions.eq(this.currentColumn, aArgument));
      
      return this;
   }// --------------------------------------------
   /**
    * 
    * @param aArgument like condition to test
    *
    */
   public QueryBuilder like(String aArgument)
   {
      add(Restrictions.like(this.currentColumn, aArgument, MatchMode.ANYWHERE));
      
      return this;
   }// --------------------------------------------
   
   /**
    * @param criteria the criteria to set
    */
   public void setCriteria(Criteria criteria)
   {
      if (criteria == null)
         throw new IllegalArgumentException(
         "criteria required in HibernateQueryBuilder.setCriteria");
      
      this.criteria = criteria;
   }// --------------------------------------------
   public QueryBuilder and(QueryBuilder aBuilder)
   {
      HibernateQueryBuilder other = (HibernateQueryBuilder)aBuilder;
      
      if(!other.criterions.isEmpty())
      {
         Criterion otherCriterion = null;
         
         for (Iterator i = other.criterions.iterator(); i.hasNext();)
         {
            otherCriterion = (Criterion) i.next();
            this.criteria.add(otherCriterion);
            
         }         
      }
      
      
      return this;
   }// --------------------------------------------
   /**
    * @return the criteria
    */
   public Criteria getCriteria()
   {
      return criteria;
   }// --------------------------------------------
   private void add(Criterion aRestriction)
   {
      criterions.add(aRestriction);
   }// --------------------------------------------
   private ArrayList criterions = new ArrayList();
   private Criteria criteria = null;
   private String currentColumn = "";
}