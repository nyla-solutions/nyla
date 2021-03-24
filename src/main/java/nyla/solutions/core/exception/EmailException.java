package nyla.solutions.core.exception;

/**
 * Represent an email error
 *
 */
public class EmailException extends CommunicationException 
{
	public static final String DEFAULT_ERROR_CODE = "EMAIL00";

	/**
	 * 
	 */
	public EmailException() 
    {
		super("Email Exception");
		this.setCode(DEFAULT_ERROR_CODE);
	}// --------------------------------------------
	/**
	 * @param arg0
	 */
	public EmailException(String arg0) 
    {
		super(arg0);
		this.setCode(DEFAULT_ERROR_CODE);
	}// --------------------------------------------
    static final long serialVersionUID = 1;
}
