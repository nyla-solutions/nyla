package nyla.solutions.core.operations.logging;

/**
 * Interface to logging messages
 * @author Gregory Green
 *
 */
public interface Log
{
	/**
	 * 
	 * @param aClass the class of the logger
	 */
	public void setLoggingClass(Class<?> aClass);
	
	
	/**
	 * Print a warn level
	 * @param message the message to print
	 */
	public void debug(Object message);
	
	/**
	 * Print a debug level
	 * @param message the message to print
	 * @param t a thrown exception
	 */
	public void debug(Object message, Throwable t);
	
	
	/**
	 * Print a warn level
	 * @param message the message to print
	 */
	public void info(Object message);
	
	/**
	 * Print a debug level
	 * @param message the message to print
	 * @param t a thrown exception
	 */
	public void info(Object message, Throwable t);
	/**
	 * Print a error level
	 * @param message the message to print
	 */
	public void error(Object message);
	
	/**
	 * Print a error level
	 * @param message the message to print
	 * @param t a thrown exception
	 */
	public void error(Object message, Throwable t);
	
	/**
	 * Print a log level
	 * @param message the message to print
	 */
	public void fatal(Object message);
	
	/**
	 * Print a fatal level
	 * @param message the message to print
	 * @param t a thrown exception
	 */
	public void fatal(Object message, Throwable t);
	
	/**
	 * Print a warn level
	 * @param message the message to print
	 */
	public void warn(Object message);
	
	/**
	 * Print a warn level
	 * @param message the message to print
	 * @param t a thrown exception
	 */
	public void warn(Object message, Throwable t);
	
	
}
