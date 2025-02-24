package nyla.solutions.dao;


import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.xml.XML;

/**
 * <pre>
 * <b>XmlQuery</b> is a textable version of of SLQ query result set  
 * 
 * <b>Sample Spring Definition</b>
 * 	&lt;bean id="dbJobLongRunningWeek" class="nyla.solutions.dao.XmlQuery"
		singleton="false"&gt;
		&lt;property name="sql"&gt;
			&lt;value&gt;
				select JOB_NAME, TO_CHAR(LOG_DATE,'MM/dd/YYYY hh:mi:ss AM') as
				log_date,
				TO_CHAR(RUN_DURATION),
				NVL(ADDITIONAL_INFO,' ')
				from
				dba_scheduler_job_run_details
				where run_duration > INTERVAL
				'0 1:00:0'
				DAY TO SECOND(0)
				and TO_CHAR(LOG_DATE,'IW') =
				TO_CHAR(SYSDATE,'IW')
				and status = 'SUCCEEDED'
				order by JOB_NAME, LOG_DATE
			&lt;/value&gt;
		&lt;/property&gt;
	      &lt;property name="connectionURL" value="jdbc.connection.url=jdbc:oracle:thin:@sid"/&gt;
		&lt;property name="jdbc.driver" value="oracle.jdbc.driver.OracleDriver"/&gt;
		&lt;property name="dbUserName" value="user"/&gt;
		&lt;property name="dbPassword" value="{cryption}-25 -20 98 -25.."/&gt;
	&lt;/bean&gt;

   <b>Sample XML results</b>
   &lt;dataRows&gt;
    &lt;nyla.solutions.dao.DataRow&gt;
      &lt;useNullString&gt;false&lt;/useNullString&gt;
      &lt;entries&gt;
        &lt;string&gt;BIODATA&lt;/string&gt;
        &lt;big-decimal&gt;0&lt;/big-decimal&gt;
        &lt;big-decimal&gt;15678056&lt;/big-decimal&gt;
      &lt;/entries&gt;
    &lt;/nyla.solutions.dao.DataRow&gt;
    &lt;dataRows&gt;
   </pre>
 * @author Gregory Green
 *
 */
public class XmlQuery extends Query implements Textable
{
   /**
    * 
    * Execute the sqlQuery and return XML results in the format DataResultSet
    * 
    */
   public String getText()
   {
      try
      {         
         String xml = XML.getInterpreter().toXML(this.getResults());
         
         Debugger.println(this, "xml query results="+xml);
         return xml;
                  
      }
      catch(NoDataFoundException e)
      {
         return "";
      }
   }//--------------------------------------------
   public int compareTo(Object arg0)
   {    
      return 0;
   }//--------------------------------------------   
}
