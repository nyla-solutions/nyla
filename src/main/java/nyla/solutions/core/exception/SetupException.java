package nyla.solutions.core.exception;

/**

 * <pre>

 * SetupException exception occurred setup initialization error

 * </pre> 

 * @author Gregory Green

 * @version 1.0

 */

public class SetupException extends ConfigException

{
	public static final String DEFAULT_ERROR_CODE = "SUP00";
	public static final String DEFAULT_ERROR_CATEGORY = "SETUP";


   /**
    * Constructor for SetupException initializes internal 
    * data settings.
    * 
    */
   public SetupException()
   {
      super("Set up exception");
      this.setCode(DEFAULT_ERROR_CODE);
      this.setCategory(DEFAULT_ERROR_CATEGORY);
   }//--------------------------------------------

   /**
    * Constructor for SetupException initializes internal 
    * data settings.
    * @param aMessage
    */
   public SetupException(String aMessage)
   {

      super(aMessage);
      
      
      this.setCode(DEFAULT_ERROR_CODE);
      this.setCategory(DEFAULT_ERROR_CATEGORY);

   }//--------------------------------------------



   /**
    * Constructor for SetupException initializes internal 
    * data settings.
    * @param aThrowable
    */
   public SetupException(Throwable aThrowable)
   {

      super(aThrowable);

      this.setCode(DEFAULT_ERROR_CODE);
      this.setCategory(DEFAULT_ERROR_CATEGORY);
   }//--------------------------------------------



   /**
    * Constructor for SetupException initializes internal 
    * data settings.
    * @param aMessage
    * @param aThrowable
    */
   public SetupException(String aMessage, Throwable aThrowable)
   {

      super(aMessage, aThrowable);
      
      this.setCode(DEFAULT_ERROR_CODE);
      this.setCategory(DEFAULT_ERROR_CATEGORY);

   }//--------------------------------------------
   
   static final long serialVersionUID = SetupException.class.getName()
   .hashCode();
}
