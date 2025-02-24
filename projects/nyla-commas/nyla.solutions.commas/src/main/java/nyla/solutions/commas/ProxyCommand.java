package nyla.solutions.commas;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.creational.proxy.ObjectMethodProxy;

/**
 * Uses ObjectMethodProxy object to execute a method on a target object.
 * 
 * Usage: ProxyCommand cmd = new ProxyCommand();
		cmd.setTarget(this);
		
		cmd.setMethodName("tcase1");
		
		Object[] arg1 = {Boolean.valueOf(true), "HELLOW"};
		
		Assert.assertTrue(!tcase1Executed);
		Object r1 = cmd.execute(arg1);		
		
 * @author Gregory Green
 *
 */
public class ProxyCommand implements Command<Object,Object> 
{
	/**
	 * Execute the method of the target object uses the given argument
	 * @param argument the method input argument
	 * @return the return of the method invocation
	 */
	public Object execute(Object argument)
	{
		if (this.target == null)
			throw new RequiredException("this.target");
		
		
		
		try
		{
			if(objectMethodProxy == null)
			{
				objectMethodProxy = new ObjectMethodProxy(this.target, this.methodName);
			}
			
			if(argument instanceof Object[])
			{
				return  objectMethodProxy.execute((Object[])argument);
			}
			else
			{
				//convert to a single item array
				Object[] arguments = { argument};
				
				return objectMethodProxy.execute(arguments);
			}
		}
		catch (RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SystemException(e.getMessage(),e);
		}
	}// ------------------------------------------------
	
	/**
	 * @return the target
	 */
	public Object getTarget()
	{
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Object target)
	{
		this.target = target;
	}
	
	/**
	 * @return the methodName
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName)
	{
		if(this.methodName != null && !this.methodName.equals(methodName) && this.objectMethodProxy != null)
		{
			//method changing
			this.objectMethodProxy = null;
		}
		
		
		this.methodName = methodName;
	}

	private ObjectMethodProxy objectMethodProxy = null;
	private String methodName = null;
	private Object target = null;

}
