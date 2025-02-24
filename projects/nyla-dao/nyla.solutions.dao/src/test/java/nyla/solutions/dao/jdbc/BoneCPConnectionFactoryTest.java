package nyla.solutions.dao.jdbc;

import java.sql.Connection;

import org.junit.Ignore;

import nyla.solutions.dao.SQL;
import nyla.solutions.dao.jdbc.pooling.TomcatJdbcConnectionFactory;
import nyla.solutions.core.exception.NoDataFoundException;
import junit.framework.TestCase;

@Ignore
public class BoneCPConnectionFactoryTest extends TestCase
{

	public void testConnection() throws Exception
	{
		
		String jdbcDriver = "com.vmware.sqlfire.jdbc.ClientDriver";
		String connectionURL="jdbc:sqlfire://localhost:1521";
		String dbUserName="app";
		char[] dbPassword=null;
		
		SQL conn = SQL.connect(jdbcDriver, connectionURL, dbUserName, dbPassword);
		
		try
		{
			conn.selectDataResultSet("select * from inventory");
		}
		catch(NoDataFoundException e)
		{
			
		}
		
		
		conn.dispose();
		
		
		TomcatJdbcConnectionFactory bcpf = new TomcatJdbcConnectionFactory();
		bcpf.setConnectionUrl(connectionURL);
		bcpf.setDriver(jdbcDriver);
		bcpf.setUserName(dbUserName);
		
		Connection connection = bcpf.getDataSource().getConnection();
		
		conn = SQL.connect(connection);
		
		conn.selectDataResultSet("select * from inventory");
			
	}
}
