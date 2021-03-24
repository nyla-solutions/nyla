package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.exception.fault.ClassNotFoundFaultException;

import java.io.Serializable;



/**
 * This interface represents Class information like class names, 
 * generic types parameters and array component types.
 * @author Gregory Green
 *
 */
public interface BeanClassInfo extends Serializable
{
	/**
	 * The name of the class.
	 * 
	 * For a Map&lt;String,DemoDafTheray&gt; type this value will be
	 * beanClassName=java.util.Map
	 * 
	 * @return the class name of the input 
	 */
	public String getBeanClassName();
	
	/**
	 * The class of the object.
	 * 	For a Map&lt;String,DemoDafTheray&gt; type
	 * this value will be class=java.util.Map.class
	 * @return the object class
	 * @throws ClassNotFoundFaultException when the input class cannot be found in the CLASSPATH
	 */
	public Class<?> getBeanClass()
	throws ClassNotFoundFaultException;
	

	/**
	 * The type of object (object, array or generic).
	 * The object type is the default.
	 * @return ClassType.object, ClassType.array or ClassType.generic
	 */
	public ClassType getBeanClassType();
	
	/**
	 * Returns the Class representing the component type of an array. 
	 * If this class does not represent an array class this method returns null.
	 * If type is String[], then this value will be = String.class
	 * 
	 * @return the class of the array type
	 */
	public Class<?> getArrayComponentTypeClass();
	
	
	/**
	 * Returns the class name representing the component type of an array. 
	 * If this class does not represent an array class this method returns null.
	 * If type is String[], then this value will be "java.lang.String"
	 * @return the class name of the array type
	 */
	public String getArrayComponentTypeClassName();
	
	/**
	 * Returns an array of String that represent the type class name
	 * variables declared by the generic declaration represented 
	 * by this object, in the declaration order.
	 * 
	 * For a Map&lt;String,DemoDafTheray&gt; type
	 * 
	 * genericTypeClassNames=[java.lang.String, some.ObjectClass
	 * 
	 * @return of the types name for generic objects
	 */
	public String[] getGenericTypeClassNames();
	
	/**
	 * Returns an array of Classes that represent the type 
	 * variables declared by the generic declaration represented 
	 * by this object, in the declaration order.
	 * 
	 * For a Map&lt;String,DemoDafTheray&gt; type
	 * 
	 * genericTypeClasses=[String.class, some.ObjectClass]
	 * @return type names for generic objects
	 */
	public Class<?>[] getGenericTypeClasses();
	
}
