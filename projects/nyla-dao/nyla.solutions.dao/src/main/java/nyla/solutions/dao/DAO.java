package nyla.solutions.dao;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.sql.DataSource;

import nyla.solutions.core.data.Arrayable;
import nyla.solutions.core.data.Criteria;
import nyla.solutions.core.data.Data;
import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.data.Identifier;
import nyla.solutions.core.data.Property;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.DuplicateRowException;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.IntegrityConstraintException;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SizeViolationException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.iteration.RangeCriteria;
import nyla.solutions.core.patterns.iteration.ResultSetIterator;
import nyla.solutions.core.security.data.SecurityCredential;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;
import nyla.solutions.dao.jdbc.JdbcConstants;


/**
*   <b>DAO</b> provides utility functions for
*   concrete DAO classes to utilize. 
*   
*    Sample config.properties
 *  
 *  jdbc.driver=oracle.jdbc.driver.OracleDriver
 *  jdbc.connection.url=jdbc:oracle:thin:@localhost:1521:local
 *  jdbc.user=solutions
 *  jdbc.password=solutions
 *  
*   <b>design_pattern:</b> Data Access Objects
*   @author Gregory Green
*   @version 1.0
*/
public abstract class DAO implements ACID, Connectable
{
	public static final int BATCH_SIZE = Config.getPropertyInteger(DAO.class,"BATCH_SIZE",10).intValue();
	
	/**
	 * STRING_TYPENAME  = Config.getProperty(DAO.class,"STRING_TYPENAME", "VARCHAR")
	 */
	public static String STRING_TYPENAME  = Config.getProperty(DAO.class,"STRING_TYPENAME", "VARCHAR");
	
  /**
   * Represent a no value
   */
   public static final String NO = Data.NO;

  /**
   * Represent a yes value
   */
   public static final String YES = Data.YES;
   
  /**
   * Represent a false value
   */
  public static final String FALSE = Data.FALSE;
  
  /**
   * Represent a true value
   */
  public static final String TRUE = Data.TRUE;
  
  /**
   * Represents a null integer
   */
  public static final int NULL_INT = Data.NULL;
  /**
   * SQL CODE for No data found exception
   */
  public static final int NO_DATA_FOUND = 1403;

  /**
   * SQL CODE for duplicate value on index exception
   */
  public static final int ORACLE_DUP_VAL_ON_INDEX= 1;
  
  /**
   * DB2VAL_IN_INDEX = -803
   */
  public static final int DB2VAL_IN_INDEX = -803;
  
  /**
   * 
   * Constructor
   */
  public DAO()
  {
	  this(false);
  }//--------------------------------------------
  /**
   * 
   * Constructor
   */
  public DAO(boolean connectOnConstruct)
  {
	  if(connectOnConstruct)
		  connect();
     
  }//--------------------------------------------
  /**
   * 
   * @see nyla.solutions.dao.Connectable#isConnected()
   */
  public boolean isConnected()
  {
		return this.the_connection != null;
  }// --------------------------------------------------------
  /**
   * Establish a database connection
   * @throws ConnectionException
   */
  public void connect()
  throws ConnectionException
  {
		if(this.the_connection != null)
			  return; //already connected
		
	  	try
		{

			  
			if(this.dataSource != null)
			 	  this.the_connection = this.dataSource.getConnection();
			  else
			  {
			  
			 	  this.the_connection = DAOFactory.createJDBCConnection(
			 			 dsName, 
			 			jbcdDriver, 
			 			connectionURL,
			 			dbUserName,
			 			dbPassword);
				    	
			  }
		}
		catch (SQLException e)
		{
			throw new ConnectionException(e);
		}
  }// --------------------------------------------------------

  
  /**
   * 
   * @param driver the jdbcDriver
   * @param connectionURL the connection URL
   * @param user the user
   * @param password the password
   * @throws ConnectionException
   */
  protected DAO(String jdbcDriver, String connectionURL, String user, char[] password)
  throws ConnectionException
  {
     try
      
     {
	  the_connection = DAOFactory.createJDBCConnection(jdbcDriver, connectionURL, user, password);
      } 
     	catch (Exception e)
      {
     	   throw new ConnectionException(Debugger.stackTrace(e));
      }
  }// ----------------------------------------------
  /**
   * Initialize the statement
   * @param ps the parameter statement
   * @throws SQLException
   */
	public static void initStatement(Arrayable<Object> dataRow, PreparedStatement ps, int maxParameters)
	throws SQLException
	{
		Object[] inputs = dataRow.toArray();
		
		if(maxParameters < 1)
			maxParameters = inputs.length;
		
		Object obj = null;
		for (int i = 0; i < maxParameters; i++)
		{
			obj = inputs[i];
			try
			{
				if(obj != null)
					ps.setObject(i+1, obj);
				else
					ps.setNull(i+1,  java.sql.Types.NULL);
			}
			catch(SQLException e)
			{
				throw new SQLException("position:"+i,e);
			}
		}
		
		Debugger.println(dataRow,"initStatement maxParameters="+maxParameters+" "+dataRow);
	}// --------------------------------------------------------
  /**
   *  constructor for the the abstract the DAO
   * @throws ConnectionException if a system related Configuration Runtime Exception errors
   */
   protected DAO(Connection aConnection)
   throws ConnectionException   
   {
     the_connection = aConnection;
    
   }//-------------------------   
   /**
    *  constructor for the the abstract the DAO
    * @throws ConnectionException if a system related Configuration Runtime Exception errors
    */
    protected DAO(DataSource dataSource)
    throws ConnectionException, SQLException  
    {

      this(dataSource.getConnection());
    }//-------------------------   
  
  /**
   *  constructor for the the abstract the DAO
   * @throws ConnectionException if a system related Configuration Runtime Exception errors
   */
  public DAO(SecurityCredential aUser, Connection aConnection)
  throws ConnectionException   
  {
    user = aUser;
    the_connection = aConnection;
     
 }//-------------------------

  /**
   * Default constructor for the the abstract the DAO
   * @throws ConnectionException if a system related Configuration Runtime Exception errors
   */
   public DAO(SecurityCredential aUser)
   throws ConnectionException
   {

     this(aUser,
          DAOFactory.createConnection(aUser));
           
   }//--------------------------------------------
   /**
    * 
    * @param aResultSet the result sets
    * @return DAOFactory.toColumnNames(aResultSet)
    */
   protected Collection<String> toColumnNames(ResultSet aResultSet)
   throws SQLException
   {
      return DAOFactory.toColumnNames(aResultSet);
   }// --------------------------------------------

  /**
   * <pre>
   * This method is used by DAO to support database independents.
   * Each resource bundle may be taylored to a particular database.
   * For example there may be a bundle for Oracle and one for DB2.
   * 
   * This method will determine to configured database and return
   * the appropriate bundle.
   * 
   * So if the class is "some.thing.ADAO.class" and the database name is "oracle"
   * then the resource bundle is "ADAO-oracle.properties" in the "some.thing package".
   * </pre> 
   * @return ResourceBundle.getBundle(aClass.getName()+"-"+DAOFactory.getDatabaseName()
   *
   */
  protected ResourceBundle createBundle(Class<?> aClass)
  {
     return ResourceBundle.getBundle(aClass.getName()+"-"+DAOFactory.getDatabaseTypeName());
     
  }//------------------------------------------------
  /**
   * copy constructor
   * @param aDAO the DAO to copy 
   */
  public DAO(DAO aDAO)
  {
     //user = aDAO.user;
     this.connectionReference  = new WeakReference<Connection>(aDAO.the_connection);

     thisCreatedConnection = false;
  }//--------------------------------------------
  /**
   * Calls the dispose method when the object is garbage collected
   */
  public void finalize() throws Throwable
  {
      if(!disposed )
      {
        Debugger.printWarn("DAO has not been disposed. Possible connection leak. "
        +getClass().getName());  
      }
  }//----------------------------------------------
  /**
   * 
   * @param aSQL
   * @return
   * @throws SQLException
   */
  public PreparedStatement prepareStatement(String aSQL)
  throws SQLException
  {
     return prepareStatement(this.getConnection(), aSQL);
  }//-------------------------------------
  /**
   * 
   * @param aConnection the connection
   * @param aSQL the SQL 
   * @return the prepared statement
   * @throws SQLException
   */
  public PreparedStatement prepareStatement(Connection aConnection, String aSQL)
  throws SQLException
  {
     Debugger.println(this,"prepareStatement="+aSQL);
     return aConnection.prepareStatement(aSQL);
  }//-------------------------------------
  /**
   * 
   */
   public void commit()
   {
	try
	{
	   the_connection.commit();   
	} 
	catch (Exception e)
	{
	   throw new SystemException(Debugger.stackTrace(e));
	}	
   }// ----------------------------------------------
   /**
    * 
    */
    public void rollback()
    {
 	try
 	{
 	   the_connection.rollback();   
 	} 
 	catch (Exception e)
 	{
 	   throw new SystemException(Debugger.stackTrace(e));
 	}	
    }// ----------------------------------------------
  /**
   * This will close any remain database connections
   */
  public void dispose()
  {
	  Debugger.println(this,"DISPOSING START "+this.getClass().getName());
     
     try
     {
        
        if(the_connection != null && thisCreatedConnection )
        {

          the_connection.close();
          Debugger.println(this,"DISPOSING END CONNECTION CLOSED "+this.getClass().getName());
        }
        else
        {
        	Debugger.println(this,"DISPOSING END connection ="+this.the_connection+" "+this.getClass().getName());
        }
        
        //the_connection = null;
     }
     catch(Throwable e)
     {
        e.printStackTrace();
     }
     
     disposed = true;
  }//------------------------------------------------
  /**
   * @return the connection object
   */
  private Connection getConnection()
  {
     if(this.thisCreatedConnection)
         return the_connection;
     else
        return (Connection)this.connectionReference.get();

  }//------------------------------------------------------------
  
  
  /**
   * @param the Connection object
   */
  public void setConnection(Connection aConnection)
  {
     the_connection= aConnection;

  }//------------------------------------------------------------   
  /**
   * @param aData a Java util date object
   * @return an SQL date object
   */
  protected static java.sql.Date toDate(java.util.Date aDate)
  {
     if(aDate == null )
        return null;
     
     return new java.sql.Date(aDate.getTime());
  }//-----------------------------------------------
  /**
   * @param aData a Java util date object
   * @return an SQL time stampt object
   */   
  protected static Timestamp toTimestamp(java.util.Date aDate)
  {
     if(aDate == null)
        return null;
      
     return new Timestamp(aDate.getTime());
  }//-----------------------------------------------------
  /**
   * @param aData a Java util date object
   * @return an SQL time stampt object
   */   
  protected static Timestamp toTimestamp(java.util.Calendar aDate)
  {
     if(aDate == null)
        return null;
      
     return new Timestamp(aDate.getTime().getTime());
  }//-----------------------------------------------------
  protected static java.sql.Date toDate(java.util.Calendar aDate)
  {
     if(aDate == null)
        return null;
      
     return new java.sql.Date(aDate.getTime().getTime());
  }//-----------------------------------------------------  
  /**
   * @return the formatted date
   */
  protected static String formatDateToDDMonYYYY(java.util.Date date)
  {
     if( date == null)
        return "";
        
     StringBuffer buf = new SimpleDateFormat("dd-MMM-yyyy").format(date, new StringBuffer(), new FieldPosition(0));
     return buf.toString();
 }//---------------------------------------------------
  /**
   * @return the formatted date
   */
  protected static String formatDateToDDMonYYYY(java.sql.Date date){
     if( date == null)
        return "";
     StringBuffer buf = new SimpleDateFormat("dd-MMM-yyyy").format(date, new StringBuffer(), new FieldPosition(0));
     return buf.toString();
  }//------------------------------------------
  /**
   * @param aErrorID the error identification number
   * @param aErrorMsg the error message
   * @throws SystemException if results are validate
   */
  protected void checkResults(String aErrorID, String aErrorMsg)
  throws SystemException
  {
     if(!Data.isNull(aErrorID))
       throw new SystemException(aErrorID,aErrorMsg);

  }//------------------------------------------
  protected int getInt(ResultSet aResultSet, String aName)
  {
    try
     {
         return aResultSet.getInt(aName);
     }
     catch (SQLException e)
     {
        Debugger.printError(this,e);
        return 0;
     }
     
  }//-----------------------------------------
  protected String getString(ResultSet aResultSet, String aName )
  throws SQLException
  {
    try
     {
         return aResultSet.getString(aName);
     }
     catch (SQLException e)
     {
        throw new SQLException("Name="+aName+" "+Debugger.stackTrace(e));        
     }
     
  }//-----------------------------------------
  /**
   * 
   * @param aResultSet the result set
   * @param aName the name of the parameter
   * @return
   */
  protected Timestamp getTimestamp(ResultSet aResultSet, String aName )
  {
    try
     {
         return aResultSet.getTimestamp(aName);
     }
     catch (SQLException e)
     {
    	 Debugger.printError(e);
        return null;
     }
     
  }//-----------------------------------------
  /**
   * Geenrice method to select on integer based on any integer value
   * @param aSQL the query
   * @param aInput the input integer
   * @return interger result from the query
   * @throws NoDataFoundException when no results are found
   * @throws SQLException when an internal error occurs
   */
  protected Integer[] selectIntegers(String aSQL,  int aInput)
  throws NoDataFoundException, SQLException
  {
     Object[] inputs = {new Integer(aInput)};
     
     return selectIntegers(aSQL, inputs);
  }//------------------------------------------------------------
  /**
   * Geenrice method to select on integer based on any integer value
   * @param aSQL the query
   * @param aInput the input integer
   * @return interger result from the query
   * @throws NoDataFoundException when no results are found
   * @throws SQLException when an internal error occurs
   */
  protected Integer[] selectIntegers(String aSQL,  Object aInput)
  throws NoDataFoundException, SQLException
  {

        Object [] inputs = {aInput};
        return selectIntegers(aSQL,inputs );

  }//------------------------------------------------------------   
  protected Integer[] selectIntegers(String aSQL,  Object[] aInputs)
  throws NoDataFoundException, SQLException

  {
     if(aInputs == null )
        throw new IllegalArgumentException("aInputs not provided)");
              
     PreparedStatement stmt = null;
     ResultSet rs = null;
     try  
     {
       
    	 Debugger.println(this,aSQL);
        
        stmt =this.prepareStatement(aSQL);         
        
        initPreparedStatement(aInputs, stmt);
        
        rs = this.select(stmt);
        
        ArrayList<Integer> l = new ArrayList<Integer>
             ();
        while(rs.next()) 
        {
            l.add(new Integer(rs.getInt(1)));
        }
        
        l.trimToSize();
        if(l.isEmpty())
           throw new NoDataFoundException("inputs are "+Debugger.toString(aInputs));
        
        Integer [] ints = new Integer[l.size()];
        System.arraycopy(l.toArray(),0,ints,0,ints.length);
        
        return ints;
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------
  /**
   * Select string from a database
   * @param aSQL the SQL to execute
   * @param aInputs the prepared statement inputs
   * @return the list of string (first column)
   * @throws SQLException
   */
  protected String[] selectStrings(String aSQL,  Object[] aInputs)
  throws SQLException

  {
     if(aInputs == null )
        throw new IllegalArgumentException("aInputs not provided)");
              
     PreparedStatement stmt = null;
     ResultSet rs = null;
     try  
     {
       
        Debugger.println(this,aSQL);
        
        stmt =this.prepareStatement(aSQL);         
        
        initPreparedStatement(aInputs, stmt);
        
        rs = this.select(stmt);
        
        ArrayList<String> l = new ArrayList<String>
             (BATCH_SIZE);
        while(rs.next()) 
        {
            l.add(rs.getString(1));
        }
        
        l.trimToSize();
        if(l.isEmpty())
          return null;
        
        String [] ints = new String[l.size()];
        System.arraycopy(l.toArray(),0,ints,0,ints.length);
        
        return ints;
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------  
  /**
   * Select a single columns of longs
   * @param aSQL the SQL that specifies the column
   * @param aInput the input value
   * @return the array of longs
   * @throws NoDataFoundException
   * @throws SQLException
   */
  protected Long[] selectLongs(String aSQL, Object aInput)  
  throws NoDataFoundException, SQLException
  {
     Object[] inputs = { aInput };
     return selectLongs(aSQL, inputs);
  }// --------------------------------------------
  /**
   * Select an arry of longs from the database
   * @param aSQL the SQL
   * @param aInput the inputs to prepare
   * @return array of longs
   * @throws NoDataFoundException
   * @throws SQLException
   */
  protected Long[] selectLongs(String aSQL, Object[] aInputs)
  throws NoDataFoundException, SQLException
  {
     if (aSQL == null)
      throw new IllegalArgumentException("SQL required in DAO.selectLongs");
              
     PreparedStatement stmt = null;
     ResultSet rs = null;
     try  
     {
        Debugger.println(this,aSQL);
        
        stmt =this.prepareStatement(aSQL);
        
        this.initPreparedStatement(aInputs, stmt);        
        
        rs = this.select(stmt);
        
        ArrayList<Long> l = new ArrayList<Long>
             (BATCH_SIZE);
        
        while(rs.next()) 
        {
            l.add(new Long(rs.getLong(1)));
        }
        
        l.trimToSize();
        if(l.isEmpty())
           throw new NoDataFoundException(Debugger.toString(aSQL));
                
        return (Long[])l.toArray(new Long[l.size()]);
     }
     catch(SQLException e)
     {
        throw new SQLException(e+" SQL="+aSQL);
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------
  /**
   * 
   * @param aSQL the SQL statements
   * @param aInputs the SQL inputs
   * @return true if resultSet.next is true
   * @throws NoDataFoundException
   * @throws SQLException
   */
  protected boolean selectHasResults(String aSQL, Object[] aInputs)
  throws SQLException
  {
     if (aSQL == null)
      throw new IllegalArgumentException("SQL required in DAO.selectHasResults");
              
     PreparedStatement stmt = null;
     ResultSet rs = null;
     try  
     {
        Debugger.println(this,aSQL);
        
        stmt =this.prepareStatement(aSQL);
        
        this.initPreparedStatement(aInputs, stmt);        
        
        rs = this.select(stmt);
        
        return rs.next(); 
     }
     catch(SQLException e)
     {
        throw new SQLException(aSQL,e);
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------
  protected Integer[] selectIntegers(String aSQL)
  throws NoDataFoundException, SQLException

  {
     if (aSQL == null)
      throw new IllegalArgumentException("aSQL required in DAO.selectIntegers");
              
     Statement stmt = null;
     ResultSet rs = null;
     try  
     {
       
        Debugger.println(this,aSQL);
        
        stmt =this.createStatement();                 
        
        rs = stmt.executeQuery(aSQL);
        
        ArrayList<Integer> l = new ArrayList<Integer>
             (BATCH_SIZE);
        while(rs.next()) 
        {
            l.add(new Integer(rs.getInt(1)));
        }
        
        l.trimToSize();
        if(l.isEmpty())
           throw new NoDataFoundException(Debugger.toString(aSQL));
        
        Integer [] ints = new Integer[l.size()];
        System.arraycopy(l.toArray(),0,ints,0,ints.length);
        
        return ints;
     }
     catch(SQLException e)
     {
        throw new SQLException(e+" SQL="+aSQL);
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------
  /**
   * Select a single result using a result set object creator
   * @param aSQL the SQL statement
   * @param mapper the result mapper
   * @param results the results
   * @throws SQLException
   */
  protected <OutputType,CriteriaType> OutputType selectResult(String sql, Object[] inputs,
		  ResultSetObjectCreator<OutputType> mapper)
  throws SQLException
  {
	  ArrayList<OutputType> results = new ArrayList<OutputType>();
	  
	  this.selectResults(sql, inputs, mapper,results);
	  
	  if(results.isEmpty())
		  return null;
	  
	  return (OutputType)results.iterator().next();
  }// --------------------------------------------------------
  /**
   * Select results using a result set object creator
   * @param aSQL the SQL statement
   * @param mapper the result mapper
   * @param results the results
   * @throws SQLException
   */
  protected <OutputType,CriteriaType> void selectResults(String sql, Object[] inputs,
		  ResultSetObjectCreator<OutputType> mapper, Collection<OutputType> results)
  throws SQLException
  {
	  selectResults(sql, inputs, arrayPreparedStatementConstructor,mapper, results);
  }// --------------------------------------------------------

  
  /**
   * Select results using a result set object creator
   * @param aSQL the SQL statement
   * @param mapper the result mapper
   * @param results the results
   * @throws SQLException
   */
  protected <OutputType,CriteriaType> void selectResults(String sql, CriteriaType criteria,
		  PreparedStatementConstructor<CriteriaType> setter, 
		  ResultSetObjectCreator<OutputType> mapper, Collection<OutputType> results)
  throws SQLException
  {
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  
	  try
	  {
		ps = this.prepareStatement(sql);
		  
		  setter.constructPreparedStatement(this.the_connection, ps, criteria);
		  
		  rs = ps.executeQuery();
		  
		  int index =0;
		  
		  while(rs.next()) 
		  {
		  	results.add(mapper.create(rs, index++));
		  }
	}
	catch(SQLException e)
	{
		throw new SQLException(sql,e);
	}
	finally
	{
        if(rs != null)
	           try{ rs.close();}catch(Exception err){}
        
	    if(ps != null)  
	           try{ ps.close();}catch(Exception err){} 
	}
	  
  }// --------------------------------------------------------
  
  /**
   * Select results using the paging object
   * @param sql the SQL to execute
   * @param criteria the Input criteria
   * @param setter 
   * @param mapper
   * @param paging
   * @throws SQLException
   */
protected <OutputType,CriteriaType> Pagination selectPaging(String sql, CriteriaType criteria,
		  PreparedStatementConstructor<CriteriaType> setter, 
		  ResultSetObjectCreator<OutputType> mapper, Paging<OutputType> paging)
  throws SQLException
  {
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  
	  PageCriteria pageCriteria = paging.getPageCriteria();
	  
	  boolean storePagination = false;
	  int pageSize = Integer.MAX_VALUE;
	  int beginIndex = 0;
	  
	  
	 if(pageCriteria != null)
	  {
		  storePagination = pageCriteria.isSavePagination();
		  pageSize = pageCriteria.getSize();
		  
		  //Test for no paging
		  if(pageSize <= 0)
			  pageSize = Integer.MAX_VALUE; //TODO: you may want to revisit this
		  
		  beginIndex = pageCriteria.getBeginIndex();
	  }
	  
	  try
	  {
		  ps = this.prepareStatement(sql);
		  
		  setter.constructPreparedStatement(this.the_connection, ps, criteria);
		  
		  rs = ps.executeQuery();
		  
		 int index =1;
		  Pagination  pagination = DAOFactory.createPagination(pageCriteria);
		  
		  
		  boolean isLast = true;
		  OutputType output = null;
		  
		  
		  Iterator<ResultSet> iterator = new ResultSetIterator(rs);
		  
		 
		  
		  while (iterator.hasNext())
		  {
			  
			  if(index > pageSize)
			  {
				  isLast = false;
				  
				  if(storePagination)
				  {
					  output = mapper.create(rs, index);
					  
					  if(output != null)
					  {
						  //save in pagination
						  pagination.store(output, pageCriteria);
					  }
						
					  //construct pagination in the background
					 this.thisCreatedConnection = false;  //pagination will close the connection
						 
					 pagination.constructPaging(iterator, pageCriteria, mapper);
				  }
				 
				  break; //loop thru
			  }
			  
			 if(pageSize > 0 && paging.size() < pageSize)
			  {
				 output = mapper.create(rs, index);
				 
				 if(storePagination && output != null)
				 {
					 //save in pagination
					 pagination.store(output, pageCriteria);
				 }
				
				 if(output!= null)
					 paging.add(output);
			  }

			 if(output != null)
				 index++;
		  }
		  
		  
		  //set first
		  paging.setFirst( beginIndex < pageSize);
		  //set last
		  paging.setLast(isLast);
		  
		  //starting additional paging in background
		  
		  return pagination;
		   
	}
	catch(SQLException e)
	{
		throw new SQLException(sql+" "+e.getMessage(),e);
	}
	finally
	{
        if(!storePagination && rs != null)
	           try{ rs.close();}catch(Exception err){}
      
        if(!storePagination && ps != null)  
	           try{ ps.close();}catch(Exception err){} 
	}
	  
  }// --------------------------------------------------------

  static class ArrayPreparedStatementConstructor implements PreparedStatementConstructor<Object[]>
  {

	public void constructPreparedStatement(Connection connection,
			PreparedStatement preparedStatement, Object[] inputs
			)
			throws SQLException
	{
		for (int i = 0; i < inputs.length; i++)
		{
			preparedStatement.setObject(i +1, inputs[i]);
		}
	}
  }
 
  /**
   * Select results using a result set object creator
   * @param sql the SQL statement
   * @return true if result set has a single record
   * @throws SQLException
   */
  protected <CriteriaType> boolean selectHasResults(String sql, CriteriaType criteria,
		  PreparedStatementConstructor<CriteriaType> setter)
  throws SQLException
  {
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  
	  try
	  {
		ps = this.prepareStatement(sql);
		  
		  setter.constructPreparedStatement(this.the_connection, ps, criteria);
		  
		  rs = ps.executeQuery();
		  
		 return rs.next();

	}
	catch(SQLException e)
	{
		throw new SQLException(sql,e);
	}
	finally
	{
        if(rs != null)
	           try{ rs.close();}catch(Exception err){}
        
	    if(ps != null)  
	           try{ ps.close();}catch(Exception err){} 
	}
	  
  }// --------------------------------------------------------
  /**
   * Select results using a result set object creator
   * @param aSQL the SQL statement
   * @param mapper the result mapper
   * @param results the results
   * @throws SQLException
   */
  protected <CriteriaType> int executeUpdate(String sql, CriteriaType criteria,
		  PreparedStatementConstructor<CriteriaType> setter)
  throws SQLException
  {
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  
	  try
	  {
		ps = this.prepareStatement(sql);
		  
		  setter.constructPreparedStatement(this.the_connection, ps, criteria);
		  
		  return  ps.executeUpdate();
		 
	}
	catch(SQLException e)
	{
		throw new SQLException(sql,e);
	}
	finally
	{
        if(rs != null)
	           try{ rs.close();}catch(Exception err){}
        
	    if(ps != null)  
	           try{ ps.close();}catch(Exception err){} 
	}
	  
  }// --------------------------------------------------------
  /**
   * Select results using a result set object creator
   * @param aSQL the SQL statement
   * @param mapper the result mapper
   * @param results the results
   * @throws SQLException
   */
  protected <T> void selectResults(String aSQL, ResultSetObjectCreator<T> mapper, Collection<T> results)
  throws SQLException
   {
		     if (aSQL == null)
		      throw new IllegalArgumentException("aSQL required in DAO.selectObject");
		              
		     Statement stmt = null;
		     ResultSet rs = null;
		     try  
		     {
		       
		        Debugger.println(this,aSQL);
		        
		        stmt =this.createStatement();                 
		        
		        rs = stmt.executeQuery(aSQL);
		  
		        int index =0;
		        
		        while(rs.next()) 
		        {
		        	results.add(mapper.create(rs, index++));
		        }
		      }
		     catch(SQLException e)
		     {
		        throw new SQLException(" SQL="+aSQL,e);
		     }
		     finally
		     { 
		        if(rs != null)
		           try{ rs.close();}catch(Exception err){} 
		        if(stmt != null)  
		           try{ stmt.close();}catch(Exception err){} 
		     }       
		     
		  }//-------------------------------------------------
  
    /**
     * 
     * @param aSQL
     * @return the first result
     * @throws SQLException
     */
    protected String selectString(String aSQL)
    throws SQLException
	{
    	String[] results = selectStrings(aSQL);
    	
    	if(results == null || results.length == 0)
    		return null;
    	
    	return results[0];
	}// --------------------------------------------------------
  protected String[] selectStrings(String aSQL)
  throws SQLException

  {
     if (aSQL == null)
      throw new IllegalArgumentException("aSQL required in DAO.selectIntegers");
              
     Statement stmt = null;
     ResultSet rs = null;
     try  
     {
       
        Debugger.println(this,aSQL);
        
        stmt =this.createStatement();                 
        
        rs = stmt.executeQuery(aSQL);
        
        ArrayList<String> l = new ArrayList<String>
             (BATCH_SIZE);
        while(rs.next()) 
        {
            l.add(rs.getString(1));
        }
        
        l.trimToSize();
        if(l.isEmpty())
           return null;
        
        String [] ints = new String[l.size()];
        System.arraycopy(l.toArray(),0,ints,0,ints.length);
        
        return ints;
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------  
  /**
   * Determine is select has results
   * @param ps the prepared statement
   * @return true result set has results
   * @throws SQLException
   */
  protected boolean selectHasResults(PreparedStatement ps)
  throws SQLException
  {
	  ResultSet rs = null;
	  
	  try
	  {
		 rs = ps.executeQuery(); 
		 
		 return rs.next();
	  }
	  finally
	  {
		  if(rs != null)
			  try{ rs.close(); } catch(Exception e){}
	  }
  }// --------------------------------------------------------
  
  protected Long[] selectLongs(String aSQL)
  throws NoDataFoundException, SQLException
 {
    if (aSQL == null)
     throw new IllegalArgumentException("aSQL required in DAO.selectLongs");
             
    Statement stmt = null;
    ResultSet rs = null;
    try  
    {
      
       Debugger.println(this,aSQL);
       
       stmt =this.createStatement();                 
       
       rs = stmt.executeQuery(aSQL);
       
       ArrayList<Long> l = new ArrayList<Long>
            (BATCH_SIZE);
       while(rs.next()) 
       {
           l.add(new Long(rs.getInt(1)));
       }
       
       l.trimToSize();
       if(l.isEmpty())
          throw new NoDataFoundException(Debugger.toString(aSQL));
       
       Long [] ints = new Long[l.size()];
       System.arraycopy(l.toArray(),0,ints,0,ints.length);
       
       return ints;
    }
    catch(SQLException e)
    {
       throw new SQLException(e+" SQL="+aSQL);
    }
    finally
    { 
       if(rs != null)
          try{ rs.close();}catch(Exception err){} 
       if(stmt != null)  
          try{ stmt.close();}catch(Exception err){} 
    }       
    
 }//-------------------------------------------------
  /**
   * 
   * @param rs the results set
   * @return
   */
  public static DataRow toDataRow(ResultSet rs)
  throws SQLException
  {
	  return new ResultSetDataRow(rs);
  }// --------------------------------------------------------
  /**
   * Select data and return a in memory results of the results
   * @param sql the query to execute
   * @return in memory results 
   * @throws NoDataFoundException
   * @throws SQLException
   */
  protected DataResultSet selectDataResultSet(String sql)
  throws NoDataFoundException, SQLException

  {
     if (sql == null)
      throw new IllegalArgumentException("aSQL required in DAO.selectIntegers");
              
     Statement stmt = null;
     ResultSet rs = null;
     try  
     {
       
        Debugger.println(this,sql);
        
        stmt =this.createStatement();                 
        
        rs = stmt.executeQuery(sql);
        
        DataResultSet dataResultSet = DAOFactory.toDataResultSet(rs);
                
        if(dataResultSet.isEmpty())
           throw new NoDataFoundException(Debugger.toString(sql));
              
        
        return dataResultSet;
     }
     catch(SQLException e)
     {
	  throw new SQLException(sql+e.getMessage()+" code="+e.getErrorCode());
	  
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     }       
     
  }//-------------------------------------------------  
  
  /**
   * Initial prepared statement from an array of inputs
   * @param aInputs the input argumnets
   * @param stmt the prepared statement
   * @throws SQLException when an internal error occurs
   */
  protected void initPreparedStatement(Object[] aInputs, PreparedStatement stmt) 
  throws SQLException
  {
     Object input= null;
     
     int paramPos = 1;
     
     byte [] b = null;
     for(int i = 0;i < aInputs.length;i++)
     {
        input = aInputs[i];
        
        
        if(input instanceof byte[] )
        {    
          b = (byte[])input;  
           if(b.length == 0)
           {
              stmt.setNull(paramPos, Types.BINARY);  
           }
           else
           {
                        
             stmt.setBytes(paramPos,b);
           }
          
        }
        else if(input instanceof Integer)
        {

              this.setInt(paramPos,((Integer)input).intValue(),stmt);
        } 
        else if (input instanceof java.sql.Timestamp) 
        {

              stmt.setTimestamp(paramPos, (java.sql.Timestamp) input);
        }        
        else if (input instanceof java.sql.Date) 
        {

              stmt.setDate(paramPos, (java.sql.Date) input);
        }
        else if (input instanceof java.util.Date) 
        {
              stmt.setDate(paramPos, toDate((java.util.Date)input));
        }        
        else if (input instanceof java.util.Calendar) 
        {

              stmt.setTimestamp(paramPos,toTimestamp((Calendar)input));
        }      
        else if(input instanceof Long)
        {
           
           setLong(paramPos, (Long)input, stmt);
        }
        else
        {
           if(input ==null)
           {
              stmt.setNull(paramPos, Types.VARCHAR);
           }
           else
           {
              this.setString(paramPos,input.toString(),stmt);  
           }
        }
        
        paramPos++;
     }
  }//---------------------------------------------------------------------
  /**
   * @param aParamPosition
   * @param aInput
   * @param aPreparedStatement
   * @throws SQLException
   */
  private void setLong(int aParamPosition, Long aInput, PreparedStatement aPreparedStatement) throws SQLException
  {
     if(aInput ==null || aInput.longValue() < 0)
     {
        aPreparedStatement.setNull(aParamPosition, Types.BIGINT);  
     }
     else
     {
         aPreparedStatement.setLong(aParamPosition,((Long)aInput).longValue());
     }
  }//-----------------------------------------------------------------
  protected java.sql.Date getDate(ResultSet aResultSet, String aName )
  {
    try
     {
         return aResultSet.getDate(aName);
     }
     catch (SQLException e)
     {
        Debugger.printError(e);
        return null;
     }
  }//-----------------------------------------
  protected java.util.Calendar getCalendar(ResultSet aResultSet, int aIndex )
  {
    try
     {
       java.util.Date date = aResultSet.getTimestamp(aIndex);
       if(date ==null)
          return null;
       
       Calendar calendar = Calendar.getInstance();

       calendar.setTime(date);
       return calendar;
     }
     catch (SQLException e)
     {
    	 Debugger.printError(e);
        return null;
     }
  }//-----------------------------------------
  /**
   * 
   * @return Statement
   * @throws SQLException
   */
  protected Statement createStatement()
  throws SQLException
  {
     return getConnection().createStatement();
     
  }//---------------------------------------------------
  /**
   * Execute an update statement
   * @param sql the SQL
   * @param input a single input
   * @return the number of records updated
   * @throws SQLException
   */
	public int executeUpdate(String sql, String input) throws SQLException
	{
		Object[] inputs = {input};
		return executeUpdate(sql,inputs);
	}// --------------------------------------------------------
		
	/**
		   * Execute an update statement
		   * @param sql the SQL
		   * @param input a single input
		   * @return the number of records updated
		   * @throws SQLException
	*/
	public int executeUpdate(String sql, Object[] inputs) throws SQLException
	{
		return executeUpdate(sql, inputs, 
			new PreparedStatementConstructor<Object[]>()
			{
				public void constructPreparedStatement(Connection connection,
						PreparedStatement preparedStatement, Object[] inputs)
						throws SQLException
				{
					for (int i = 0; i < inputs.length; i++)
					{
						preparedStatement.setObject(i+1, inputs[i]);
					}
				}
			});
	}// --------------------------------------------------------
  /**
   * Execute a delete statement
   * @param aSQL the delete
   * @throws NoDataFoundException
   * @throws SQLException
   */
  protected void delete(String aSQL)
  throws NoDataFoundException, SQLException
  {
     Statement sqlStmt = null;
     try
     {
        sqlStmt = createStatement();
        
        if( sqlStmt.executeUpdate(aSQL) < 1)
        {
           throw new NoDataFoundException(aSQL);
        }
     }
     finally
     {
        if(sqlStmt != null)
        {
           try{ sqlStmt.close(); } catch(Exception e) {}
        }
     }
  }//--------------------------------------------------
  /**
   * Executive a callable statement that selects data from the database
   * @param aStatement the callable statement 
   */
  protected ResultSet select(PreparedStatement aStatement)
  throws SQLException
  {         
        return aStatement.executeQuery();
  }//------------------------------------------
  /**
   * Executive a callable statement that insert data into the database
   * @param aStatement the callable statement 
  */
  protected boolean insert(PreparedStatement aStatement)
  throws DuplicateRowException, SQLException
  {  
     try
     {
        return aStatement.execute();
     }
     catch(SQLException e)
     {
        if(e.getMessage().indexOf("constraint")  > -1)
           throw new DuplicateRowException(Debugger.stackTrace(e));

         throw e;
     }
  }//------------------------------------------
 /**
  * Executive a callable statement that update data into the database
  * @param aStatement the callable statement 
  */
  protected int update(PreparedStatement aStatement)
  throws SystemException, NoDataFoundException
  {  
     try
     {
        int cnt = aStatement.executeUpdate();
        
        if(cnt == 0 )
           throw new NoDataFoundException();
           
        return cnt;
     }
     catch(SQLException e)
     {
        if(e.toString().indexOf("too large") > -1)
        {
           throw new SizeViolationException(Debugger.stackTrace(e));
        }
        else if(e.toString().indexOf("integrity") > -1)
           throw new IntegrityConstraintException(Debugger.stackTrace(e));
        else if(e.toString().indexOf("unique") > -1)
           throw new DuplicateRowException(Debugger.stackTrace(e));
        else
        {
           throw new SystemException(Debugger.stackTrace(e));
        }
        
     }
  }//------------------------------------------
  /**
   * Executive a callable statement that update data into the database
   * @param aStatement the callable statement 
   * @throws NoDataFoundException if no data deleted
   */
   protected int delete(PreparedStatement aStatement)
   throws SQLException
   {
         int cnt = aStatement.executeUpdate();     
         
         return cnt;
   }//------------------------------------------   
  /**
   * @return an instance of a CallableStatement
   * @param aSQL the callable SQL statement string
   */
  protected CallableStatement prepareCall(String aSQL)
  throws SQLException
  { 
       Debugger.println(this,"execting sql="+aSQL);

       return the_connection.prepareCall(aSQL);
  }//---------------------------------------------------------
  /**
   * 
   * @param aString
   * @return true if string = (Y or T) else false
   */
  protected boolean toBoolean(String aString)
  {      
     if(aString == null)
        return false;
        
     aString = aString.trim().toUpperCase();
     if("Y".equals(aString) || "T".equals(aString))
        return true;
     else return false;
     
  }//---------------------------------------------------------
  /**
   * 
   * @param aCount the reset set count
   * @param aRangeCriteria the range criteria
   * @return true if Count >= aRangeCriteria.first() && aCount < aRangeCriteria.last
   */
  protected boolean matchRange(int aCount, RangeCriteria aRangeCriteria)
  {
     //Debugger.println(this,"count="+aCount+" first="+aRangeCriteria.first()+" last="+ aRangeCriteria.last());
     return aCount >= aRangeCriteria.first() && aCount <= aRangeCriteria.last();
     
  }//----------------------------------------------------------
  /**
   * Set null to NULL if equal to -1  
   * @param aPosition the position
   * @param aInt the integer
   * @param stmt the statement
   * @throws SQLException
   */
  protected void setInt(int aPosition, int aInt, PreparedStatement stmt)
        throws SQLException
  {
        if(aInt == NULL_INT)
        {
           stmt.setNull(aPosition, Types.INTEGER);
        }
        else      
           stmt.setInt(aPosition,aInt);
  }//------------------------------------------------------
  /**
   * Set null to NULL if empty  
   * @param aPosition the position
   * @param aInt the integer
   * @param stmt the statement
   * @throws SQLException
   */
  protected void setString(int aPosition, String text, PreparedStatement stmt)
        throws SQLException
  {
        if(Data.isNull(text))
        {
           stmt.setNull(aPosition, Types.VARCHAR);
        }
        /*else if(text.length() > maxSetStringLength)
        {
           stmt.setMaxFieldSize(text.length()+2);
           
           stmt.setCharacterStream(aPosition, new StringReader(text), text.length()+2);
        }*/
        else      
           stmt.setString(aPosition,text);
  }//------------------------------------------------------   
  /**
   * Set value to null, T or F
   * @param aPosition the position
   * @param aBoolean the boolean value
   * @param stmt the statement
   * @throws SQLException
   */
  protected void setTrueFalse(int aPosition, Boolean aBoolean, PreparedStatement stmt)
  throws SQLException
  {
     if(aBoolean == null )
     {
        stmt.setNull(aPosition, Types.VARCHAR);
     }
     else
     {
        if(aBoolean.booleanValue())
        {
           //TRUE
           stmt.setString(aPosition,TRUE);
        }
        else
        stmt.setString(aPosition,FALSE);
        
     }
  }//-----------------------------------------------------
  /**
      * Set value to null, Y or N
      * @param aPosition the position
      * @param aBoolean the boolean value
      * @param stmt the statement
      * @throws SQLException
      */
     protected void setYesNo(int aPosition, boolean aBoolean, PreparedStatement stmt)
     throws SQLException
     {
        setYesNo(aPosition, new Boolean(aBoolean),stmt);
     }//--------------------------------------------
  
  /**
      * Set value to null, Y or N
      * @param aPosition the position
      * @param aBoolean the boolean value
      * @param stmt the statement
      * @throws SQLException
      */
     protected void setYesNo(int aPosition, Boolean aBoolean, PreparedStatement stmt)
     throws SQLException
     {
        if(aBoolean == null )
        {
           stmt.setNull(aPosition, Types.VARCHAR);
        }
        else
        {
           if(aBoolean.booleanValue())
           {
              //TRUE
              stmt.setString(aPosition,YES);
           }
           else
           stmt.setString(aPosition,NO);
        
        }
     
     }//-----------------------------------------------------  
         /**
         * Set value to null or value integer
         * @param aPosition the position
         * @param aBoolean the boolean value
         * @param stmt the statement
         * @throws SQLException
         */
        protected void setCriteria(int aPosition, Criteria aCriteria, PreparedStatement stmt)
        throws SQLException
        {
           if(aCriteria == null || aCriteria.getPrimaryKey() < 1 )
           {
              stmt.setNull(aPosition, Types.INTEGER);
           }
           else
           {
              setInt(aPosition,aCriteria.getPrimaryKey(), stmt);         
           }
     
        }//-----------------------------------------------------        
  
  /**
   * 
   * @return the sequence next val
   * @throws NoDataFoundException
   * @throws SQLException
   */
  protected int nextVal(String aSQL)
  throws SQLException
  {
     String sql = aSQL;
     //Debugger.println(this,sql);
     ResultSet rs = null;
     Statement sqlStmt = null;
     try
     {   
        //Debugger.println(this,"sql="+sql);

        sqlStmt = createStatement();
        
        rs = sqlStmt.executeQuery(sql);
        
        if (!rs.next())
            throw new SQLException("SEQUENCE not found sql="+sql);
                 
         
        return rs.getInt(1);         

      }
      finally
      {
         if(sqlStmt != null)
         {
            try{ sqlStmt.close(); } catch(Exception e) {}
         }
         
          if (rs != null) 
           try{rs.close();}catch (Exception err){}
      }                          
  }//-----------------------------------------------------
  /**
   * 
   * @param aResourceID the resource PK
   * @return list of property for the resource
   * @throws SQLException when an internal error occurs
   * @throws NoDataFoundException when no found is 
   */
   protected Property []  selectProperties(int aID, String sql)
   throws SQLException, NoDataFoundException, SystemException 
  {
     PreparedStatement stmt = null;
     ResultSet rs = null;
     
     if(sql == null ||  sql.length() == 0)
        throw new SystemException("SQL not provided");
     
     try  
     {
        Debugger.println(this,sql);
        
        stmt =this.prepareStatement(sql);         
        
        this.setInt(1,aID,stmt);
        rs = this.select(stmt);
        
        ArrayList<Property> l = new ArrayList<Property>();
        Property property = null;
        while(rs.next()) 
        {
           property = createProperty(rs);
           
           l.add(property);
        }
        
        if(l.isEmpty())
           throw new NoDataFoundException("Properties for ID="+aID+" sql="+sql);
        
        Property[] properties = new Property[l.size()];
        
        System.arraycopy(l.toArray(),0,properties,0,properties.length);
        
        return properties;
       
     }
     finally
     { 
        if(rs != null)
           try{ rs.close();}catch(Exception err){} 
        if(stmt != null)  
           try{ stmt.close();}catch(Exception err){} 
     } 
  }//--------------------------------------------------   
   /**
    * 
    * @param aResultSet (Position1=name, Position2=value)
    * @return the property
    * @throws SQLException
    */
   protected Property createProperty(ResultSet aResultSet)
   throws SQLException
   {
      Property p = new Property();
      p.setName(aResultSet.getString(1));
      p.setValue(aResultSet.getString(2));

     return p;       
   }//-----------------------------------------------

   /**
    * 
    * @param aInputs the input arguments
    * @param aSQL the prepared SQL statement
    * @throws SQLException when an internal error occurs
    * @throws NoDataFoundException when rows updated
    */
   protected boolean insert(Object [] aInputs, String aSQL)
   throws SQLException, DuplicateRowException
   {
      PreparedStatement stmt = null;       
      
      if(aSQL == null ||  aSQL.length() == 0)
         throw new SystemException("SQL not provided");
      
      try  
      {
         //Debugger.println(this,aSQL);
         
         stmt =this.prepareStatement(aSQL);         
         
         this.initPreparedStatement(aInputs,stmt);
         
         return this.insert(stmt);         
      }
      catch(SQLException e)
      {
         throw new SQLException("SQL="+aSQL+" Inputs ="+Debugger.toString(aInputs)+" \n",e);
      }
      catch(DuplicateRowException e)
      {
         throw new DuplicateRowException("SQL="+aSQL+" Inputs ="+Debugger.toString(aInputs)+" \n",e);
      }
      finally
      { 
         if(stmt != null)  
            try{ stmt.close();}catch(Exception err){} 
      } 
   }//----------------------------------------------     
   /**
    * 
    * @param aSQL the SQL statement
    * @throws SQLException when an internal error occurs
    * @throws NoDataFoundException when rows updated
    */
   protected int insert(String aSQL)
   throws SQLException, DuplicateRowException
   {
      Statement stmt = null;       
      
      if(aSQL == null ||  aSQL.length() == 0)
         throw new SystemException("SQL not provided");
      
      try  
      {
         //Debugger.println(this,aSQL);
         
         stmt =this.createStatement();     
         
         return stmt.executeUpdate(aSQL);  
      }
      catch(SQLException e)
      {
         
    	  if(e.getMessage().indexOf("constraint")  > -1)
              throw new DuplicateRowException(Debugger.stackTrace(e));
    	  
         throw new SQLException("SQL="+aSQL+" error="+Debugger.stackTrace(e));
      }
      finally
      { 
         if(stmt != null)  
            try{ stmt.close();}catch(Exception err){} 
      } 
   }//----------------------------------------------       

   /**
    * 
    * @param aInputs the input arguments
    * @param aSQL the prepared SQL statement
    * @return number of row updated
    * @throws SQLException when an internal error occurs
    * @throws NoDataFoundException when rows updated
    */
   protected int update(Object [] aInputs, String aSQL)
   throws SQLException, NoDataFoundException
   {
      PreparedStatement stmt = null;       
      
      if(aSQL == null ||  aSQL.length() == 0)
         throw new SystemException("SQL not provided");
      
      try  
      {
         //Debugger.println(this,aSQL);
         
         stmt =this.prepareStatement(aSQL);         
         
         this.initPreparedStatement(aInputs,stmt);
         
         return this.update(stmt);         
      }
      catch(SQLException e)
      {
         if(e.toString().indexOf("too large") > -1)
         {
            throw new SizeViolationException(Debugger.toString(aInputs)+" "+Debugger.stackTrace(e));
         }
         else if(e.toString().indexOf("integrity") > -1)
            throw new IntegrityConstraintException(Debugger.stackTrace(e));
         else if(e.toString().indexOf("unique") > -1)
            throw new DuplicateRowException(Debugger.stackTrace(e));
         else
         {
            throw new SystemException(Debugger.stackTrace(e));
         }
         
      }
      finally
      { 
         if(stmt != null)  
            try{ stmt.close();}catch(Exception err){} 
      } 
   }//----------------------------------------------    
   protected int update(Connection aConnection, Object [] aInputs, String aSQL)
   throws NoDataFoundException, SQLException
   {
      PreparedStatement stmt = null;       
      
      if(aSQL == null ||  aSQL.length() == 0)
         throw new SystemException("SQL not provided");
      
      try  
      {
         //Debugger.println(this,aSQL);
         
         stmt =this.prepareStatement(aConnection, aSQL);         
         
         this.initPreparedStatement(aInputs,stmt);
         
         return this.update(stmt);         
      }
      catch(SizeViolationException e)
      {
         e.setInputs(aInputs);
         throw e;
      }
      finally
      { 
         if(stmt != null)  
            try{ stmt.close();}catch(Exception err){} 
      } 
   }//----------------------------------------------    

   /**
    * 
    * @param aInputs the input arguments
    * @param aSQL the prepared SQL statement
    * @return number of row deleted
    * @throws SQLException when an internal error occurs
    * @throws NoDataFoundException when rows deleted
    */
   protected int delete(Object [] aInputs, String aSQL)
   throws SQLException
   {
      PreparedStatement stmt = null;       
      
      if(aSQL == null ||  aSQL.length() == 0)
         throw new SystemException("SQL not provided");
      
      try  
      {
         Debugger.println(this,aSQL);
         
         stmt =this.prepareStatement(aSQL);         
         
         this.initPreparedStatement(aInputs,stmt);
         
         return this.delete(stmt);         
      }
      finally
      { 
         if(stmt != null)  
            try{ stmt.close();}catch(Exception err){} 
      } 
   }//----------------------------------------------
   /**
    * 
    * @return this.the_connection == null || this.the_connection.isClosed()
    */
   public boolean isClosed()
   {
      try
      {
         return this.the_connection == null || this.the_connection.isClosed();
      }
      catch(SQLException e)
      {
         return true;
      }
   }//--------------------------------------------
   /**
    * Copy blob binary content
    * @param source
    * @param destination
    * @throws SQLException
    */
   public static void copy(Blob source, Blob destination)
   throws SQLException
   {
	   
	  InputStream input = source.getBinaryStream();
	  OutputStream output = destination.setBinaryStream(0);
	  try
	  {
		  IO.write(output, input);
	  }
	  catch(IOException e)
	  {
		  throw new SQLException(e);
	  }
	  finally
	  {
		  if(input != null)
			  try{ input.close(); } catch(Exception e){}
		  
		  if(output != null)
			  try{ output.close(); } catch(Exception e){}
	  }
	  
   }// --------------------------------------------------------
   /**
    * Copy blob binary content
    * @param source
    * @param destination
    * @throws SQLException
    */
   public static String toString(Clob clob)
   throws SQLException, IOException
   {
	   return IO.readText(clob.getAsciiStream(),true);
   }// --------------------------------------------------------
   /**
    * Copy blob binary content
    * @param source
    * @param destination
    * @throws SQLException
    */
   public static void copy(Clob source, Clob destination)
   throws SQLException
   {
	   
	  Reader input = source.getCharacterStream();
	  Writer output = destination.setCharacterStream(0);
	  try
	  {
		  IO.write(output, input);
	  }
	  catch(IOException e)
	  {
		  throw new SQLException(e);
	  }
	  finally
	  {
		  if(input != null)
			  try{ input.close(); } catch(Exception e){}
		  
		  if(output != null)
			  try{ output.close(); } catch(Exception e){}
	  }
	  
   }// --------------------------------------------------------   
   /**
    * Populate the Java bean with values from result set
    * @param aObject the object to populate
    * @param aResultSet the result set
    */
  public static void populateObject(Object aObject, ResultSet aResultSet)
  throws SQLException
  {
     ResultSetMetaData meta = aResultSet.getMetaData();
     int columnCount = meta.getColumnCount();
     
     String propName = null;
     Object object = null;
     for (int i = 0; i < columnCount; i++)
     {
        propName = meta.getColumnName(i+1);        
        
        object = aResultSet.getObject(propName);
        
        try
        {

           if (object != null && object instanceof BigDecimal)
           {
              JavaBean.setProperty(aObject,propName, new Long(((BigDecimal)object).longValue()));              
           }
           else
           {             
              //loop thru columns        
              JavaBean.setProperty(aObject,propName, object);              
           }
        }
        catch(Exception e)
        {
           throw new SystemException(Debugger.stackTrace(e));
        }
     }
     
  }// --------------------------------------------
   /**
    * @return the disposed
    */
   protected final boolean isDisposed()
   {
      return disposed;
   }// --------------------------------------------

   /**
    * @param disposed the disposed to set
    */
   protected final void setDisposed(boolean disposed)
   {   
      this.disposed = disposed;
   }// --------------------------------------------

   /**
    * Format the object value based of the type.
    * Note that most type will be surrounded by a quote, while numbers to will not.
    * @param aObject the object value to format
    * @return the formatted value typically used in a where cause
    */
   public static String formatValue(Object aObject)
   {
      if(aObject == null)
         return String.valueOf(aObject);
      
      if(aObject instanceof Number)
      {
         return aObject.toString();
      }
      else
      {
         //encode quote
         String value = aObject.toString().replace("'", "''");  
         return new StringBuilder("'").append(value).append("'").toString();
      }
   }// --------------------------------------------
   /**
    * @param map the raw inputs that must be formatted
    *  @param sql the SQL to format
    * @return the formatted SQL with formatted values from a given map
    */
   public static String formatSQL(String sql,Map<?, ?> map)
   throws FormatException
   {
      if(map == null)
         return sql;
      
      Map<Object,Object> formattedMap = new HashMap<Object,Object>();
     //Format each value
      Set<?> keys = map.keySet();
      
      for (Object key : keys)
	  {
    	  formattedMap.put(key, formatValue(map.get(key)));
	  }
      
      return Text.format(sql, formattedMap);
   }// --------------------------------------------
   /**
    * 
    * @param objArray the object array
    * @return
    * @throws SQLException
    */
   protected Array toArray(String[] objArray)
   throws SQLException
   {
	   return this.the_connection.createArrayOf(STRING_TYPENAME, objArray);
   }// --------------------------------------------------------
   
   /**
    * Build an IN where cause
    * @param identifers the identifier to build a in clause
    * @return in (identifiers[0], identifiers[1] ... )
    */
   public String toInSqlCause(Identifier[] identifiers)
   {
	   StringBuilder sqlBuilder = new StringBuilder(" in (");
	   boolean firstTime = true;
	   
	   for (int i = 0; i < identifiers.length; i++)
		{
			
		   if(!firstTime )
			   sqlBuilder.append(",");
			
		   sqlBuilder.append(formatValue(identifiers[i].getId()));
		   
		   firstTime = false;
		}
	   
	   return sqlBuilder.append(")").toString();
   }// --------------------------------------------------------
   /**
    * Build an IN where cause
    * @param identifers the identifier to build a in clause
    * @return in (identifiers[0], identifiers[1] ... )
    */
   public String toInSqlCause(String[] identifiers, boolean includeInWithParenthesis)
   {
	   StringBuilder sqlBuilder = new StringBuilder();
	   
      if(includeInWithParenthesis)
    	  sqlBuilder.append(" in (");
      
	   boolean firstTime = true;
	   
	   for (int i = 0; i < identifiers.length; i++)
		{
			
		   if(!firstTime )
			   sqlBuilder.append(",");
			
		   sqlBuilder.append(formatValue(identifiers[i]));
		   
		   firstTime = false;
		}
	   
	   if(includeInWithParenthesis)
		   sqlBuilder.append(")");
		   
	  return sqlBuilder.toString();
   }// --------------------------------------------------------
   /**
    * Build an IN where cause
    * @param identifers the identifier to build a in clause
    * @return in (identifiers[0], identifiers[1] ... )
    */
   public static String toInSqlCause(Collection<Identifier> identifiers)
   {
	   if(identifiers == null || identifiers.isEmpty())
		   return "";
	   
	   StringBuilder sqlBuilder = new StringBuilder(" in (");
	   boolean firstTime = true;
	   
	   for (Identifier identifier : identifiers)
		{
			
			
		   if(!firstTime )
			   sqlBuilder.append(",");
			
		   sqlBuilder.append(formatValue(identifier.getId()));
		   
		   firstTime = false;
		}
	   
	   return sqlBuilder.append(")").toString();
   }// --------------------------------------------------------
   
   protected void addInToWhere(String column, String[] values, StringBuffer sql)
   {
      if(values != null && values.length > 0)
      {
         sql.append(" AND "+column+" IN ('"+values[0]+"'");
         for(int i =1 ; i < values.length; i++)
         {
           sql.append(" ,'"+values[i]+"'");  
         }
         sql.append(")");         
      }
   }// --------------------------------------------   

  
   
	 /**
	 * @return database information
	 * @throws SQLException
	 * @see java.sql.Connection#getMetaData()
	 */
	protected DatabaseMetaData getMetaData() throws SQLException
	{
		return getConnection().getMetaData();
	}//---------------------------------------------
	/**
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource()
	{
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	/**
	 * @return the dsName
	 */
	public String getDsName()
	{
		return dsName;
	}
	/**
	 * @param dsName the dsName to set
	 */
	public void setDsName(String dsName)
	{
		this.dsName = dsName;
	}
	/**
	 * @return the jbcdDriver
	 */
	public String getJbcdDriver()
	{
		return jbcdDriver;
	}
	/**
	 * @param jbcdDriver the jbcdDriver to set
	 */
	public void setJbcdDriver(String jbcdDriver)
	{
		this.jbcdDriver = jbcdDriver;
	}
	/**
	 * @return the connectionURL
	 */
	public String getConnectionURL()
	{
		return connectionURL;
	}
	/**
	 * @param connectionURL the connectionURL to set
	 */
	public void setConnectionURL(String connectionURL)
	{
		this.connectionURL = connectionURL;
	}
	/**
	 * @return the dbUserName
	 */
	public String getDbUserName()
	{
		return dbUserName;
	}
	/**
	 * @param dbUserName the dbUserName to set
	 */
	public void setDbUserName(String dbUserName)
	{
		this.dbUserName = dbUserName;
	}
	/**
	 * @return the dbPassword
	 */
	public char[] getDbPassword()
	{
		return dbPassword;
	}
	/**
	 * @param dbPassword the dbPassword to set
	 */
	public void setDbPassword(char[] dbPassword)
	{
		this.dbPassword = dbPassword;
	}


private ArrayPreparedStatementConstructor arrayPreparedStatementConstructor = new ArrayPreparedStatementConstructor();  
   protected SecurityCredential user = null;
   Connection the_connection = null;
   private boolean thisCreatedConnection = true;
   private WeakReference<Connection> connectionReference = null;
   private boolean disposed = false;
   private DataSource dataSource = null;
   private String dsName = Config.getProperty(this.getClass(),JdbcConstants.DS_NAME_PROP,Config.getProperty(JdbcConstants.DS_NAME_PROP, ""));
   private String jbcdDriver = Config.getProperty(this.getClass(),JdbcConstants.JDBC_DRIVER_PROP,Config.getProperty(JdbcConstants.JDBC_DRIVER_PROP,""));
   private String connectionURL = Config.getProperty(this.getClass(),JdbcConstants.JDBC_CONNECTION_URL_PROP,Config.getProperty(JdbcConstants.JDBC_CONNECTION_URL_PROP,""));
   private String dbUserName = Config.getProperty(this.getClass(),JdbcConstants.JDBC_USER_PROP,Config.getProperty(JdbcConstants.JDBC_USER_PROP,""));
   private char[] dbPassword = Config.getPropertyPassword(this.getClass(),JdbcConstants.JDBC_PASSWORD_PROP,Config.getPropertyPassword(JdbcConstants.JDBC_PASSWORD_PROP,""));

}