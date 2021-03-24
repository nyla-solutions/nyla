package nyla.solutions.core.patterns.creational.proxy;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.PROXY;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Proxy for execute of method calls
 * @author Gregory Green
 *
 */
public class ObjectMethodProxy
{
	/**
	 * Constructor with object/method information
	 * @param target the target object
	 * @param methodName the method
	 * @throws NoSuchMethodException when method does not exits
	 */
	public ObjectMethodProxy(Object target, String methodName)
			throws NoSuchMethodException
	{
		this(target, methodName, null);
	}// ------------------------------------------------

	/**
	 * Constructor with object/method information
	 * @param target the target object
	 * @param methodName the method name
	 * @param parameterTypes the input method parameter types
	 * @throws NoSuchMethodException method does not exist
	 */
	public ObjectMethodProxy(Object target, String methodName,
			Class<?>[] parameterTypes) throws NoSuchMethodException
	{
		if (target == null)
			throw new RequiredException("target");

		this.target = target;

		this.targetClass = this.target.getClass();

		this.methodName = methodName;

		if(parameterTypes == null)
			this.parameterTypes = null;
		else
			this.parameterTypes = parameterTypes.clone();

		if (this.parameterTypes != null)
		{
			// find method
			this.method = targetClass.getDeclaredMethod(methodName,
					parameterTypes);
		}
	}// ------------------------------------------------

	/**
	 * Note that this methods does not support auto boxing of primitive types
	 * @param arguments the object method input arguments
	 * @return the return of the method call
	 * @throws Exception when an unknowns errors occurs
	 */
	public Object execute(Object[] arguments) throws Exception
	{
		try
		{
			if (this.method == null)
			{
				if (this.parameterTypes == null)
					this.parameterTypes = PROXY.toParameterTypes(arguments);

				this.method = PROXY.findMethod(this.targetClass,
						this.methodName, this.parameterTypes);
			}

			return this.method.invoke(target, arguments);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(target.getClass().getName()+"."+this.method+"("+Debugger.toString(e)+")");
		}
		catch (NoSuchMethodException e)
		{
			throw e;
		}
		catch (IllegalAccessException e)
		{
			throw e;
		}
		catch (InvocationTargetException e)
		{
			if (e.getCause() instanceof Exception)
				throw (Exception) e.getCause();
			else
				throw e;
		}

	}// --------------------------------------------

	public static Method findMethod(Class<?> objClass, String methodName,
			Class<?>[] parameterTypes) throws NoSuchMethodException
	{
		try
		{
			return objClass.getDeclaredMethod(methodName, parameterTypes);
		}
		catch (NoSuchMethodException e)
		{
			if (Object.class.equals(objClass))
				throw e;

			// try super
			return findMethod(objClass.getSuperclass(), methodName,
					parameterTypes);
		}

	}// ----------------------------------------------

	private Method method = null;
	private Class<?>[] parameterTypes = null;
	private String methodName = null;
	private Object target = null;
	private Class<?> targetClass = null;
}
