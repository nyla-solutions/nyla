package nyla.solutions.core.exception;

import java.util.ArrayList;
import java.util.Collection;


/**
 * <pre>
 * SummaryException represents one or more exceptions
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class SummaryException extends SystemException
{
    private Collection<Exception> summary = new ArrayList<Exception>();
    static final long serialVersionUID = SummaryException.class.getName().hashCode();

   /**
    * Constructor for ExceptionSummary initializes internal 
    * data settings.
    * 
    */
   public SummaryException()
   {
      super();
   }

   /**
    * Constructor for ExceptionSummary initializes internal 
    * data settings.
    * @param aMessage the message 
    */
   public SummaryException(String aMessage)
   {
      super(aMessage);
   }

   /**
    * Constructor for ExceptionSummary initializes internal 
    * data settings.
    * @param throwable the nest exception
    */
   public SummaryException(Throwable throwable)
   {
      super(throwable);
   }

   /**
    * Constructor for ExceptionSummary initializes internal 
    * data settings.
    * @param aMessage
    * @param aNestedException
    */
   public SummaryException(String aMessage, Throwable aNestedException)
   {
      super(aMessage, aNestedException);
   }

   /**
    * @return Returns the summary.
    */
   public Collection<Exception> getSummary()
   {
      return summary;
   }

   /**
    * @param summary The summary to set.
    */
   public void setSummary(Collection<Exception> summary)
   {
      this.summary = summary;
   }

   /**
    * Add exception to summary
    * @param aException the exception the add
    */
   public void addException(Exception aException)
   {
      summary.add(aException);
   }

   /**
    * 
    * @see java.lang.Throwable#toString()
    */
   public String toString()
   {
    
      return super.toString()+" summary="+summary;
   }

   /**
	 * @return true if empty
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty()
	{
		return summary != null && summary.isEmpty();
	}

	/**
	 * @return size
	 * @see java.util.Collection#size()
	 */
	public int size()
	{
		if( summary == null)
			return 0;
		
		return summary.size();
	}


}
