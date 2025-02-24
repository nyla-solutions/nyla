package nyla.solutions.dao;

import nyla.solutions.core.patterns.Disposable;

/**
 * ACID (atomicity, consistency, isolation, durability) is a set of properties 
 * that guarantee database transactions are processed reliably (from wikipedia).
 * 
 * @author Gregory Green
 *
 */
public interface ACID extends Disposable
{
   /**
    * Commit the transaction
    */
   void commit();
   
   /**
    * roll back the transaction
    */
   void rollback();
}
