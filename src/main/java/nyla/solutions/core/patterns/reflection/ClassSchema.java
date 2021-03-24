package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Composite view of a class definition.
 * 
 * 
 * Usage: 
 * ClassSchema classSchema = new ClassSchema(SchemaUT.class);
		
		TypeSchema[] fieldSchemas = classSchema.getFieldSchemas();
		
		Assert.assertNotNull(fieldSchemas);
		
 * @author Gregory Green
 *
 */
public class ClassSchema implements Serializable, ClassSchemaElement
{
	private final String className;
	private final ClassType classType;

	private final MethodSchema[] methodSchemas;
	private final TypeSchema[] fieldSchemas;
	private final Object[] enumConstants;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1510907649123959661L;

	/**
	 * Construct class schema for a given class
	 * @param aClass the class to construct the schema for
	 */
	public ClassSchema(Class<?> aClass)
	{
		if (aClass == null)
			throw new IllegalArgumentException("aClass required");

		this.className = aClass.getName();

		if(Mirror.isPrimitive(aClass))
		{
			classType = ClassType.primitive;
			methodSchemas = null;
			fieldSchemas = null;
			enumConstants = null;
			return; //do not process primitive
		}
		else if(aClass.isArray())
		{
			classType = ClassType.array;
			methodSchemas = null;
			fieldSchemas = null;
			enumConstants = null;
			return;
		}
		else if(Timestamp.class.equals(aClass))
		{
			classType = ClassType.timestamp;
			this.enumConstants =null;
			methodSchemas = null;
			fieldSchemas = null;
			return;
		}
		else if(Calendar.class.equals(aClass))
		{
			classType = ClassType.calendar;
			this.enumConstants =null;
			methodSchemas = null;
			fieldSchemas = null;
			return;
		}
		else if(Time.class.equals(aClass))
		{
			classType = ClassType.time;
			this.enumConstants =null;
			methodSchemas = null;
			fieldSchemas = null;
			return;
		}
		else if(Date.class.isAssignableFrom(aClass))
		{
			classType = ClassType.date;
			this.enumConstants =null;
			methodSchemas = null;
			fieldSchemas = null;
			return;
		}
		else if(aClass.isEnum())
		{
			classType = ClassType.ENUM;
			this.enumConstants = aClass.getEnumConstants();
			methodSchemas = null;
			fieldSchemas = null;
			return;
		}
		else if(aClass.getName().startsWith("java."))
		{
			classType = ClassType.NonPrimitiveJava;
			methodSchemas = null;
			fieldSchemas = null;
			enumConstants = null;
			return;
		}
		else {
			classType = ClassType.generic;
			this.enumConstants = null;
			Field[] fields = aClass.getDeclaredFields();

			if(fields == null || fields.length == 0)
			{
				methodSchemas = null;
				fieldSchemas = null;
				return;

			}


			ArrayList<TypeSchema> fieldList = new ArrayList<TypeSchema>(fields.length);

			Class<?> fieldClass = null;
			for (int i = 0; i < fields.length;i++)
			{
				if("serialVersionUID".equals(fields[i].getName()))
					continue; // serial version

				fieldClass = fields[i].getType();

				if(Mirror.isPrimitive(fieldClass))
				{
					fieldList.add(new PrimitiveTypeSchema(fields[i]));
				}
				else
				{
					fieldList.add(new ComplexTypeSchema(fields[i]));
				}
			}

			this.fieldSchemas = new TypeSchema[fieldList.size()];

			fieldList.toArray(this.fieldSchemas);

			//Build methods

			Method[] methods = aClass.getMethods();

			if(methods != null)
			{
				this.methodSchemas = new MethodSchema[methods.length];
				for (int i = 0; i < methods.length; i++)
				{
					methodSchemas[i] = new MethodSchema(methods[i]);
				}

			}
			else
			{
				methodSchemas = null;
			}
		}



	}// -----------------------------------------------


	/**
	 * @return the classType
	 */
	public ClassType getClassType()
	{
		return classType;
	}


	public void accept(ClassSchemaVisitor visitor)
	{
		visitor.visitClass(this);
	}

	/**
	 * @return the className
	 */
	public String getObjectClassName()
	{
		return className;
	}// -----------------------------------------------

	/**
	 * List all fields
	 * @return the array of types schemas
	 */
	public TypeSchema[] getFieldSchemas()
	{
		if(fieldSchemas == null)
			return null;

		return fieldSchemas.clone();
	}// -----------------------------------------------




	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ClassSchema [className=" + className + ", classType="
				+ classType + ", methodSchemas="
				+ Arrays.toString(methodSchemas) + ", fieldSchemas="
				+ Arrays.toString(fieldSchemas) + ", enumConstants="
				+ Arrays.toString(enumConstants) + "]";
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
		result = prime * result + Arrays.hashCode(enumConstants);
		result = prime * result + Arrays.hashCode(fieldSchemas);
		result = prime * result + Arrays.hashCode(methodSchemas);
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
		ClassSchema other = (ClassSchema) obj;
		if (className == null)
		{
			if (other.className != null)
				return false;
		}
		else if (!className.equals(other.className))
			return false;
		if (classType != other.classType)
			return false;
		if (!Arrays.equals(enumConstants, other.enumConstants))
			return false;
		if (!Arrays.equals(fieldSchemas, other.fieldSchemas))
			return false;
		if (!Arrays.equals(methodSchemas, other.methodSchemas))
			return false;
		return true;
	}

	/**
	 * @return the enumConstants
	 */
	public Object[] getEnumConstants()
	{
		if(enumConstants == null)
			return null;

		return enumConstants.clone();
	}




}
