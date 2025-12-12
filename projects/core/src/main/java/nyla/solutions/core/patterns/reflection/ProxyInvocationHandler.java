package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.util.PROXY;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation handler for dynamic proxy
 * @author Gregory Green
 */
public class ProxyInvocationHandler implements InvocationHandler
{
    private final Object serviceImplementation;

    /**
     * Constructor for ProxyInvocationHandler
     * @param serviceImplementation the service implementation
     * @param <ImplementType> the type of the service implementation
     */
    public <ImplementType> ProxyInvocationHandler(ImplementType serviceImplementation) {
        this.serviceImplementation = serviceImplementation;
    }

    /**
     * Invoke method on proxy
     * @param proxy the proxy object
     * @param method the method to invoke
     * @param args the method arguments
     * @return  Method results
     * @throws Throwable if an error occurs
     */
    public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable
	{
        return PROXY.executeMethod(serviceImplementation,method.getName(),args);
	}
}
