package nyla.solutions.core.patterns.jmx;

/**
 * JMX security exception
 * @author Gregory Green
 *
 */
public class JMXConnectionException extends RuntimeException
{

	public JMXConnectionException()
	{
		super();
	}

	public JMXConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public JMXConnectionException(String message)
	{
		super(message);
	}

	public JMXConnectionException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5555257720849127282L;

}
