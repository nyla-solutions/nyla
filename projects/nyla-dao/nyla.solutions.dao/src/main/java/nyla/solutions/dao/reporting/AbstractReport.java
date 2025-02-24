package nyla.solutions.dao.reporting;

import java.sql.Connection;
import java.sql.ResultSet;



import nyla.solutions.dao.DAO;
import nyla.solutions.dao.DAOFactory;
import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.security.data.SecurityCredential;
import nyla.solutions.core.util.Debugger;


/**
 * 
 * <b>AbstractReport</b> is a abstract implement of  
 * @author Gregory Green
 *
 */
public abstract class AbstractReport extends DAO implements Report
{
   /**
    * 
    * Constructor for AbstractReport initializes internal
    */
   protected AbstractReport()
   {
      super();
   }// --------------------------------------------
   /**
    * Constructor
    * @param jdbcDriver
    * @param connectionURL
    * @param user
    * @param password
    * @throws ConnectionException
    */
   public AbstractReport(String jdbcDriver, String connectionURL, String user,
	   char[] password) throws ConnectionException
   {
	super(jdbcDriver, connectionURL, user, password);	
   }// ----------------------------------------------

   protected AbstractReport(Connection connection) throws ConnectionException
   {
      super(connection);
   }// --------------------------------------------
   protected AbstractReport(DAO adao)
   {
      super(adao);
   }// --------------------------------------------
   protected AbstractReport(SecurityCredential user, Connection connection)
   throws ConnectionException
   {
      super(user, connection);
   }// --------------------------------------------
   protected AbstractReport(SecurityCredential user) throws ConnectionException
   {
      super(user);
   }// --------------------------------------------
   /**
    * 
    *
    * @see nyla.solutions.dao.reporting.Report#printReport(java.sql.ResultSet)
    */
   public void printReport(ResultSet resultSet)
   {
      Debugger.println(this, "print results");
      
      
      try
      {
         //print column names
         
         printHeader(DAOFactory.getColumnNames(resultSet));
         
         
         while(resultSet.next())
         {
            this.printRow(new nyla.solutions.dao.ResultSetDataRow(resultSet));
         }
      }
      catch(java.sql.SQLException e)
      {
         throw new SystemException(Debugger.stackTrace(e));
      }
      finally
      {
         if (resultSet != null)
            try{ resultSet.close();} catch (Exception e){}
      }
      
   }// --------------------------------------------   
   /**
    * print the information for a single row
    * @param dataRow the row to print the information
    */
   protected void printRow(DataRow dataRow)
   {
        printRow(dataRow.getStrings());
   }//--------------------------------------------
  
}
