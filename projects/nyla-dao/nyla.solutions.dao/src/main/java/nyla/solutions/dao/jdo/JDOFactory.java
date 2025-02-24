package nyla.solutions.dao.jdo;

import nyla.solutions.core.exception.ConnectionException;
import nyla.solutions.core.util.*;

import javax.jdo.PersistenceManager;



/**

 * 

 * <pre>

 * JDOFactory is a data access object factory object for all JDO based DAO.

 * </pre> 

 * @author Gregory Green

 * @version 1.0

 */



public abstract class JDOFactory

{



    public JDOFactory()

    {

    }



    public static JDOFactory getInstance()

    throws ConnectionException

    {

        try
      {
         if(instance == null)

               instance = (JDOFactory)Class.forName(jdoFactoryName).newInstance();

           return instance;
      }
    
      catch (Exception e)
      {
         throw new ConnectionException(Debugger.stackTrace(e));
         
      }

    }//--------------------------------------------

    /**

     * 

     * @param aJDODAO

     * @return

     * @throws DatabaseException

     */

    public abstract PersistenceManager createPersistenceManager(JDODAO aJDODAO)

        throws ConnectionException;

    

    /**

     * 

     * @param aClass the Value object class

     * @param aJDODAO the JDO DAO implementation

     * @return

     */

    public abstract JDOQueryBuilder createQueryBuilder(Class<?> aClass, JDODAO aJDODAO);

    

    private static JDOFactory instance = null;
    private static String jdoFactoryName = "NA";

}

