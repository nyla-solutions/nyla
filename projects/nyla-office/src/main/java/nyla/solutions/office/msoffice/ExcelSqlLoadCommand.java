/**
 * 
 */
package nyla.solutions.office.msoffice;

/**
 * Example: Config.properties
 * 
 * nyla.solutions.office.msoffice.excel.patterns.ExcelSqlLoadCommand.dataSourceName=asapDataMartDS
nyla.solutions.office.msoffice.excel.patterns.ExcelFileDirDbLoaderCommand.rootDirectory=/Projects/Merck/ASAP/dev/Merck.ASAP.integration/runtime/input/loader/
nyla.solutions.office.msoffice.excel.patterns.ExcelFileDirDbLoaderCommand.beforeSqlFilePath=/Projects/Merck/ASAP/dev/Merck.ASAP.integration/installation/db/hsqldb/asap_hsql_ddl.sql


Examples Spring config

  <bean id="asapDataMartDS" class="solutions.dao.spring.batch.ConfigDriverMgrDataSource">
		<property name="driverClassName" value="${jdbc.driver}" />
		<property name="url" value="${jdbc.connection.url}" />
		<property name="username" value="${jdbc.user}" />
		<!--  property name="password" value="${jdbc.password}" /-->
	</bean>
	
 * @author greeng3
 *
 */
public class ExcelSqlLoadCommand
        //implements Command<Boolean,String>
{
	

	/* (non-Javadoc)
	 * @see solutions.global.patterns.command.Executable#execute(solutions.global.patterns.command.Environment, java.lang.String[])
	 */
//	@Override
//	public Boolean execute(String filePath)
//	{
//		SaveRepository inserter = null;
//		try
//		{
//			if(!"".equals(this.dataSourceName))
//			{
//				DataSource ds = ServiceFactory.getInstance().create(this.dataSourceName);
//				inserter = Sql.connect(ds.getConnection());
//			}
//			else
//				inserter = SQL.connect(jdbcDriver, connectionURL, dbUserName, dbPassword);
//
//			if(filePath == null || filePath.length() == 0)
//				filePath = this.getFilePath();
//
//			if(filePath == null || filePath.length() == 0)
//				throw new RequiredException("File path not provided in input or configured as a default file path");
//
//			Excel.load(new  File(filePath), inserter, sheetName);
//
//		   return Boolean.TRUE;
//		}
//		catch (Exception e)
//		{
//			throw new SystemException(Debugger.stackTrace(e));
//		}
//		finally
//		{
//			if(inserter != null)
//				inserter.dispose();
//		}
//
//	}
//	/**
//	 * Load the database with data from a given excel file
//	 * @param args [0] = filePath [1]= sheetName
//	 */
//	public static void main(String [] args)
//	{
//
//		if(args.length < 2)
//		{
//			System.err.println("Usage: java "+ExcelSqlLoadCommand.class.getName()+" filePath sheetName");
//			System.exit(-1);
//		}
//		ExcelSqlLoadCommand cmd = new ExcelSqlLoadCommand();
//
//
//		cmd.setSheetName(args[1]);
//
//		cmd.execute(args[0]);
//
//	}
//	/**
//	 *
//	 * @return the sheet name
//	 */
//	public String getSheetName()
//	{
//		return sheetName;
//	}
//	/**
//	 * Set the sheet name
//	 * @param sheetName the sheet name to set
//	 */
//	public void setSheetName(String sheetName)
//	{
//		this.sheetName = sheetName;
//	}
//
//	/**
//	 *
//	 * @return the file path
//	 */
//	public String getFilePath()
//	{
//		return filePath;
//	}
//
//	public void setFilePath(String filePath)
//	{
//		this.filePath = filePath;
//	}
//
//
//	/**
//	 * @return the dataSourceName
//	 */
//	public String getDataSourceName()
//	{
//		return dataSourceName;
//	}
//	/**
//	 * @param dataSourceName the dataSourceName to set
//	 */
//	public void setDataSourceName(String dataSourceName)
//	{
//		this.dataSourceName = dataSourceName;
//	}
//	/**
//	 * @return the jdbcDriver
//	 */
//	public String getJdbcDriver()
//	{
//		return jdbcDriver;
//	}
//	/**
//	 * @param jdbcDriver the jdbcDriver to set
//	 */
//	public void setJdbcDriver(String jdbcDriver)
//	{
//		this.jdbcDriver = jdbcDriver;
//	}
//	/**
//	 * @return the connectionURL
//	 */
//	public String getConnectionURL()
//	{
//		return connectionURL;
//	}
//	/**
//	 * @param connectionURL the connectionURL to set
//	 */
//	public void setConnectionURL(String connectionURL)
//	{
//		this.connectionURL = connectionURL;
//	}
//	/**
//	 * @return the dbUserName
//	 */
//	public String getDbUserName()
//	{
//		return dbUserName;
//	}
//	/**
//	 * @param dbUserName the dbUserName to set
//	 */
//	public void setDbUserName(String dbUserName)
//	{
//		this.dbUserName = dbUserName;
//	}
//	/**
//	 * @return the dbPassword
//	 */
//	public char[] getDbPassword()
//	{
//		if(dbPassword == null)
//			return null;
//
//
//		return Arrays.copyOf(dbPassword,dbPassword.length);
//	}//------------------------------------------------
//	/**
//	 * @param dbPassword the dbPassword to set
//	 */
//	public void setDbPassword(char[] dbPassword)
//	{
//		if(dbPassword == null)
//		{
//			this.dbPassword = null;
//		}
//		else
//		{
//			this.dbPassword = Arrays.copyOf(dbPassword,0);
//		}
//	}//------------------------------------------------
//
//
//	private String dataSourceName = Config.getProperty(ExcelSqlLoadCommand.class,"dataSourceName",Config.getProperty(JdbcConstants.DS_NAME_PROP,""));
//	private String jdbcDriver = Config.getProperty(ExcelSqlLoadCommand.class,"jdbcDriver",Config.getProperty(JdbcConstants.JDBC_DRIVER_PROP,""));
//	private String connectionURL = Config.getProperty(ExcelSqlLoadCommand.class,"connectionURL",Config.getProperty(JdbcConstants.JDBC_CONNECTION_URL_PROP,""));
//	private String dbUserName =  Config.getProperty(ExcelSqlLoadCommand.class,"dbUserName",Config.getProperty(JdbcConstants.JDBC_USER_PROP,""));
//	private char[] dbPassword = Config.getPropertyPassword(ExcelSqlLoadCommand.class,"dbPassword",Config.getPropertyPassword(JdbcConstants.JDBC_PASSWORD_PROP,""));
//	private String filePath = Config.getProperty(ExcelSqlLoadCommand.class,"filePath","");
//	private String sheetName = Config.getProperty(ExcelSqlLoadCommand.class,"sheetName","");;

}
