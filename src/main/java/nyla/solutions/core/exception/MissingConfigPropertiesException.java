package nyla.solutions.core.exception;


import nyla.solutions.core.io.IO;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.util.Config;


public class MissingConfigPropertiesException extends ConfigException
{

   /**
	 * 
	 */
	private static final long serialVersionUID = 5689827263132067133L;

	/**
    * Constructor for SetupException initializes internal 
    * data settings.
	 * @param key the key that is missing
    * 
    */
   public MissingConfigPropertiesException(String key)
   {
	   super("Missing configuration property key:"+key+ " in environment variables, JVM system properties and property file "
			   + Config.RESOURCE_BUNDLE_NAME+".properties  in classpath:"+ClassPath.getClassPathText()
			   +" "+ IO.newline()
			   +" or call Config.loadArgs(String[] args) to load properties from input arguments"
	           +" Ex 2 args: --prop1=1 --prop2=2 ");

   }//--------------------------------------------

	   

}
