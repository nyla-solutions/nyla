package nyla.solutions.commas;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import nyla.solutions.core.exception.fault.ClassNotFoundFaultException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.reflection.BeanClassInfo;
import nyla.solutions.core.patterns.reflection.ClassSchema;
import nyla.solutions.core.patterns.reflection.ClassType;
import nyla.solutions.core.patterns.reflection.Mirror;

/**
 * This class contains catalog information.
 * 
 * @author Gregory Green
 * 
 */
public class CatalogClassInfo implements Serializable, BeanClassInfo
{
	/**
	 * 
	 * @param aClass the class to be wrapped
	 * @param genericType the generic type
	 */
	public CatalogClassInfo(Class<?> aClass, Type genericType)
	{
		this.classSchema = new ClassSchema(aClass);
		
		init(aClass,genericType);
	}// -----------------------------------------------

	/**
	 * 
	 * @param aClass the class to be wrapped
	 * @param genericTypes the generic types
	 */
	public CatalogClassInfo(Class<?> aClass, Type[] genericTypes)
	{
		this.classSchema = new ClassSchema(aClass);
		
		init(aClass,genericTypes);
	}// -----------------------------------------------	
	/**
	 * Initial the 
	 * @param aClass the class name
	 * @param genericType the generic type
	 */
	private void init(Class<?> aClass, Type genericType)
	{
		Type[] types = {genericType};
		init(aClass,types);
	}// -----------------------------------------------
	/**
	 * Initialize the catalog class information
	 * @param aClass the class
	 * @param genericTypes the generic types
	 */
	private void init(Class<?> aClass, Type[] genericTypes)
	{
		
		if(Mirror.isPrimitive(aClass))
		{
			this.classType = ClassType.primitive;
			return;
		}
		
		//check for array component type
		Class<?> arrayComponentTypeClass = aClass.getComponentType();
		
		if(arrayComponentTypeClass != null)
		{
			this.classType = ClassType.array;
			
			this.arrayComponentTypeClassName = arrayComponentTypeClass.getName();
			return;
		}
		

		
	    if(genericTypes == null || genericTypes.length == 0)
	    	return; //do not continue
	    
	    this.classType = ClassType.generic;
	        	
	    ArrayList<String> genericTypeClassNamesList = new ArrayList<String>();
	    Type genericType = null;
	    	
	    ParameterizedType aType = null;
	    Type[] parameterArgTypes = null;
	    		
	    //build generic types
	    for (int i = 0; i < genericTypes.length; i++)
		{
		   	genericType =genericTypes[i];
		   	
		   	//TODO if(AbstractCatalog.isHidden(genericType))
		   	//continue; //skip
		   	
		   	if(genericType instanceof ParameterizedType)
		   	{
		        aType = (ParameterizedType) genericType;
		        parameterArgTypes = aType.getActualTypeArguments();
		        for(Type parameterArgType : parameterArgTypes)
		        {			            
		        	genericTypeClassNamesList.add(formatTypeName(parameterArgType.toString()));
		        }
		    }
		   	else
		   	{
		   		//add basic (non-parameterized types)
		   		genericTypeClassNamesList.add(formatTypeName(genericType.toString()));
		   	}

		}//end for
	    
	    if(!genericTypeClassNamesList.isEmpty())
	    {
	    	this.genericTypeClassNames = new String[genericTypeClassNamesList.size()];
	    	genericTypeClassNames = (String[])genericTypeClassNamesList.toArray(this.genericTypeClassNames);
	    }
	    
	}// -----------------------------------------------
	/**
	 * 
	 * @param typeName the class prefix with a space the class type (object, interface, etc).
	 * @return
	 */
	private static String formatTypeName(String typeName)
	{
		if(typeName.contains(" "))
		{
			//replace type at beginning
			typeName = typeName.split(" ")[1];
		}
		return typeName;
	}// -----------------------------------------------

	//@Override
	public String getBeanClassName()
	{
		
		return this.classSchema.getObjectClassName();
	}// -----------------------------------------------
	/**
	 * Bean class
	 */
	//@Override
	public Class<?> getBeanClass() 
   throws ClassNotFoundFaultException
	{
		if(this.classSchema == null)
			return null;
		
		String className = this.classSchema.getObjectClassName();
		
		if(className == null
				|| className.length() == 0)
			return null;
		
		return ClassPath.toClass(className);
		
	}// -----------------------------------------------
	/**
	 * Return the class type. Note that the default type is object.
	 * @return the class type (object, array or generic)
	 */
	//@Override
	public ClassType getBeanClassType()
	{
		return classType;
	}// -----------------------------------------------
	
	/**
	 * If type is String[], this function will return 
	 * 
	 * @return null is this class is not an array. Otherwise return the class of the array
	 */
	//@Override
	public Class<?> getArrayComponentTypeClass()
	{
		if(this.arrayComponentTypeClassName == null ||
			this.arrayComponentTypeClassName.length() == 0)
			return null;
		
		return ClassPath.toClass(arrayComponentTypeClassName);
	}// -----------------------------------------------
	/**
	 * 
	 */
	//@Override
	public String getArrayComponentTypeClassName()
	{
		return arrayComponentTypeClassName;
	}// -----------------------------------------------
	/**
	 * 
	 */
	//@Override
	public String[] getGenericTypeClassNames()
	{
		if(genericTypeClassNames == null)
			return null;
		
		return genericTypeClassNames.clone();
	}// -----------------------------------------------
	/**
	 * @return the generic type classes for a given object
	 * @throws DafClassNotFoundException when when the class cannot be created
	 */
	//@Override
	public Class<?>[] getGenericTypeClasses()
	throws ClassNotFoundFaultException
	{
		if(this.genericTypeClassNames == null || this.genericTypeClassNames.length == 0)
			return null;
		
		Class<?>[] classes = new Class<?>[genericTypeClassNames.length];
		
		for (int i = 0; i < classes.length; i++)
		{
			classes[i] = ClassPath.toClass(genericTypeClassNames[i]);
		}
		
		return classes;
	}// -----------------------------------------------
	
	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "CatalogClassInfo [classType=" + classType + ", classSchema="
				+ classSchema + ", arrayComponentTypeClassName="
				+ arrayComponentTypeClassName + ", genericTypeClassNames="
				+ Arrays.toString(genericTypeClassNames) + "]";
	}



	/**
	 * @return the classType
	 */
	public ClassType getClassType()
	{
		return classType;
	}

	/**
	 * @param classType the classType to set
	 */
	public void setClassType(ClassType classType)
	{
		this.classType = classType;
	}

	/**
	 * @return the classSchema
	 */
	public ClassSchema getClassSchema()
	{
		return classSchema;
	}



	
	/**
	 * serialVersionUID = -6762005155834177508L
	 */
	private static final long serialVersionUID = -6762005155834177508L;

	private ClassType classType = ClassType.object;

	private final ClassSchema classSchema;
	private String arrayComponentTypeClassName = null;
	private String[] genericTypeClassNames;
}
