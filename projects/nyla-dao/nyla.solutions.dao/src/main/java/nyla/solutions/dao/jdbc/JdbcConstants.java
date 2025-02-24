package nyla.solutions.dao.jdbc;

import nyla.solutions.core.util.Config;

public interface JdbcConstants
{
	  /**
	    * 
	    */
	   public static final String JDBC_ENABLE_DEBUG_PROP = "jdbc.enable.debug";
	   
	   /**
	    * JDBC_CONNECTION_URL_PROP = "jdbc.connection.url"
	    */
	   public static final String JDBC_CONNECTION_URL_PROP = "jdbc.connection.url";
	   
	   /**
	    * JDBC_DRIVER_PROP = "jdbc.driver"
	    */
	   public static final String JDBC_DRIVER_PROP = "jdbc.driver";
	   
	   /**
	    * JDBC_USER_PROP = "jdbc.user"
	    */
	   public static final String JDBC_USER_PROP = "jdbc.user";
	   
	   /**
	    * JDBC_PASSWORD_PROP = "jdbc.password"
	    */
	   public static final String JDBC_PASSWORD_PROP = "jdbc.password";
	   

	   /**
	    * JDBC_AUTOCOMMIT_PROP = "jdbc.autoCommit"
	    */
	   public static final String JDBC_AUTOCOMMIT_PROP = "jdbc.autoCommit";
	   
	   /**
	    * JDBC_DEFAULT_AUTOCOMMIT = Config.getPropertyBoolean("jdbc.autoCommit.default",true)
	    */
	   public static final boolean JDBC_DEFAULT_AUTOCOMMIT = Config.getPropertyBoolean("jdbc.autoCommit.default",true);
	   
	   /**
	    * JDBC Database source property name "JDBCDSName"
	    */
	   public static final String DS_NAME_PROP = "JDBCDSName";
	   
	   /**
	    * JNDI Property JDBCJNDIInitialContextFactory
	    */
	   public static final String JNDI_JDBC_INTIAL_ContextFactory = "JDBCJNDIInitialContextFactory";
	      

	   /**
	    * JNDI Directory Property "JDBCJNDIProviderURL"
	    */   
	   public static final String JNDI_JDBC_PROVIDER_URL = "JDBCJNDIProviderURL";

}
