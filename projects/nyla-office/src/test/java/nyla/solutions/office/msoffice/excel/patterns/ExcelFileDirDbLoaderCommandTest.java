//package nyla.solutions.office.msoffice.excel.patterns;
//
//import junit.framework.TestCase;
//
//import org.junit.Assert;
//import org.junit.Ignore;
//
//@Ignore
//public class ExcelFileDirDbLoaderCommandTest extends TestCase
//{
//	public void testExecute()
//	{
//		ExcelFileDirDbLoaderCommand cmd = new ExcelFileDirDbLoaderCommand();
//		cmd.setConnectionURL("jdbc:hsqldb:mem:exceldb");
//		cmd.setJdbcDriver("org.hsqldb.jdbc.JDBCDriver");
//		cmd.setDbUserName("SA");
//		cmd.setRootDirectory("/Projects/Merck/ASAP/dev/Merck.ASAP.integration/runtime/input/loader/");
//
//		cmd.setBeforeSqlFilePath(beforeSqlFile);
//
//		Assert.assertFalse(cmd.execute(null));
//
//		String[] args = {"wpp"};
//
//		Assert.assertTrue(cmd.execute(args));
//	}
//
//
//	private String beforeSqlFile = "/Projects/Merck/ASAP/dev/Merck.ASAP.integration/installation/db/hsqldb/asap_hsql_ddl.sql";
//
//}
