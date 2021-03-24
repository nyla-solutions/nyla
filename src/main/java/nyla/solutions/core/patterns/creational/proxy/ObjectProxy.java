package nyla.solutions.core.patterns.creational.proxy;

import nyla.solutions.core.data.MethodCallFact;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;

import java.lang.reflect.Method;
import java.util.ArrayList;




public class ObjectProxy
{
   /**
    * Execute the method call on a object
    * @param object the target object that contains the method
    * @param methodCallFact the method call information
    * @return the return object of the method call
    * @throws Exception
    */
   public static Object executeMethod(Object object, MethodCallFact methodCallFact)
   throws Exception
   {
	if (object == null)
	   throw new RequiredException("object");
	
	if (methodCallFact == null)
	   throw new RequiredException("methodCallFact");
	
	return executeMethod(object, methodCallFact.getMethodName(),methodCallFact.getArguments());
   }// ----------------------------------------------
   /**
    * Note that this methods does not support auto boxing of primitive types
    * @param aObject the object
    * @param methodName the method
    * @param aArguments the input argument
    * @return the object return values
    * @throws Exception
    */
   public static Object executeMethod(Object aObject, String methodName, Object[] aArguments)
   throws Exception
   {
	Class<?>[] parameterTypes = null;
	ArrayList<Object> parameterTypeArrayList = null;
	
	if(aArguments != null && aArguments.length >0)
	{
	   
	   //get array of inputs
	   
	     parameterTypeArrayList = new ArrayList<Object>(aArguments.length);
	         
	     for (int i = 0; i < aArguments.length; i++)
	     {
		  if(aArguments[i] == null)
		  {
		     //TODO: throw new RequiredException("argument["+i+"]");
		     parameterTypeArrayList.add(Object.class);
		  }
		  else
		  {
		     parameterTypeArrayList.add(aArguments[i].getClass());   
		  }
		  
		  
	     }
	     	     
	      parameterTypes = (Class[])parameterTypeArrayList.toArray(new Class[parameterTypeArrayList.size()]);  
	}
      
     
      
      //find method
      Method method = null;
      

      method = aObject.getClass().getDeclaredMethod(methodName, parameterTypes);
     
      try
	{
         return method.invoke(aObject, aArguments);
	} 
      catch (Exception e)
	{          
	   throw new SystemException("methodName="+methodName+" object="+aObject.getClass().getName()+" parameters="+parameterTypeArrayList+" EXCEPTION="+Debugger.stackTrace(e));
	}
      
   }// --------------------------------------------

  
}
