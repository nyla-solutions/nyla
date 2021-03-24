package nyla.solutions.core.patterns.jmx;

/**
 * JMX security exception
 * @author Gregory Green
 *
 */
public class JMXSecurityException extends RuntimeException
{

	public JMXSecurityException()
	{
		super();
	}

	public JMXSecurityException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JMXSecurityException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public JMXSecurityException(String message)
	{
		super(message);
	}

	public JMXSecurityException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5555257720849127282L;

}
