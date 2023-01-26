package nyla.solutions.core.exception.fault;

import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Presenter;

import java.io.PrintWriter;
import java.io.Serial;
import java.io.StringWriter;
import java.util.Map;



public class FaultException extends RuntimeException implements Fault
{

	/**
	 * serialVersionUID = -8500125749384413080L
	 */
	@Serial
	private static final long serialVersionUID = -8500125749384413080L;
	
	
	public static final String DEFAULT_ERROR_CATEGORY_NM = "DEFAULT";
	
	public FaultException(String message, String notes, String programName, String functionName,
			String errorCategory, String errorCode)
	{
		super(message);
		this.notes = notes;
		this.module = programName;
		this.operation = functionName;
		this.category = errorCategory;
		this.code = errorCode;
	}

	public FaultException()
	{
		super();
	}// -----------------------------------------------

	public FaultException(String message, Throwable cause)
	{
		super(message, cause);
		
	}// -----------------------------------------------

	public FaultException(String message)
	{
		super(message);
		
		this.notes = message;
	}// -----------------------------------------------

	public FaultException(Throwable cause)
	{
		super(cause);
		
	}// -----------------------------------------------
	public FaultException(String message, String functionName, String errorCategory,
			String errorCode, String programName)
	{
		super(message);
		this.operation = functionName;
		this.category = errorCategory;
		this.code = errorCode;
		this.module = programName;
		this.notes = message;
	}// -----------------------------------------------
	public FaultException(String message, Throwable cause, String functionName, String errorCategory,
			String errorCode, String programName)
	{
		super(message, cause);
		this.operation = functionName;
		this.category = errorCategory;
		this.code = errorCode;
		this.module = programName;
	}// -----------------------------------------------
	public FaultException(Throwable cause, String functionName, String errorCategory,
			String errorCode, String programName)
	{
		super(cause);
		this.operation = functionName;
		this.category = errorCategory;
		this.code = errorCode;
		this.module = programName;
	}// -----------------------------------------------

	/**
	 * 
	 * @return the exception stack trace
	 */
	public String stackTrace()
	{
		return stackTrace(this);
	}// -----------------------------------------------
	/**
	 * 
	 * @param e the root exception
	 * @return the exception stack trace
	 */
	public static String stackTrace(Throwable e)
	{
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    e.printStackTrace(pw);
	      
	    return sw.toString();
		
	}// -----------------------------------------------
	
	/* (non-Javadoc)
	 * @see solutions.gedi.exception.GediFault#getFunctionName()
	 */
	//@Override
	public String getOperation()
	{
		return operation;
	}// -----------------------------------------------

	/**
	 * @param operation the functionName to set
	 */
	public void setOperation(String operation)
	{
		this.operation = operation;
	}// -----------------------------------------------

	/* (non-Javadoc)
	 * @see solutions.gedi.exception.GediFault#getErrorCategory()
	 */
	//@Override
	public String getCategory()
	{
		return category;
	}// -----------------------------------------------


	/* (non-Javadoc)
	 * @see solutions.gedi.exception.GediFault#getErrorCode()
	 */
	//@Override
	public String getCode()
	{
		return code;
	}// -----------------------------------------------



	

	/* (non-Javadoc)
	 * @see solutions.gedi.exception.GediFault#getProgramName()
	 */
	//@Override
	public String getModule()
	{
		return module;
	}

	/**
	 * @param module the programName to set
	 */
	public void setModule(String module)
	{
		this.module = module;
	}

	/**
	 * @param errorCategory the errorCategory to set
	 */
	public void setCategory(String errorCategory)
	{
		this.category = errorCategory;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setCode(String errorCode)
	{
		this.code = errorCode;
	}
	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}
	public void copy(Object object)
	{
		FaultException other = (FaultException)object;
		
		this.notes = other.notes;
		this.module = other.module;
		this.operation = other.operation;
		this.category = other.category;
		this.code = other.code;
		this.argument = other.argument;
		
		this.setMessage(other.getMessage());
		this.setStackTrace(other.getStackTrace());
	}// --------------------------------------------------------
	
	 /**
	    * Format the exception message using the presenter getText method
	    * @param aID the key of the message
	    * @param aBindValues the values to plug into the message.
	    */
	   protected void formatMessage(String aID, Map<?,?> aBindValues)
	   {
	      Presenter presenter = Presenter.getPresenter(this.getClass());
	       
	       message = presenter.getText(aID,aBindValues);
	   }// --------------------------------------------
	   /**
	    * 
	    * @param aMessage the message to set
	    */
	   protected void setMessage(String aMessage)
	   {
	      this.message = aMessage;
	      
	   }// --------------------------------------------
	/**
	 * @return the argument
	 */
	public Object getArgument()
	{
		return argument;
	}

	/**
	 * @param argument the argument to set
	 */
	public void setArgument(Object argument)
	{
		this.argument = argument;
	}
    /**
    * 
    * @see java.lang.Throwable#getMessage()
    */
   public String getMessage()
   {
      if(message != null && message.length() > 0)
      {
         return message;
      }
      
      return super.getMessage();
      
   }// --------------------------------------------
   /**
    * @param aID the key in the exception's properties files
    * @param aMessage the message
    */
	 public FaultException(String aID, String aMessage)
	 {
	    Presenter presenter = Presenter.getPresenter(this.getClass());
	    
	    message = new StringBuilder(presenter.getText(aID)).append(" ").append(aMessage).toString();
	 }//---------------------------------------------
	 
	 public String getErrorStackTrace()
	 {
		 return Debugger.stackTrace(this);
	 }// --------------------------------------------------------
	 

	/**
	 * @return the argumentId
	 */
	public String getArgumentId()
	{
		return argumentId;
	}

	/**
	 * @param argumentId the argumentId to set
	 */
	public void setArgumentId(String argumentId)
	{
		this.argumentId = argumentId;
	}

	/**
	 * @return the argumentType
	 */
	public String getArgumentType()
	{
		return argumentType;
	}

	/**
	 * @param argumentType the argumentType to set
	 */
	public void setArgumentType(String argumentType)
	{
		this.argumentType = argumentType;
	}



	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source)
	{
		this.source = source;
	}


	private String source = null;

	private Object argument;
	private String notes = null;
	private String module = null;
	private String operation = null;
	private String category = null;
	private String code = null;
	private String argumentId = null;
	private String argumentType = null;
    private String message = null;
}
