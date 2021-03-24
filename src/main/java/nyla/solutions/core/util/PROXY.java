package nyla.solutions.core.util;

import nyla.solutions.core.exception.RequiredException;

import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * <pre>
 * PROXY provides a set of functions to execute an operation on an object
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class PROXY
{
	public static Object executeMethod(Object aObject, String aMethodName,
			Object[] aArguments) throws Exception
	{
		if (aObject == null)
			throw new RequiredException("Input object");

		// get array of inputs

		Class<?>[] parameterTypes = null;

		ArrayList<Object> parameterTypeArrayList = null;
		if (aArguments != null)
		{
			parameterTypeArrayList = new ArrayList<Object>(aArguments.length);
			for (int i = 0; i < aArguments.length; i++)
			{
				if (aArguments[i] == null)
					continue;

				parameterTypeArrayList.add(aArguments[i].getClass());
			}

			parameterTypes = (Class[]) parameterTypeArrayList
					.toArray(new Class[parameterTypeArrayList.size()]);
		}

		// find method
		Method method = aObject.getClass().getDeclaredMethod(aMethodName,
				parameterTypes);

		return method.invoke(aObject, aArguments);
	}// --------------------------------------------

	/**
	 * Find the method for a target of its parent
	 * @param objClass the object class
	 * @param methodName the method name
	 * @param parameterTypes the method parameter types
	 * @return the method
	 * @throws NoSuchMethodException
	 */
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

			try
			{
				// try super
				return findMethod(objClass.getSuperclass(), methodName,
						parameterTypes);
				
			}
			catch(NoSuchMethodException parentException)
			{
				throw e;
			}
		}

	}// ----------------------------------------------

	 public static Method  findMethodByArguments(Class<?> targetClass, String methodName, Object[] arguments)
	 throws NoSuchMethodException
	 {

			if(arguments == null || arguments.length == 0)
			{
				return targetClass.getDeclaredMethod(methodName,(Class<?>)null);
			}
			
			Class<?>[] parameterTypes = toParameterTypes(arguments);
			
			return targetClass.getDeclaredMethod(methodName,parameterTypes);
	}// ------------------------------------------------

	public static Class<?>[] toParameterTypes(Object argument)
	{
		if(!(argument instanceof Object[]))
		{
			Class<?>[] classArgs = {argument.getClass()};
			
			return classArgs;
		}
		
		Object[] arguments = (Object[])argument;
		
		int len = arguments.length;
		
		Class<?>[] parameterTypes = new Class[len];
		
		//get array of inputs

		for (int i = 0; i < len; i++)
		{

			parameterTypes[i] = arguments[i].getClass();   
		}
		return parameterTypes;
	}// -----------------------------------------------
}
