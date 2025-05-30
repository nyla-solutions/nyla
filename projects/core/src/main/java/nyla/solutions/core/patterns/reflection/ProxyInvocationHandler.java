package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.util.Debugger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvocationHandler implements InvocationHandler
{

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
		Debugger.println("proxy="+method);
		return null;
	}

}
