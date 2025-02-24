package nyla.solutions.dao;

import nyla.solutions.dao.jdbc.JdbcConstants;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.DuplicateRowException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.data.*;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;

/**
 * 
 * <pre>
 *  SQL provides a set of functions to execute JDBC operations.
 *  
 *  Sample config.properties
 *  
 *  jdbc.driver=oracle.jdbc.driver.OracleDriver
 *  jdbc.connection.url=jdbc:oracle:thin:@localhost:1521:local
 *  jdbc.user=solutions
 *  jdbc.password=solutions
 *  jdbc.autoCommit=true
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */

public class SQL extends DAO implements Inserter
{
   /**
    * 
    * Constructor for SQL initializes internal 
    * data settings.
    * @param aConnection the database connection
    * @throws ConnectionException
    */
   protected SQL(Connection aConnection) throws ConnectionException
   {
      super(aConnection);
   }// --------------------------------------------
   /**
    * 
    * @return the connection object
    * @throws ConnectionException
    */
   public static Connection createJDBCConnection(String aDriver, String aConnectionURL,String aUser,char[] aPassword)
   throws Exception
   {
      return DAOFactory.createJDBCConnection(aDriver, aConnectionURL, aUser, aPassword);
   }//--------------------------------------------
   
   /**
    * @param rs
    * @throws SQLException
    * @throws IOException
    */
   public static void setClob(ResultSet rs, int clobPosition, String clobText) 
   throws SQLException, IOException
   {
	Clob clob = rs.getClob(clobPosition);	 
	java.io.Writer out =  null;	 
	 try
	 {
	    Debugger.println(SQL.class,"setting clobText="+clobText);
	    
	    out = clob.setCharacterStream(clobText.length());
	    out.write(clobText);
	    out.flush();		 
	}
	finally
	{
	    if(out != null)
		 try { out.close(); } catch(Exception e){}
	}
   }// ----------------------------------------------

   public <OutputType,CriteriaType> Pagination 
   selectPaging(String sql, CriteriaType criteria, PreparedStatementConstructor<CriteriaType> setter, 
		   ResultSetObjectCreator<OutputType> mapper, Paging<OutputType> paging) 
   throws SQLException 
   {
	   return super.selectPaging(sql, criteria, setter, mapper, paging);
   }// --------------------------------------------------------
   /**
    * 
    * @see nyla.solutions.dao.DAO#executeUpdate(java.lang.String, java.lang.Object, nyla.solutions.dao.PreparedStatementConstructor)
    */
   public <CriteriaType> int executeUpdate(String sql, CriteriaType criteria, nyla.solutions.dao.PreparedStatementConstructor<CriteriaType> setter) 
   throws SQLException 
   {
	   return super.executeUpdate(sql,criteria,setter);
   }// --------------------------------------------------------
   /**
    * @param rs
    * @throws SQLException
    * @throws IOException
    */
   public static void setClob(ResultSet rs, int clobPosition, File clobFile) 
   throws SQLException, IOException
   {
	Clob clob = rs.getClob(clobPosition);	 
	java.io.OutputStream out =  null;	 
	InputStream in = null;
	 try
	 {
	    in = new FileInputStream(clobFile);
	    Debugger.println(SQL.class,"setting clob from file="+clobFile.getAbsolutePath());
	    
	    out = clob.setAsciiStream(clobFile.length());
	    
	    IO.write(out, in);
	    out.flush();		 
	}	
	finally
	{
	   if(in != null)
		try{ in.close(); } catch(Exception e){}
		
	    if(out != null)
		 try { out.close(); } catch(Exception e){}
	}
   }// ----------------------------------------------
   /**
    * 
    * @param rs
    * @return
    */
   public static DataRow toDataRow(ResultSet rs)
   throws SQLException
   {
	   return DAO.toDataRow(rs);
   }// --------------------------------------------------------
   /**
    * Singleton factory method
    * 
    * @return a single instance of the SQL object for the JVM
    */
   public static SQL getInstance()
   {
      if (isSingleton())
      {
         if (instance == null || instance.isClosed())
         {
            instance = null;

            instance = new SQL(DAOFactory.createJDBCConnection());
         }

         return instance;
      }
      else
      {
         return new SQL(DAOFactory.createJDBCConnection());
      }
   }// --------------------------------------------
   /**
    * Create created SQL must be disposed
    * @param the connection to wrap with the connection object
    * @return a SQL connection
    */
   public static SQL connect(Connection connection)
   {
	   return new SQL(connection);
   }// --------------------------------------------------------
   
   /**
    * Create created SQL must be disposed
    * @param jdbcDriver the JDBC driver
    * @param connectionURL the connection URL
    * @param dbUserName the database user name
    * @param dbPassword the database password
    * @return a connection
    */
   public static SQL connect(String jdbcDriver, String connectionURL, String dbUserName, char[] dbPassword)
   {
	if (jdbcDriver == null || jdbcDriver.length() == 0)
	   throw new RequiredException("jdbcDriver");
	
	if (connectionURL == null || connectionURL.length() == 0)
	   throw new RequiredException("connectionURL");
	
	if (dbUserName == null || dbUserName.length() == 0)
	   throw new RequiredException("dbUserName");
	
	
      try
      {
         return new SQL(SQL.createJDBCConnection(jdbcDriver, connectionURL, dbUserName, dbPassword));
      }
      catch (ConnectionException e)
      {        
         throw e;
      }
      catch (Exception e)
      {
         
         throw new ConnectionException(Debugger.stackTrace(e));
      }
      
   }//--------------------------------------------
   /**
    * True if no data source (with connection pooling) is specified.
    * 
    * @return Text.isNull(Config.getProperty(Constants.DS_NAME_PROP,""))
    */
   private static boolean isSingleton()
   {
      return Text.isNull(Config.getProperty(JdbcConstants.DS_NAME_PROP, ""));

   }// --------------------------------------------

   /**
    * @return the single first result
    */
   public synchronized String selectString(String aSQL) throws SQLException
   {
      return super.selectString(aSQL);
      
   }// --------------------------------------------
   public synchronized String[] selectStrings(String aSQL) throws SQLException
   {
      return super.selectStrings(aSQL);
      
   }// --------------------------------------------
   public Collection<String> toColumnNames(ResultSet aResultSet) throws SQLException
   {
      return super.toColumnNames(aResultSet);
   }// --------------------------------------------
   /**
    * 
    * @see nyla.solutions.dao.DAO#selectStrings(java.lang.String, java.lang.Object[])
    */
   public synchronized String[] selectStrings(String aSQL, Object[] aInputs) throws  SQLException
   {
      // TODO Auto-generated method stub
      return super.selectStrings(aSQL, aInputs);
      // ----------------------------------------
   }
   /**
    * Determine if select has results
    * @param ps the prepared statement
    * @return true result set has results
    * @throws SQLException
    */
   public boolean selectHasResults(PreparedStatement ps)
   throws SQLException
   {
	   return super.selectHasResults(ps);
   }// --------------------------------------------------------
   public synchronized Integer[] selectIntegers(String aSQL) throws NoDataFoundException, SQLException
   {
      return super.selectIntegers(aSQL);
      // ----------------------------------------
   }

   public synchronized CallableStatement prepareCall(String aSQL) throws SQLException
   {
      return super.prepareCall(aSQL);
   }

   public synchronized Date getDate(ResultSet aResultSet, String aName)
   {
      return super.getDate(aResultSet, aName);
   }

   public synchronized int getInt(ResultSet aResultSet, String aName)
   {
      return super.getInt(aResultSet, aName);
   }

   public synchronized String getString(ResultSet aResultSet, String aName)
   throws SQLException
   {
      return super.getString(aResultSet, aName);
   }// --------------------------------------------
   public <CriteriaType> boolean selectHasResults(String sql, CriteriaType criteria,
			  PreparedStatementConstructor<CriteriaType> setter)
   throws SQLException
	{
	   return super.selectHasResults(sql, criteria, setter);
	}// --------------------------------------------------------
   /**
    * 
    * @see nyla.solutions.dao.DAO#selectDataResultSet(java.lang.String)
    */
   public DataResultSet selectDataResultSet(String aSQL) throws NoDataFoundException, SQLException
   {
      return super.selectDataResultSet(aSQL);
   }// --------------------------------------------------------
   /**
    * 
    * @see nyla.solutions.dao.DAO#getTimestamp(java.sql.ResultSet, java.lang.String)
    */
   public synchronized Timestamp getTimestamp(ResultSet aResultSet, String aName)
   {
      return super.getTimestamp(aResultSet, aName);
   }// --------------------------------------------------------
   public synchronized void initPreparedStatement(Object[] aInputs,
                                                  PreparedStatement stmt) throws SQLException
   {
      super.initPreparedStatement(aInputs, stmt);
   }// --------------------------------------------------------

   
   public synchronized boolean insert(Object[] aInputs, String aSQL) 
   throws SQLException, DuplicateRowException
   {
      return super.insert(aInputs, aSQL);
   }

   public synchronized boolean insert(PreparedStatement aStatement) throws DuplicateRowException, SQLException
   {
      return super.insert(aStatement);
   }//---------------------------------------------
   /**
    * 
    * @param aSQL the SQL statement
    * @throws SQLException when an internal error occurs
    * @throws NoDataFoundException when rows updated
    */
   public synchronized int insert(String aSQL)
   throws SQLException, DuplicateRowException
   {
	
	   return super.insert(aSQL);
	
   }//---------------------------------------------

   public synchronized int nextVal(String aSQL) throws SQLException
   {
      return super.nextVal(aSQL);
   }// --------------------------------------------------------
   /**
    * 
    * @see nyla.solutions.dao.DAO#select(java.sql.PreparedStatement)
    */
   public synchronized ResultSet select(PreparedStatement aStatement) 
   throws SQLException
   {
      return super.select(aStatement);
   }// --------------------------------------------------------
   /**
    * 
    * @param aSQL the SQL statements
    * @param aInputs the SQL inputs
    * @return true if resultSet.next is true
    * @throws NoDataFoundException
    * @throws SQLException
    */
   public boolean selectHasResults(String aSQL, Object[] aInputs)
   throws SQLException
   {
	   return super.selectHasResults(aSQL,aInputs);
   }// --------------------------------------------------------
   
   public synchronized Integer[] selectIntegers(String aSQL, int aInput) throws NoDataFoundException, SQLException
   {
      return super.selectIntegers(aSQL, aInput);
   }// --------------------------------------------------------
   
   public synchronized Integer[] selectIntegers(String aSQL, Object[] aInputs) throws NoDataFoundException, SQLException
   {
      return super.selectIntegers(aSQL, aInputs);
   }
   public synchronized Property[] selectProperties(int aID, String sql) throws SQLException, NoDataFoundException, SystemException
   {
      return super.selectProperties(aID, sql);
   }
   public synchronized void setCriteria(int aPosition, Criteria aCriteria,
                                        PreparedStatement stmt) throws SQLException
   {
      super.setCriteria(aPosition, aCriteria, stmt);
   }
   public synchronized void setInt(int aPosition, int aInt,
                                   PreparedStatement stmt) throws SQLException
   {
      super.setInt(aPosition, aInt, stmt);
   }
   public synchronized void setString(int aPosition, String aString,
                                      PreparedStatement stmt) throws SQLException
   {
      super.setString(aPosition, aString, stmt);
   }
   public synchronized void setTrueFalse(int aPosition, Boolean aBoolean,
                                         PreparedStatement stmt) throws SQLException
   {
      super.setTrueFalse(aPosition, aBoolean, stmt);
   }
   public synchronized void setYesNo(int aPosition, boolean aBoolean,
                                     PreparedStatement stmt) throws SQLException
   {
      super.setYesNo(aPosition, aBoolean, stmt);
   }
   public synchronized void setYesNo(int aPosition, Boolean aBoolean,
                                     PreparedStatement stmt) throws SQLException
   {
      super.setYesNo(aPosition, aBoolean, stmt);
   }
   public synchronized boolean toBoolean(String aString)
   {
      return super.toBoolean(aString);
   }

   public synchronized int update(Object[] aInputs, String aSQL) throws SQLException, NoDataFoundException
   {
      return super.update(aInputs, aSQL);
   }

   public synchronized int update(PreparedStatement aStatement) throws SystemException, NoDataFoundException
   {
      return super.update(aStatement);
   }// --------------------------------------------

   public synchronized boolean execute(String aSQL) throws SQLException
   {
      Statement stmt = null;
      try
      {
         stmt = super.createStatement();
         
         Debugger.println(this,"sql="+aSQL);
         return stmt.execute(aSQL);
      }
      finally
      {
         if (stmt != null)
            try
            {
               stmt.close();
            }
            catch (Exception e)
            {
            }
      }
   }// --------------------------------------------
   public synchronized Long[] selectLongs(String aSQL) throws NoDataFoundException, SQLException
   {
      return super.selectLongs(aSQL);
   }
   // ----------------------------------------

   public synchronized Integer[] selectIntegers(String aSQL, Object aInput) throws NoDataFoundException, SQLException
   {
      return super.selectIntegers(aSQL, aInput);
   }// --------------------------------------------

   public synchronized int delete(Object aInputs[], String aSQL) throws SQLException
   {
      return super.delete(aInputs, aSQL);
   }// --------------------------------------------

   public synchronized int delete(Object aInputs, String aSQL) throws SQLException, NoDataFoundException
   {
      Object[] inputs = {
         aInputs };
      return super.delete(inputs, aSQL);
   }// --------------------------------------------

   public synchronized void delete(String aSQL) throws NoDataFoundException, SQLException
   {
      super.delete(aSQL);
   }// --------------------------------------------

   public synchronized Statement createStatement() throws SQLException
   {
      return super.createStatement();
   }// --------------------------------------------
   
   /*
    * public synchronized String[] selectStrings(String aSQL, Object aInputs[])
    * throws NoDataFoundException, SQLException { return
    * super.selectStrings(aSQL, aInputs); }
    */
   	private static SQL instance = null;
}
