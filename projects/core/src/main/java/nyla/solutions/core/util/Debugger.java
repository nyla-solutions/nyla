package nyla.solutions.core.util;


import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.operations.logging.Log;
import nyla.solutions.core.operations.logging.SystemOutLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.*;

import static nyla.solutions.core.util.Config.settings;


/**
 * 
 * <pre>
 * Debugger provides useful methods for obtaining exception stack traces.
 * It can build reader friendly strings for objects 
 * that do not implement their toString method.
 * 
 * It also provides a set of print functions 
 * to log DEBUG, INFO, WARN and FATAL level messages using
 * the Debugger.println(...), Debugger.printInfo(...),
 * Debugger.printWarn(...) and Debugger.printFatal(...) methods respectively.
 * 
 * The default log object implementation is solutions.global.operations.Log4J.
 * 
 * Set the configuration property  to plug-in another logger (@see Config more information);
 * 
 * Log.class=className
 * 
 * The logClass class name indicated must implement the 
 * nyla.solutions.core.operations.Log interface.
 * 
 *
 * 
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.5
 */
public class Debugger
{
	//For println level messaging
	private static boolean DEBUG = settings().getPropertyBoolean(Debugger.class,"DEBUG",true).booleanValue();
	private static Log defaultLogger;

	
	/**
	 * LOG_CLASS_NAME_PROP = "Log.logClass"
	 */
	public static final String LOG_CLASS_NAME_PROP = "Log.class";

	private static Class<?> logClass;
	private static HashMap<Class<?>, Log> logMap = new HashMap<Class<?>, Log>();

	static
	{

		try
		{
			logClass = Class.forName(settings().getProperty(LOG_CLASS_NAME_PROP,
					SystemOutLog.class.getName()));
			         
			defaultLogger = (Log) ClassPath.newInstance(logClass);

		}
		catch(SetupException e)
		{
			throw e;
		}
		catch(ConfigException e)
		{
			throw e;
		}
		catch (RuntimeException e)
		{	
			throw new SetupException(e);

		}
		catch (NoClassDefFoundError e)
		{
			throw new SetupException("Check value of "+LOG_CLASS_NAME_PROP+" in confi file"+Config.getLocation(),e);
		}
		catch (ClassNotFoundException e)
		{
			throw new SetupException("Check value of "+LOG_CLASS_NAME_PROP+" in confi file"+Config.getLocation(),e);
		}

	}

	public static String toPrettyPrint(List<?> list) {

		if(list == null || list.isEmpty())
			return "";

		var builder = new StringBuilder();

		for (Object item : list){
			builder.append(item)
					.append(IO.newline());
		}

		return builder.toString();
	}


	/**
	 * Return an instance of the Log and calls
	 * setLoggingClass on the instance with the calling class
	 * @param c the calling class
	 * @return the logger implementation
	 */

	@SuppressWarnings("rawtypes")
	public static Log getLog(Class c)
	{

		try
		{
			if (c == null || logClass == null)
				return defaultLogger;

			// check cache
			Log logger = logMap.get(c);

			if (logger == null)
			{
				// create a put in map
				logger = (Log) ClassPath.newInstance(logClass);
				logger.setLoggingClass(c);
				logMap.put(c, logger);
			}

			return logger;

		}
		catch (Exception e)
		{

			e.printStackTrace();
			return defaultLogger;
		}
	}

	/**
	 * 
	 * convert throwable to a stack trace
	 * @param t the throwable
	 * @return the stack trace string
	 */

	public static String stackTrace(Throwable t)
	{
		if (t == null)
		{
			return "Debugger.stackTrace(null) NULL EXCEPTION (NO TRACE AVAILABLE)!!";
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * 
	 * @param obj input object
	 * @return the string representation
	 */
	public static String toString(Object obj)
	{
		return toString(obj, new HashSet<Object>());
	}

	/**
	 * Create an easy to read text of the given input object
	 * @param obj input object
	 * @return the string representation
	 */
	private static String toString(Object obj, Set<Object> set)
	{

		if (obj == null)
			return "null";

		if (set.contains(obj))
			return ""; // already printed

		set.add(obj);

		if (obj instanceof Object[])
		{

			Object[] objs = (Object[]) obj;

			StringBuilder arrayText = new StringBuilder("{");

			for (int i = 0; i < objs.length; i++)
			{

				arrayText.append("[" + i + "]=").append(Debugger.toString(objs[i], set));

				// append comma
				if (i + 1 != objs.length)
					arrayText.append(" ,");

			}// end for

			arrayText.append("}");

			return arrayText.toString();

		}

		if (obj instanceof String
				|| obj instanceof Integer
				|| obj instanceof StringBuilder)
			return obj.toString();

		Class<?> cl = obj.getClass();

		var textBuilder = new StringBuilder(cl.getName());

		// inspect the fields of this class and all superclasses
		do
		{

			textBuilder.append("[");
			Field[] fields = cl.getDeclaredFields();

			AccessibleObject.setAccessible(fields, true);
			// get the names and values of all fields

			for (int i = 0; i < fields.length; i++)
			{

				Field f = fields[i];
				textBuilder.append(f.getName()).append("=");

				try
				{
					Object val = f.get(obj);

					if (val != null)
					{

						if (val instanceof Object[])
						{

							Object[] objs = (Object[]) val;

							for (int c = 0; c < objs.length; c++)
							{
								textBuilder.append(Debugger.toString(objs[c], set));
							}

						}
						else
							textBuilder.append(val);

					}
					else
						textBuilder.append("null");

				}

				catch (Exception e)
				{
					e.printStackTrace();
				}

				if (i < fields.length - 1)
					textBuilder.append(",");
			}

			textBuilder.append("]");

			cl = cl.getSuperclass();

		}
		while (cl != Object.class);

		return textBuilder.toString();

	}

	/**
	 * 
	 * Uses reflection to the print all attributes of a given object
	 * @param obj the object to print
	 */
	public static void dump(Object obj)
	{
		println("DUMP:" + toString(obj));
	}

	/**
	 * 
	 * Print aObject information and the caller module for tracing *
	 * @param caller the calling object
	 * @param message the message/object to print 
	 * 
	 */
	public static void println(Object caller, Object message)
	{
		if (!DEBUG)
			return;
		
		var text = new StringBuilder();

		Class<?> c = callerBuilder(caller);

		if(message instanceof Throwable)
			getLog(c).debug(text.append(stackTrace((Throwable)message)));
		else
			getLog(c).debug(text.append(message));

	}
	/**
	 * Set a boolean to determine if Debugger.println messages will be printed.
	 * @param  debug to determine is print line message will be printed
	 */
	public static void debug(boolean debug)
	{
		DEBUG = debug;
	}

	/**
	 * Print a message using the configured Log
	 * @param message the message/print to print
	 */
	public static void println(Object message)
	{
		if (!DEBUG)
			return;
		
		defaultLogger.debug(message);

		
	}
	/**
	 * Print a error message. 
	 * The stack trace will be printed if
	 * the given message is an exception.
	 * @param caller the calling object
	 * @param message the message/object to print
	 */
	public static void printError(Object caller, Object message)
	{
		var text = new StringBuilder();

		Class<?> c = callerBuilder(caller);

		if(message instanceof Throwable)
			getLog(c).error(text.append(stackTrace((Throwable)message)));
		else
			getLog(c).error(text.append(message));

	}
	/**
	 * Print error message using the configured defaultLogger.
	 * The stack trace will be printed if
	 * the given message is an exception.
	 * @param errorMessage the error/object message
	 */
	public static void printError(Object errorMessage)
	{

		if (errorMessage instanceof Throwable)
		{

			var e = (Throwable) errorMessage;

			defaultLogger.error(stackTrace(e));

		}
		else
			defaultLogger.error(errorMessage);

	}
	/**
	 * Print a fatal level message.
	 * The stack trace will be printed if
	 * the given message is an exception.
	 * @param message the fatal message
	 */
	public static void printFatal(Object message)
	{

		if (message instanceof Throwable)
		{
			Throwable e = (Throwable) message;
			e.printStackTrace();
		}

		defaultLogger.fatal(message);

	}
	/**
	 * Print a fatal message.
	 * The stack trace will be printed if
	 * the given message is an exception.
	 * @param caller the calling object
	 * @param message the fatal message
	 */
	public static void printFatal(Object caller, Object message)
	{
		
		var text = new StringBuilder();

		Class<?> c = callerBuilder(caller);

		if(message instanceof Throwable)
			getLog(c).fatal(text.append(stackTrace((Throwable)message)));
		else
			getLog(c).fatal(text.append(message));

	}
	/**
	 * Print an INFO message
	 * @param caller the calling object
	 * @param message the INFO message
	 */
	public static void printInfo(Object caller, Object message)
	{
		
		var text = new StringBuilder();

		Class<?> c = callerBuilder(caller);

		if(message instanceof Throwable)
			getLog(c).info(text.append(stackTrace((Throwable)message)));
		else
			getLog(c).info(text.append(message));

	}
	/**
	 * Print a INFO level message
	 * @param message
	 */
	public static void printInfo(Object message)
	{
		
		if (message instanceof Throwable)
		{
			Throwable e = (Throwable) message;
			defaultLogger.info(stackTrace(e));
		}
		else
			defaultLogger.info(message);

	}
	/**
	 * Print a warning level message. 
	 * The stack trace will be printed if
	 * the given message is an exception.
	 * @param caller the calling object
	 * @param message the message to print
	 */
	public static void printWarn(Object caller, Object message)
	{
		var text = new StringBuilder();

		Class<?> c = callerBuilder(caller);

		if(message instanceof Throwable)
			getLog(c).warn(text.append(stackTrace((Throwable)message)));
		else
			getLog(c).warn(text.append(message));

	}

	private static Class<?> callerBuilder(Object caller)
	{
		if(caller == null)
			return null;

		if(caller instanceof Class<?> classInstance)
			return classInstance;

		return caller.getClass();
	}
	/**
	 * Print A WARN message. The stack trace will be printed if
	 * the given message is an exception.
	 * @param message the message to stack
	 */
	public static void printWarn(Object message)
	{

		if (message instanceof Throwable)
		{
			var e = (Throwable) message;
			defaultLogger.warn(stackTrace(e));

		}

		else

			defaultLogger.warn(message);

	}
	public static void println(Object caller, String format, Object... args)
	{
		Object msg = String.format(format, args);
		
		println(caller,msg);
	}
	public static void printInfo(Object caller, String format, Object... args)
	{
		Object msg = String.format(format, args);
		
		printInfo(caller,msg);
	}
	public static void printWarn(Object caller, String format, Object... args)
	{
		Object msg = String.format(format, args);
		
		printWarn(caller,msg);
	}
	public static void printError(Object caller, String format, Object... args)
	{
		Object msg = String.format(format, args);
		
		printError(caller,msg);
	}
	public static void printFatal(Object caller, String format, Object... args)
	{
		Object msg = String.format(format, args);
		
		printFatal(caller,msg);
	}

}
