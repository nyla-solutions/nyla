package nyla.solutions.dao;
import java.sql.*;

import javax.naming.*;
import javax.sql.DataSource;

import java.util.*;

import nyla.solutions.dao.jdbc.JdbcConstants;
import nyla.solutions.dao.jdbc.pooling.JdbcConnectionFactory;
import nyla.solutions.core.data.DataRow;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Pagination;
import nyla.solutions.core.security.data.SecurityCredential;
import nyla.solutions.core.util.*;

/**
 * 
 * <pre>
 * DAOFactory factory object for creations of data access object resource
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class DAOFactory
{
	/**
	 * Default constructor
	 */
    public DAOFactory()
    {
    }//--------------------------------------------
    /**
     * Create a pagination instance
     * @return
     */
    public static Pagination createPagination(PageCriteria pageCriteria)
    {
    
    	if(pageCriteria == null)
    		return null;
    	
		return ClassPath.newInstance(paginationClassName,String.class,pageCriteria.getId());

    }// --------------------------------------------------------
	/**
	 * Get the data source by its name
	 * @param dataSourceName the data source name
	 * @return the created data source
	 */
	public static DataSource getDataSource(String dataSourceName)
	{
		
		//previous data source
		
		DataSource dataSource = dataSources.get(dataSourceName);
		
		if(dataSource == null)
		{
			//create data sources
			
			String prop = new StringBuilder("factory")
					.append(".")
					.append(dataSourceName).append(".").append(JdbcConstants.JDBC_DRIVER_PROP).toString();
			
			JdbcConnectionFactory jcf = getConnectionFactory();
			jcf.setDriver(Config.getProperty(prop));
			
			prop = new StringBuilder("factory")
			.append(".").append(dataSourceName).append(".").append(JdbcConstants.JDBC_CONNECTION_URL_PROP).toString();
			jcf.setConnectionUrl(Config.getProperty(prop));
			
			
			//user name
			prop = new StringBuilder("factory")
			.append(".").append(dataSourceName).append(".").append(JdbcConstants.JDBC_USER_PROP).toString();
			String user = Config.getProperty(prop,"");
			
			if(user.length() != 0 )
				jcf.setUserName(user);
			
			//Password
			prop = new StringBuilder("factory")
			.append(".").append(dataSourceName).append(".").append(JdbcConstants.JDBC_PASSWORD_PROP).toString();
			
			
			char[] password = Config.getPropertyPassword(prop,"");
			if(password.length > 0)
				jcf.setPassword(password);
			
			dataSource = jcf.getDataSource();
			dataSources.put(dataSourceName,dataSource);
		}
		
		return dataSource;
	}// --------------------------------------------------------
    /**
     * 
     * @return the default BoneCP instance
     */
    private synchronized static JdbcConnectionFactory getConnectionFactory()
    {
		if (jdbcConnectionFactory == null)
    	    jdbcConnectionFactory =  ClassPath.newInstance(jdbcConnectionFactoryClassName);
			
		return jdbcConnectionFactory;
    }// --------------------------------------------------------
	/**
	 * 
	 * @return the JDBC connection factory
	 */
    public static JdbcConnectionFactory createConnectionFactory()
    {
		return ClassPath.newInstance(jdbcConnectionFactoryClassName);

    }// --------------------------------------------------------
    /**
     * 
     * @param aResultSet the result set
     * @return the collection
     * @throws SQLException
     */
    public static Collection<String> toColumnNames(ResultSet aResultSet)
    throws SQLException
    {
       ResultSetMetaData meta = aResultSet.getMetaData();
       int columnCount = meta.getColumnCount();
       Collection<String> columnNames = new HashSet<String>();
       for (int i = 1; i <= columnCount; i++)
       {
          columnNames.add(meta.getColumnName(i));  
       }
       
       return columnNames;
    }// --------------------------------------------
    /**
     * 
     */
    /*public static DataRow toDataRow(ResultSet aResultSet, DataResultSet dataResultSet)
    throws SQLException
    {       
       return new ResultSetDataRow(aResultSet);
      
    }*/
    // --------------------------------------------
    /**
     * 
     * @param dsName the data source name
     * @param aDriver
     * @param aConnectionURL
     * @param aUser
     * @param aPassword
     * @return
     * @throws Exception
     */
    static Connection createJDBCConnection(String dsName, String aDriver, String aConnectionURL,String aUser,char[] aPassword)
    throws ConnectionException
    {

    	   //Check is data source name provided
    	   if(dsName != null && dsName.length() > 0)
    		   return createConnectionFromDS(dsName);
    	   
          return createJDBCConnection(aDriver, aConnectionURL, aUser, aPassword);
   
    }//--------------------------------------------
    static Connection createJDBCConnection(String aDriver, String aConnectionURL,String aUser,char[] aPassword)
    throws ConnectionException
    {    	    	   
    	   try
		   {
    		   Connection connection = null;
			
    		   Class.forName(aDriver);

				Debugger.println(aConnectionURL);
				
				String password = null;
				if(aPassword != null)
				{
					password = new String(aPassword);
					if(password.indexOf(Cryption.CRYPTION_PREFIX) < 0)
					 {
					    Debugger.printWarn("Provided password is not encrypted!");
					 }
					 else
					 {
					    //decrypt password
					    password = Cryption.interpret(password);
					 }
				}
						 //check if connection is encrypted            
						 
						 
						 
						// connection = DriverManager.getConnection(
						//                url,
						//                configFile.getString("jdbc.user"),
						//                configFile.getString("jdbc.password"));                        

						 
						 connection = DriverManager.getConnection(
						                 aConnectionURL,
						                 aUser,
						                 password
						                 );                        

	          return connection;
			}
			catch (ClassNotFoundException e)
			{
				throw new ConnectionException(e);
			}
			catch (SQLException e)
			{
				throw new ConnectionException(e);
			} 
    }//--------------------------------------------
    /**
     * Create a JDBC based connection
     * @return the connection object
     * @throws ConnectionException
     */
    static Connection createJDBCConnection()
    throws ConnectionException
    {
       try
       {
      
          Connection connection = null;

             Class.forName(Config.getProperty(JdbcConstants.JDBC_DRIVER_PROP));
             
             String url = Config.getProperty(JdbcConstants.JDBC_CONNECTION_URL_PROP);
             Debugger.println(DAOFactory.class,new StringBuilder(JdbcConstants.JDBC_DRIVER_PROP).append("=").append(url).toString());
             
             String password = 
                     new String(Config.getPropertyPassword(JdbcConstants.JDBC_PASSWORD_PROP,""));
             
             if(password.length() == 0)
            	 password = null;
             
             connection = DriverManager.getConnection(
                            url,
                            Config.getProperty(JdbcConstants.JDBC_USER_PROP),
                            password);    
             
             
             boolean autoCommit = Config.getPropertyBoolean(JdbcConstants.JDBC_AUTOCOMMIT_PROP,JdbcConstants.JDBC_DEFAULT_AUTOCOMMIT).booleanValue();
             connection.setAutoCommit(autoCommit);
             
  
          return connection;
       }
       catch (Exception e)
       {
          e.printStackTrace();
          throw new ConnectionException(e);
       }
    }//--------------------------------------------
    protected static Connection createConnection(SecurityCredential aUser)
    {
       if(!Text.isNull(Config.getProperty(JdbcConstants.DS_NAME_PROP, "")))
       {
          return createConnectionFromDS(aUser);   
       }
       else
       {
          return createJDBCConnection();
       }
    }//--------------------------------------------
    /**
     * 
     * @param aUser the user
     * @return the connection for the dataSource
     */
    protected static Connection createConnectionFromDS(SecurityCredential aUser)
    {
    	// Lookup through the namingservice to retrieve a DataSource object
        String dsName = Config.getProperty(JdbcConstants.DS_NAME_PROP); 
        
        return createConnectionFromDS(dsName);
    }// --------------------------------------------------------
    /**
     * 
     * @param dsName the data source name
     * @return the created connection
     */
    protected static Connection createConnectionFromDS(String dsName)
    throws ConnectionException
    {
      javax.sql.DataSource ds;
 
      try
      {
            Debugger.println(DAOFactory.class,"looking up ["+dsName+"]");
            
            ds =(javax.sql.DataSource) getContext().lookup(dsName);
            // Obtain a Connection from the DataSource

            return ds.getConnection();
         }
         catch (Exception e)
         {
            throw new ConnectionException(e);
         }

   }//--------------------------------------------
    
    /**
     * 
     * @param resultSet
     * @return column names for the results set
     * @throws SQLException
     */
    public static String[] getColumnNames(ResultSet resultSet)
    throws SQLException
    {
       ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
       
       int columnCount = resultSetMetaData.getColumnCount();
       
       String [] columnNames = new String[columnCount];
       
       for (int i = 0; i < columnCount; i++)
       {
          columnNames[i] = resultSetMetaData.getColumnName(i+1);  
       }
       return columnNames;
    }//--------------------------------------------
    public static String getDatabaseTypeName()
    {
        return Config.getProperty("jdbc.database.type.name");
    }//--------------------------------------------

    /**
     * This method is used to put all records in the result set 
     * into the DataResultSet.
     * Note the result set must be closed after calling this method.
     * @param aResultSet the result set
     * @return the DataResultSet
     * @throws SQLException
     */
    public static DataResultSet toDataResultSet(ResultSet aResultSet)
    throws SQLException
    {
       if (aResultSet == null)
         throw new IllegalArgumentException(
         "aResultSet required");
       
       ResultSetMetaData meta = aResultSet.getMetaData();
       int columnCount = meta.getColumnCount();
       DataResultSet dataResultSet = new DataResultSet();
       
       for (int i = 0; i < columnCount; i++)
       {
          dataResultSet.addColumn(meta.getColumnName(i+1),i);  
       }
       
       //Collection columnNames = dataResultSet.getColumnNames();
       
       while(aResultSet.next())
       {
          dataResultSet.addRow(toDataRow(aResultSet));
       }
       
       return dataResultSet;
    }// --------------------------------------------

    /**
     * 
     */
    public static DataRow toDataRow(ResultSet aResultSet)
    throws SQLException
    {       
      /* ResultSetDataRow row = new ResultSetDataRow();
       int columnCount = dataResultSet.getColumnCount();
       
      for (int i = 0; i< columnCount; i++)
      {         
         
         row.add(aResultSet.getObject(i+1));
      }
      return row;
      */
 	   return new ResultSetDataRow(aResultSet);
    }// --------------------------------------------
    /**
     * 
     * @param resultSet the result set to retrieve meta data
     * @return number of columns in result set
     * @throws SQLException
     */
    public static int retrieveColumnCount(ResultSet resultSet)
    throws SQLException
    {
       ResultSetMetaData meta = resultSet.getMetaData();
       return meta.getColumnCount();
       
    }// --------------------------------------------

    /**
     * 
     * @return initial context
     * @throws NamingException
     */
    private static InitialContext getContext()
        throws NamingException
    {
        if(ctx == null)
        {
           
            String contextFactoryNM = Config.getProperty("JDBCJNDIInitialContextFactory");
            String providerURL = Config.getProperty("JDBCJNDIProviderURL");
            Properties parms = new Properties();
            parms.setProperty("java.naming.factory.initial", contextFactoryNM);
            parms.setProperty("java.naming.provider.url", providerURL);
            Debugger.println(DAOFactory.class,"JNDI properties=" + parms);
            ctx = new InitialContext(parms);
        }
        return ctx;
    }//--------------------------------------------
	private static JdbcConnectionFactory jdbcConnectionFactory = null;
	
	private static Map<String,DataSource> dataSources = new HashMap<String, DataSource>();
    
    public static final String ORACLE_DATABASE_NAME = "oracle";
    private static InitialContext ctx = null;
    private static final String paginationClassName = "nyla.solutions.dao.mongodb.JDBCMongoPagination";
    private static final String  jdbcConnectionFactoryClassName = Config.getProperty(DAOFactory.class,"jdbcConnectionFactoryClassName",
    		"nyla.solutions.dao.jdbc.pooling.TomcatJdbcConnectionFactory");
    
}
