package nyla.solutions.core.exception;

import java.io.Serial;

/**
 * Represent an email error
 * @author gregory green
 * @version 1.0
 *
 */
public class EmailException extends CommunicationException 
{
	public static final String DEFAULT_ERROR_CODE = "EMAIL00";
	public static final String DEFAULT_EMAIL_ERROR_MSG = "Email Exception";

	/**
	 * 
	 */
	public EmailException() 
    {
		super(DEFAULT_EMAIL_ERROR_MSG);
		this.setCode(DEFAULT_ERROR_CODE);
	}

	/**
	 * @param message the email exception message
	 */
	public EmailException(String message)
    {
		super(message);
		this.setCode(DEFAULT_ERROR_CODE);
	}

    @Serial
    private static final long serialVersionUID = 1;
}
