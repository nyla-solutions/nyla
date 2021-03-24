package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.HashSet;

/**
 * Meta-data information for a class field
 * @author Gregory Green
 *
 */
public class ComplexTypeSchema implements Serializable, TypeSchema
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8221193500710478919L;
	
	public ComplexTypeSchema(Field field)
	{
		this(field.getName(),field.getType());
	}// -----------------------------------------------
	public ComplexTypeSchema(Class<?> fieldClass)
	{
		this(null,fieldClass);
	}// -----------------------------------------------
	public ComplexTypeSchema(String fieldName, Class<?> fieldClass)
	{
		this.setFieldName(fieldName);
		
		this.fieldClass = fieldClass;
		
		this.className = fieldClass.getName();
		
		if(fieldClass.isArray())
			this.classType = ClassType.array;
		else if (Date.class.equals(fieldClass) || java.util.Date.class.equals(fieldClass))
		{
			this.classType = ClassType.date;
		}
		else if( java.util.Calendar.class.equals(fieldClass))
		{
			this.classType = ClassType.calendar;
		}
		else if( java.sql.Timestamp.class.equals(fieldClass))
		{
			this.classType = ClassType.timestamp;
		}
		else if( java.sql.Time.class.equals(fieldClass))
		{
			this.classType = ClassType.time;
		}		
		else
	    {
			Type[] generics = fieldClass.getGenericInterfaces();
		
			if(generics != null && generics.length > 0)
				this.classType = ClassType.generic;
			
			else
				this.classType = ClassType.object;
		}
		
		
		Field[] fields = fieldClass.getFields();
		
		Class<?> declaringClass= null;
		for (int i = 0; i < fields.length; i++)
		{
			declaringClass = fields[i].getType();
			
			
			if(Mirror.isPrimitive(declaringClass))
				this.primitiveFieldSchemas.add(new PrimitiveTypeSchema(fields[i]));
			else if(declaringClass.isEnum())
			{
				this.primitiveFieldSchemas.add(new EnumTypeSchema(fields[i]));
			}
			else
			{
				this.complexFieldSchemas.add(new ComplexTypeSchema(fields[i]));	
			}
		    
		}
	}// -----------------------------------------------
	/**
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
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
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}// ----------------------------------------------- 
	

	/**
	 * @return the classType
	 */
	public ClassType getClassType()
	{
		return classType;
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

	public TypeSchema[] getFieldsTypes()
	{
		TypeSchema[] typeSchemas =new TypeSchema[this.primitiveFieldSchemas.size()+this.complexFieldSchemas.size()];
		
		int i = 0;
		for (TypeSchema ts : primitiveFieldSchemas)
		{
			typeSchemas[i++] = ts;
		}
		
		for (TypeSchema ts : complexFieldSchemas)
		{
			typeSchemas[i++] = ts;
		}
		
		return typeSchemas;
		
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ComplexTypeSchema [fieldClass=" + fieldClass + ", classType="
				+ classType + ", primitiveFieldSchemas="
				+ primitiveFieldSchemas + ", complexFieldSchemas="
				+ complexFieldSchemas + ", fieldName=" + fieldName
				+ ", className=" + className + "]";
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
				+ ((classType == null) ? 0 : classType.hashCode());
		result = prime
				* result
				+ ((complexFieldSchemas == null) ? 0 : complexFieldSchemas
						.hashCode());
		result = prime * result
				+ ((fieldClass == null) ? 0 : fieldClass.hashCode());
		result = prime * result
				+ ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime
				* result
				+ ((primitiveFieldSchemas == null) ? 0 : primitiveFieldSchemas
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
		ComplexTypeSchema other = (ComplexTypeSchema) obj;
		if (className == null)
		{
			if (other.className != null)
				return false;
		}
		else if (!className.equals(other.className))
			return false;
		if (classType != other.classType)
			return false;
		if (complexFieldSchemas == null)
		{
			if (other.complexFieldSchemas != null)
				return false;
		}
		else if (!complexFieldSchemas.equals(other.complexFieldSchemas))
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
		if (primitiveFieldSchemas == null)
		{
			if (other.primitiveFieldSchemas != null)
				return false;
		}
		else if (!primitiveFieldSchemas.equals(other.primitiveFieldSchemas))
			return false;
		return true;
	}



	private Class<?> fieldClass;
	
    private final ClassType classType;
	private HashSet<TypeSchema> primitiveFieldSchemas = new HashSet<TypeSchema>();
	private HashSet<TypeSchema> complexFieldSchemas = new HashSet<TypeSchema>();
	private String fieldName;
	private String className;
}
