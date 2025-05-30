package nyla.solutions.dao;
import java.util.*;

import nyla.solutions.core.exception.ConnectionException;

import nyla.solutions.core.data.*;
import nyla.solutions.core.util.Config;
import java.sql.*;

/**
 * @author Gregory Green
 * @version 1.0
 *
 * <b>Task</b>  represents an abstract task
 * 
 * <b>Arguments</b>
 * Task arguments begin with the character "-" 
 * 
 * Example 
 * -db.user smartAppUser -db.password smartAppPassword -TASK.SPECIFIC.ARGUMENT SOME_VALUE
 * 
 */
public abstract class  Task 
implements Runnable
{
   //Task properties
    public static final String TASK_DB_USER_NM_ARG  = "db.user";
    public static final String TASK_DB_USER_PWD_ARG = "db.password";
       
   /**
    * The input cammona dline arguments
    */
   public Task(String[] arg)
   {
      init(arg);
      
   }//----------------------------
   
   protected void checkDataAccess()
   {
      if(!this.arguments.keySet().contains(TASK_DB_USER_NM_ARG) ||
        !this.arguments.keySet().contains(TASK_DB_USER_PWD_ARG))
      {
         StringBuffer error = new StringBuffer("-")
            .append(TASK_DB_USER_NM_ARG).append(" ${user} ")
            .append("-")
            .append(TASK_DB_USER_PWD_ARG)
            .append(" ${password} ")
            .append("argumentExceptions required.");
            
         throw new IllegalArgumentException(error.toString()); 
      }
      
   }
   /**
    * Initialize the arguments into the map
    * Example -db.user smartAppUser -db.password smartAppPassword
    * @param arg the arguments
    */
   private void init(String[] arg)
   {
      if(arg == null || arg.length == 0)
         return;
      
      String name = "";
      String value = "";
      

      
      for(int i = 0; i < arg.length;i++)
      {
         if(! Data.isNull(arg[i]))
         {
            arg[i] = arg[i].trim();
           
            if(arg[i].startsWith("-"))
            {
               name = arg[i].substring(1,arg[i].length());
               
               //logger.debug("argument name="+name);
               
               i++;
               if(i < arg.length)
               {
                  if(!arg[i].startsWith("-"))
                  {
                     value = arg[i];
                     
                     //logger.debug("argument value="+value);
                     
                  }
                  else
                  {
                     i--;
                  }
               }
               
               arguments.put(name,value);
            }
         }  
      }
   }//------------------------------------
   /**
    * 
    * @param aArgumentName the argument name
    * @return the argument value
    */
   protected Object getArgument(String aArgumentName)
   {
      Object arg = arguments.get(aArgumentName);
      
      if(arg == null)
        throw new RuntimeException("Argument name "+aArgumentName+" not found");
      
        return arg;
   }//----------------------------------------------
   /**
    * 
    * @return singleton instance of TaskDAO
    */
   protected DAO getConnectionDAO()
   {
      if(dao == null)
        dao = new ConnectionDAO();
      
      return dao;
   }//----------------------------------------------
   /**
    * @author green_gregory
    * @version 1.0
    *
    * <b>ConnectionDAO</b>  
    * 
    */
   private class ConnectionDAO extends DAO
   {
      /**
       * @throws ConnectionException
       */
      public ConnectionDAO() throws ConnectionException
      {
         super();
      
         setConnection(acquireConnection());
      }
      /**
       * 
       * @return the connection object
       * @throws ConnectionException
       */
      private Connection acquireConnection()
      throws ConnectionException
      {
  
         try
         {
           /* Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection c = DriverManager.getConnection(
                           "jdbc:oracle:thin:@localhost:1521:orcl",
                           "sa.eo_user",
                           "sa.eo_user");
            */
             Class.forName(Config.getProperty("jdbc.driver"));
             Connection c = DriverManager.getConnection(
                            Config.getProperty("jdbc.url"),
                            (String)getArgument(TASK_DB_USER_NM_ARG),
                            (String)getArgument(TASK_DB_USER_PWD_ARG));                       
            return c;
         }
         catch (Exception e)
         {
            e.printStackTrace();
            throw new ConnectionException(e.toString());
         }
      }
   }//end TaskDAO
   
   private static DAO dao = null;
   private Map<Object,Object> arguments = new Hashtable<Object,Object>();
}
