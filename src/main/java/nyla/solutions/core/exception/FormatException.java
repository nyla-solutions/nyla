package nyla.solutions.core.exception;

/**
 * <pre>
 * FormatException exception related for parsing or formatting data.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class FormatException extends SystemException
{
	public static final String DEFAULT_ERROR_CODE = "FORMAT01";
	
   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = -4907281152649411791L;

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    */
   public FormatException()
   {
      super();
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    * @param aMesssage
    */
   public FormatException(String aMesssage)
   {
      super(aMesssage);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    * @param arg0
    * @param aThrowable
    */
   public FormatException(String arg0, Throwable aThrowable)
   {
      super(arg0, aThrowable);
      this.setCode(DEFAULT_ERROR_CODE);
   }

   /**
    * Constructor for FormatException initializes internal 
    * data settings.
    * @param aThrowable
    */
   public FormatException(Throwable aThrowable)
   {
      super(aThrowable);
      this.setCode(DEFAULT_ERROR_CODE);
   }


}
