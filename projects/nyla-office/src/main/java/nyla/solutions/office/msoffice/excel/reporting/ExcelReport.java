package nyla.solutions.office.msoffice.excel.reporting;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.dao.reporting.AbstractReport;
import nyla.solutions.office.msoffice.excel.CSV;



/**
 * 
 * <pre>
 * Default in config.properties
 * 
 * package.ExcelReport.sql=select sysdate current_date,
 *  b.sid,b.instance,a.JOB, a.SCHEMA_USER,a.LAST_DATE,a.NEXT_DATE,a.TOTAL_TIME,a.FAILURES,WHAT
 * from dba_jobs a,dba_jobs_running b
 *    where  a.job=b.job;
 * 
 * jdbc.connection.url=jdbc:oracle:thin:@host:port:db
 * jdbc.driver=oracle.jdbc.driver.OracleDriver
 * jdbc.user=user
 * jdbc.password=ABC
 * 
 * </pre>
 * @author Gregory Green
 *
 */
public class ExcelReport  extends AbstractReport implements Executable
{
   /**
    * 
    * Constructor for ExcelReport initializes internal 
    */
   public ExcelReport()
   {
   }// --------------------------------------------  
   /**
    * 
    * @param jdbcDriver
    * @param connectionURL
    * @param user
    * @param password
    * @throws ConnectionException
    */
   public ExcelReport(String jdbcDriver, String connectionURL, String user,
	   String password) throws ConnectionException
   {
	super(jdbcDriver, connectionURL, user, password.toCharArray());
   }// ----------------------------------------------

   public void printRow(Object[] row)
   {
      try
      {
         CSV.appendFile(new File(this.filePath), row);
      }
      catch (IOException e)
      {
         throw new SystemException(Debugger.stackTrace(e));
      }      
   }// --------------------------------------------
   /**
    * Print report based on query in config property
    * this.getClass().getName()+".sql"
    */
   public void printReport()
   {
      printReport(Config.getProperty(this.getClass().getName()+".sql"));
   }//--------------------------------------------
   public void printHeader(Object[] rows)
   {     
	//only print the header the first time
	if(!IO.exists(this.filePath))
      {
	   printRow( rows);
      }
   }//--------------------------------------------
   /**
    * 
    * @param sql the SQL to report on
    */
   public void printReport(String sql)
   {
      PreparedStatement ps = null;
      ResultSet rs = null;
      
      try
      {
         ps = this.prepareStatement(sql);
         
         rs = this.select(ps);
         
         this.printReport(rs);
      }
      catch(Exception e)
      {
         throw new SystemException(Debugger.stackTrace(e));
      }
      finally
      {
         if (rs != null) 
            try{ rs.close();} catch (Exception e){ }
            
         if (ps != null) 
            try { ps.close();} catch (Exception e){}
      }

   }// --------------------------------------------
   /**
    * 
    * @param args must contain the output file path
 * @throws Exception 
    */
   public static void main(String[] args) throws Exception
   {
	 if(args.length < 2)
	 {
	      System.err.println("Usage java "+ExcelReport.class.getName()+" filePath SQL");
	      System.exit(-1);
	 }
	
      Environment env = new Environment();
      env.setArgs(args);
      
     try(ExcelReport report = new ExcelReport())
     {
         report.filePath = args[0];
         report.sql = args[1];
         report.execute(env);
     }

      
   }// --------------------------------------------
   
   public Integer execute(Environment env)
   {
	if (this.filePath == null || this.filePath.length() == 0)
	   throw new RequiredException("this.filePath");

	if (this.sql == null || this.sql.length() == 0)
	   throw new RequiredException("this.sql");
	
	try
	{
	         
	         if(env.getArgs().length == 1)
	         {
	            printReport();   
	         }
	         else
	         {
	            
	            printReport(sql);
	         }
	         
	         return 1;
	         
	      }
	      finally
	      {
	            dispose();
	      }

   }// ----------------------------------------------   
   
   /**
    * @return the filePath
    */
   public String getFilePath()
   {
      return filePath;
   }
   /**
    * @param filePath the filePath to set
    */
   public void setFilePath(String filePath)
   {
      this.filePath = filePath;
   }
   /**
    * @return the sql
    */
   public String getSql()
   {
      return sql;
   }
   /**
    * @param sql the sql to set
    */
   public void setSql(String sql)
   {
      this.sql = sql;
   }

   private String filePath = null;
   private String sql = null;
   




}
