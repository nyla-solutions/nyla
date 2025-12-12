package nyla.solutions.core.data;

import nyla.solutions.core.util.Organizer;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author Gregory Green
 */
public class MethodCallFact implements Serializable
{
   /**
    * serialVersionUID = -5541061978058350647L
    */
   private static final long serialVersionUID = -5541061978058350647L;
   
   /**
    * default constructor
    */
   public MethodCallFact()
   {}
   /**
    * Construct with data
    * @param callerName
    * @param methodName
    * @param arguments
    */
   public MethodCallFact(String callerName, String methodName, 
	   Object[] arguments)
   {
	super();
	this.methodName = methodName;
	this.callerName = callerName;
	
	if( arguments != null)
		this.arguments = arguments.clone();
   }// ----------------------------------------------
   /**
    * @return the methodName
    */
   public String getMethodName()
   {
      return methodName;
   }// ----------------------------------------------
   /**
    * @param methodName the methodName to set
    */
   public void setMethodName(String methodName)
   {
      this.methodName = methodName;
   }// ----------------------------------------------
   /**
    * @return the callerName
    */
   public String getCallerName()
   {
      return callerName;
   }// ----------------------------------------------
   /**
    * @param callerName the callerName to set
    */
   public void setCallerName(String callerName)
   {
      this.callerName = callerName;
   }// ----------------------------------------------
   /**
    * @return the arguments
    */
   public  Object[] getArguments()
   {
	   if(arguments == null)
		   return null;
	   
      return Organizer.copies().copy(arguments);
   }// ----------------------------------------------
   /**
    * @param arguments the arguments to set
    */
   public <T> void setArguments(T... arguments)
   {
	   if(arguments == null)
		   this.arguments = null;
	   else
		   this.arguments = arguments.clone();
   }// ----------------------------------------------
   /**
    * @return this.getClass().getName()+" callerName="+callerName+" methodName="+methodName
    */
   public String toString()
   {	
      return this.getClass().getName()+" callerName="+callerName+" methodName="+methodName;	
   }// ----------------------------------------------
   public int hashCode()
   {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(arguments);
	result = prime * result
		+ ((callerName == null) ? 0 : callerName.hashCode());
	result = prime * result
		+ ((methodName == null) ? 0 : methodName.hashCode());
	return result;
   }// ----------------------------------------------
   public boolean equals(Object obj)
   {
	if (this == obj)
	   return true;
	if (obj == null)
	   return false;
	if (getClass() != obj.getClass())
	   return false;
	MethodCallFact other = (MethodCallFact) obj;
	if (!Arrays.equals(arguments, other.arguments))
	   return false;
	if (callerName == null)
	{
	   if (other.callerName != null)
		return false;
	}
	else if (!callerName.equals(other.callerName))
	   return false;
	if (methodName == null)
	{
	   if (other.methodName != null)
		return false;
	}
	else if (!methodName.equals(other.methodName))
	   return false;
	return true;
   }

   private String methodName = null;
   private String callerName = null;
   private Object[] arguments = null;
}
