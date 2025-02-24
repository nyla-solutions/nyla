package nyla.solutions.dao;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;




import nyla.solutions.core.patterns.Disposable;
import nyla.solutions.core.util.Debugger;

/**
 * <pre>
 * JDBCResultSetIterator this class implements an iterator.
 * Wrappers any needed prepared statement or results set. 
 * The prepared statement and result sets are closed when
 * the dispose method is called. 
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class JDBCResultSetIterator implements Iterator<ResultSet>, Disposable
{
   /**
    * 
    * Constructor for JDBCResultSetIterator initalizes internal 
    * data settings.
    * @param aStatement the JDBC statement
    * @param aResultSet the result set
    */
   public JDBCResultSetIterator(Statement aStatement, ResultSet aResultSet)
   {
      if(aStatement == null)
         throw new IllegalArgumentException("aStatement required in JDBCResultSetIterator.JDBCResultSetIterator");
      
      if(aResultSet == null)
         throw new IllegalArgumentException("aResultSet required in JDBCResultSetIterator.JDBCResultSetIterator");
      
      this.statement = aStatement;
      this.resultSet = aResultSet;
   }// --------------------------------------------

   /**
    * Advance the result set
    * @see java.util.Iterator#hasNext()
    */
   public boolean hasNext()
   {
      boolean value = false;
      
      try
      {
         value = resultSet.next();
      }
      catch (Exception e)
      {
         Debugger.printWarn(e);
      }
      
      if(!value)
      {
         this.dispose();
      }
      
      return value;
   }// --------------------------------------------
   /**
    * 
    * @see java.util.Iterator#next()
    * @return the current resultSet
    */
   public ResultSet next()
   {
      return this.resultSet;
   }// --------------------------------------------

   /**    
    * Not implemented
    * @see java.util.Iterator#remove()
    */
   public void remove()
   {
   }// --------------------------------------------   
   /**
    * 
    * @see java.lang.Object#finalize()
    */
   protected void finalize() throws Throwable
   {
      super.finalize();
      
      if(!disposed)
      {
    	  Debugger.printWarn(this,"Result set and statement connection leak. Remember to call "+getClass().getName()+".dispose()");
      }
   }// --------------------------------------------

   /**
    * Closing the result set 
    * statement
    *
    */
   public void dispose()
   {
      if(!disposed)
      {
         if(resultSet != null) try{ resultSet.close(); } catch(Exception e){}
         
         if(statement != null ) try { statement.close(); }  catch(Exception e){}         
      }
      
      disposed = true;
   }// --------------------------------------------

    private boolean disposed = false;
   private ResultSet resultSet = null;
   private Statement statement  = null;
}
