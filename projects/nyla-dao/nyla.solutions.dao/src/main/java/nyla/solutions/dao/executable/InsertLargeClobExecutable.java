package nyla.solutions.dao.executable;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;

import nyla.solutions.commas.Executable;
import nyla.solutions.core.data.Environment;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.dao.AbstractDaoOperation;
import nyla.solutions.dao.SQL;

/**
 *  * @deprecated use commands
 * @author Gregory Green
 *
 */
@Deprecated
public class InsertLargeClobExecutable 
extends AbstractDaoOperation implements Executable
{
	
	/**
	 * 
	 * @see solutions.global.patterns.command.Executable#execute(solutions.global.patterns.command.Environment, java.lang.String[])
	 */
   public Integer execute(Environment env)
   {
	Statement statement = null;
	   
	ResultSet clobRS = null;
	ResultSet queryRS = null;
	SQL sql = null;
      try
      {
          sql =this.connect();
          
          
          statement = sql.createStatement();
          
          statement.getConnection().setAutoCommit(false);
          
          
          statement.execute(this.getSql());          
          
          Debugger.println(this,"executing SQL="+selectClobForUpdateSQL);
          
          clobRS = statement.executeQuery(this.selectClobForUpdateSQL);
          
          if(!clobRS.next())
          {
      	 throw new NoDataFoundException(selectClobForUpdateSQL);
          }
          
          //if(needsNewlineToCrLf)
          //{
      	// insertData = Text.newlineToCrLf(insertData);
          //}
          
          SQL.setClob(clobRS, clobPosition, new File(this.clobTextFilePath));          
                   
          statement.getConnection().commit();
          
          return 1;

      }
      catch(Exception e)
      {
         throw new SystemException(Debugger.stackTrace(e));
      }
      finally
      {
         if(queryRS != null)
      	try{ queryRS.close(); } catch(Exception e){}
      	
         if(clobRS != null)
      	try{ clobRS.close(); } catch(Exception e){}
      
         if(statement != null)
      	try{ statement.close(); } catch(Exception e){}
      	
          if(sql != null)
      	 try { sql.dispose(); } catch(Exception e){}
      }
   }// ----------------------------------------------
	/**
	 * Wrapper execution for a command
	 * @source
	 * @see solutions.global.patterns.command.Command#execute(java.lang.Object)
	 */
	//public Object execute(Environment source)
	//{
	//	execute(null,null);
		
	//	return null;
	//}
   
   
   /**
    * @return the label
    */
   public String getLabel()
   {
      return label;
   }
   /**
    * @param label the label to set
    */
   public void setLabel(String label)
   {
      this.label = label;
   }
   
   /**
    * @return the clobTextFilePath
    */
   public String getClobTextFilePath()
   {
      return clobTextFilePath;
   }



   /**
    * @param clobTextFilePath the clobTextFilePath to set
    */
   public void setClobTextFilePath(String clobTextFilePath)
   {
      this.clobTextFilePath = clobTextFilePath;
   }



   /**
    * @return the clobPosition
    */
   public int getClobPosition()
   {
      return clobPosition;
   }
   /**
    * @param clobPosition the clobPosition to set
    */
   public void setClobPosition(int clobPosition)
   {
      this.clobPosition = clobPosition;
   }
   /**
    * @return the selectClobForUpdateSQL
    */
   public String getSelectClobForUpdateSQL()
   {
      return selectClobForUpdateSQL;
   }
   /**
    * @param selectClobForUpdateSQL the selectClobForUpdateSQL to set
    */
   public void setSelectClobForUpdateSQL(String selectClobForUpdateSQL)
   {
      this.selectClobForUpdateSQL = selectClobForUpdateSQL;
   }
   
   /**
    * @return the needsNewlineToCrLf
    */
   public boolean isNeedsNewlineToCrLf()
   {
      return needsNewlineToCrLf;
   }



   /**
    * @param needsNewlineToCrLf the needsNewlineToCrLf to set
    */
   public void setNeedsNewlineToCrLf(boolean needsNewlineToCrLf)
   {
      this.needsNewlineToCrLf = needsNewlineToCrLf;
   }

   private String label = null;   

   private boolean needsNewlineToCrLf = false;
   private String clobTextFilePath = null;
   private int clobPosition = 1;
   private String selectClobForUpdateSQL = "";

}
