package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodSchema implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8831063503559316388L;
	
	
	public MethodSchema(Method method)
	{
		this.name = method.getName();
		
	     Class<?>[] parameterTypes = method.getParameterTypes();
	     
	     inputArguments = new TypeSchema[parameterTypes.length];
	     Class<?> inputArgClass;
	     
	     for (int i = 0; i < parameterTypes.length; i++)
		 {
	    	 inputArgClass = parameterTypes[i];
	    	 
	    	 if(Mirror.isPrimitive(inputArgClass))
	    		 inputArguments[i] = new PrimitiveTypeSchema(argName+i,inputArgClass);
	    	 else
	    		 inputArguments[i] = new ComplexTypeSchema(argName+i,inputArgClass);
		 }
		
	     Class<?> returnClass = method.getReturnType();
	     if(Mirror.isPrimitive(returnClass))
	    	 this.returnClassSchema = new PrimitiveTypeSchema(returnClass);
	     else
	    	 this.returnClassSchema = new ComplexTypeSchema(returnClass);
	}// -----------------------------------------------
	
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "MethodSchema [name="
				+ name
				+ ", inputArguments="
				+ (inputArguments != null ? Arrays.asList(inputArguments)
						: null) + ", returnClassSchema=" + returnClassSchema
				+ "]";
	}// -----------------------------------------------


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(inputArguments);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((returnClassSchema == null) ? 0 : returnClassSchema
						.hashCode());
		return result;
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodSchema other = (MethodSchema) obj;
		if (!Arrays.equals(inputArguments, other.inputArguments))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (returnClassSchema == null)
		{
			if (other.returnClassSchema != null)
				return false;
		}
		else if (!returnClassSchema.equals(other.returnClassSchema))
			return false;
		return true;
	}


	private final String name;
	private TypeSchema[] inputArguments;
	private TypeSchema returnClassSchema;
	private static String argName = "arg";
}
