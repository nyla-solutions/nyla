/**
 * 
 */
package nyla.solutions.office.msoffice.excel.reporting;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;
import nyla.solutions.core.util.settings.Settings;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;
import nyla.solutions.office.msoffice.excel.CSV;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;


/**
 * <pre>
 * Use the capture the following information CSV header information
 * 
 * "label","date", "cnt", "exe_tm_ms", "avg_fetch_time_ms"
 * 
 * 
 * set the property 
 * sql 
 * csvFilePath
 * label
 * 
 * Sample
      &lt;bean id="selectUser" class="nyla.solutions.office.msoffice.excel.reporting.CsvSelectSummaryExecutable"&gt;			
		&lt;property name="label" value="sss1"/&gt;
		&lt;property name="sql"&gt;
				&lt;value&gt;
					&lt;![CDATA[select user from dual]]&gt;
				&lt;/value&gt;
		&lt;/property&gt;
		&lt;property name="csvFilePath" value="C:/temp/user_session.csv"/&gt;
		&lt;property name="connectionURL" value="jdbc:oracle:thin:@host:port:sid"/&gt;
		
		&lt;property name="dbUserName" value="user"/&gt;
		&lt;property name="dbPassword" value="pass"/&gt;
	&lt;/bean&gt;
</pre>
 * @author Gregory Green
 *
 */
public class CsvSelectSummaryExecutable extends AbstractDaoOperation implements Executable
{
	private Object[] parameters = null;
	private String dateFormat = Config.settings().getProperty(CsvSelectSummaryExecutable.class,"dateFormat","yyyy-MM-dd:HH:mm:ss:SS");
	private String label = Config.settings().getProperty(CsvSelectSummaryExecutable.class,"label","");
	private static String[] header = {"label","date","cnt","exe_tm_ms", "avg_fetch_time_ms" };
	private File csvFile;
	private String csvFilePath = Config.settings().getProperty(CsvSelectSummaryExecutable.class,"csvFilePath");
	/**
    * Constructor
    */
   public CsvSelectSummaryExecutable()
   {
	
   }// ----------------------------------------------
      /**
	 * Execute an insert statement
	 * @param env the env
	 * 
	 * @return the exit code
	 */
	public Integer execute(Settings env)
	{		
		 if(this.getSql() == null || this.getSql().length() == 0)
	         throw new ConfigException("Property \"sqlQuery\"  not setin "+this.getClass().getName());
	      
		 Debugger.println(this,"CSV SQL label:"+this.label);
		 
	      SQL sql = null;
	      Statement ps = null;
	      ResultSet rs = null;
	      try
	      {
	         sql = SQL.connect(this.getJdbcDriver(),this.getConnectionURL(),this.getDbUserName(), this.getDbPassword());    
	         
	         
	         //ps = sql.prepareStatement(this.getSql());
	         ps = sql.createStatement();
	         
	         //if(this.parameters != null && this.parameters.length > 0)
	         //{
	      	//sql.initPreparedStatement(this.parameters, ps);
	         //}
	         
	         //ps.setFetchSize(1000);
	         //ps.setMaxFieldSize(70000);
	         
	         //ps.setQueryTimeout(4000);	         
	         //ps.setFetchSize(7000);
	         	         
	         long start = System.currentTimeMillis();
	         rs = ps.executeQuery(this.getSql());
	         long exe_time = System.currentTimeMillis() - start;
	         
	         int count = 0;
	         
	         start = System.currentTimeMillis();
	         long fetch_duration = 0;
	         
	         while(rs.next())
	         {
	      	//mark duration
	      	fetch_duration += System.currentTimeMillis() - start;
	      	count++;
	      	start = System.currentTimeMillis();
	         }
	         
	         if(csvFile == null)
	      	csvFile = new File(this.csvFilePath);
	         
	         if(!csvFile.exists())
		      {
		         //write header
		         CSV.appendFile(csvFile, (Object[])header);
		      }
	         
	         double avg_fetch = 0;
	         if(count > 0)
	         {
	      	avg_fetch = fetch_duration/(count*1.0);
	         }
	         
	         //write line
		    Object[] cells = {label, Text.formatDate(this.dateFormat, Calendar.getInstance().getTime()),Integer.valueOf(count),Long.valueOf(exe_time),Double.valueOf(avg_fetch)};
		    CSV.appendFile(csvFile, cells);
		    
		    return 1;
	         
	      }
	      catch(Exception e)
	      {
	         throw new SystemException(e);
	      }
	      finally
	      {
	         if(rs != null)
	      	try{ rs.close();} catch(Exception e){}
	      	
	        if(ps != null)
		      	try{ ps.close();} catch(Exception e){Debugger.printError(e);}
	         if(sql != null)
	            sql.dispose();
	      }
	      
	}//---------------------------------------------
	
	/**
	 * @return the label
	 */
	public String getLabel()
	{
	   return label;
	}// ----------------------------------------------
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label)
	{
	   this.label = label;
	}//------------------------------------------------
	/**
	 * @return the header
	 */
	public synchronized static String[] getHeader()
	{	   
		if(header == null|| header.length == 0)
			return null;
		
	   return Arrays.copyOf(header,header.length);
	}// ----------------------------------------------
	/**
	 * @param header the header to set
	 */
	public static synchronized void setHeader(String[] header)
	{
		if(header == null)
			CsvSelectSummaryExecutable.header = null;
		else
		{
			CsvSelectSummaryExecutable.header = Arrays.copyOf(header,header.length);	
		}
	   
	}// ----------------------------------------------	
	/**
	 * @return the dateFormat
	 */
	public String getDateFormat()
	{
	   return dateFormat;
	}
	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat)
	{
	   this.dateFormat = dateFormat;
	}
	/**
	 * @return the csvFilePath
	 */
	public String getCsvFilePath()
	{
	   return csvFilePath;
	}
	/**
	 * @param csvFilePath the csvFilePath to set
	 */
	public void setCsvFilePath(String csvFilePath)
	{
	   this.csvFilePath = csvFilePath;
	}// ----------------------------------------------
		
	/**
	 * @return the parameters
	 */
	public Object[] getParameters()
	{
		if(parameters == null || parameters.length == 0)
			return null;
		
	   return Arrays.copyOf(parameters,parameters.length);
	}//------------------------------------------------
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Object[] parameters)
	{
		if(parameters == null || parameters.length == 0)
			this.parameters = null;
		else
		{
			this.parameters = Arrays.copyOf(parameters,parameters.length);
		}
	   
	}//------------------------------------------------
	

}
