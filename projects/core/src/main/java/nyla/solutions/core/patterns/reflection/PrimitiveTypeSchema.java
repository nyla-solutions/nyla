package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Meta-data information for a class field
 * @author Gregory Green
 *
 */
public class PrimitiveTypeSchema implements Serializable, TypeSchema
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8221193500710478919L;
	
	
	public PrimitiveTypeSchema(Field field)
	{
		this(field.getName(), field.getType());
	}// -----------------------------------------------
	public PrimitiveTypeSchema(Class<?> aClass)
	{
		this.className = aClass.getName();
		
		this.fieldClass = aClass;
		
	}// -----------------------------------------------	
	public PrimitiveTypeSchema(String fieldName, Class<?> aClass)
	{
		this.setFieldName(fieldName);
		
		this.fieldClass = aClass;
		
		this.className = aClass.getName();
	}// -----------------------------------------------	
	/**
	 * @see nyla.solutions.core.patterns.reflection.TypeSchema#getFieldName()
	 */
	public String getFieldName()
	{
		return fieldName;
	}// -----------------------------------------------
	/* (non-Javadoc)
	 * @see solutions.global.patterns.reflection.TypeSchema#setFieldName(java.lang.String)
	 */
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}// -----------------------------------------------
	
	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}// -----------------------------------------------

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "PrimitiveTypeSchema [ fieldName="
				+ fieldName + ", className=" + className + "]";
	}
	/**
	 * @return ClassType.primitive
	 */
	public ClassType getClassType()
	{
		return ClassType.primitive;
	}
	
	/**
	 * @return the fieldClass
	 */
	public Class<?> getFieldClass()
	{
		return fieldClass;
	}
	/**
	 * @param fieldClass the fieldClass to set
	 */
	public void setFieldClass(Class<?> fieldClass)
	{
		this.fieldClass = fieldClass;
	}
	/**
	 * @return null
	 * @see nyla.solutions.core.patterns.reflection.TypeSchema#getFieldsTypes()
	 */
	public TypeSchema[] getFieldsTypes()
	{
		return null;
	}
	
	

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((fieldClass == null) ? 0 : fieldClass.hashCode());
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
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
		PrimitiveTypeSchema other = (PrimitiveTypeSchema) obj;
		if (className == null)
		{
			if (other.className != null)
				return false;
		}
		else if (!className.equals(other.className))
			return false;
		if (fieldClass == null)
		{
			if (other.fieldClass != null)
				return false;
		}
		else if (!fieldClass.equals(other.fieldClass))
			return false;
		if (fieldName == null)
		{
			if (other.fieldName != null)
				return false;
		}
		else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}



	private Class<?> fieldClass;
	private String fieldName  = null;
	private String className = null;



}
