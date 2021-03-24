package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;
import java.util.Date;

/**
 * Meta data object standard for a given object
 * @author Gregory Green
 *
 */
public class GenericField implements Serializable
{
	/**
	 * serialVersionUID = -463965000413175143L
	 */
	private static final long serialVersionUID = -463965000413175143L;
	
	/**
	 * The Object Field
	 * @param className the class name
	 * @param fieldName the field name
	 * @param textValue
	 */
	public GenericField(String className,String fieldName, String textValue)
	{
		if(className == null)
			throw new IllegalArgumentException("className required");
		
		if(fieldName == null)
			throw new IllegalArgumentException("fieldName required");
		
		className = className.trim();
		this.fieldName = fieldName.trim();
		
		if(className.equals("String") || String.class.getName().equals(className))
		{
			this.objClass = String.class;
		}
		else if(className.equalsIgnoreCase("Double") || Double.class.getName().equals(className))
		{
			this.objClass = Double.class;
		}		
		else if(className.equals("int") ||className.equals("Integer") || Integer.class.getName().equals(className))
		{
			this.objClass = Integer.class;
		}			
		else if(className.equalsIgnoreCase("Long") || Long.class.getName().equals(className))
		{
			this.objClass = Long.class;
		}	
		else if(className.equalsIgnoreCase("Float") || Float.class.getName().equals(className))
		{
			this.objClass = Float.class;
		}
		else if(className.equalsIgnoreCase("byte") || Byte.class.getName().equals(className))
		{
			this.objClass = Byte.class;
		}			
		else if(className.equalsIgnoreCase("Short") || Short.class.getName().equals(className))
		{
			this.objClass = Short.class;
		}		
		else if("Date".equalsIgnoreCase(className)||java.util.Date.class.getName().equals(className))
		{
			this.objClass = java.util.Date.class;
		}
		else if(className.equalsIgnoreCase("char") ||className.equalsIgnoreCase("Character") || Character.class.getName().equals(className))
		{
			this.objClass = Character.class;
		}
		else
		{
			throw new IllegalArgumentException("className:"+className+" unsupported");
		}
	}// -----------------------------------------------
	/**
	 * 
	 * @return true if field class is java.util.Date
	 */
	public  boolean isDate()
	{
		return isDate(this);
	}// -----------------------------------------------
	/**
	 * 
	 * @param field
	 * @return true if field class is java.util.Date
	 */
	public static boolean isDate(GenericField field )
	{
		if(field == null)
			return false;
		
		String fieldName = field.getFieldName();
		
		if("serialVersionUID".equals(fieldName))
			return false;
		
		Class<?> typeClass = field.getObjClass();
		if(typeClass == null)
			return false;
		
		String className = typeClass.getName();
		System.out.println("declaringClass.getName()="+className);
		
		return className.matches("java.util.Date");
		
	}// -----------------------------------------------	
	public static boolean isSupportedFieldType(String className)
	{
		if(className.equals("String") || String.class.getName().equals(className))
		{
			return true;
		}
		else if(className.equalsIgnoreCase("Double") || Double.class.getName().equals(className))
		{
			return true;
		}		
		else if(className.equals("int") ||className.equals("Integer") || Integer.class.getName().equals(className))
		{
			return true;
		}			
		else if(className.equalsIgnoreCase("Long") || Long.class.getName().equals(className))
		{
			return true;
		}	
		else if(className.equalsIgnoreCase("Float") || Float.class.getName().equals(className))
		{
			return true;
		}
		else if(className.equalsIgnoreCase("byte") || Byte.class.getName().equals(className))
		{
			return true;
		}			
		else if(className.equalsIgnoreCase("Short") || Short.class.getName().equals(className))
		{
			return true;
		}		
		else if("Date".equalsIgnoreCase(className)||java.util.Date.class.getName().equals(className))
		{
			return true;
		}
		else if(className.equalsIgnoreCase("char") ||className.equalsIgnoreCase("Character") || Character.class.getName().equals(className))
		{
			return true;
		}
		else
		{
			return false;
		}		
	}// -----------------------------------------------
	/**
	 * @return the value
	 */
	public Object getValue()
	{
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Object value)
	{
		this.value = value;
	}
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
	}
	/**
	 * @return the objClass
	 */
	public Class<?> getObjClass()
	{
		return objClass;
	}
	/**
	 * @param objClass the objClass to set
	 */
	public void setObjClass(Class<?> objClass)
	{
		this.objClass = objClass;
	}// -----------------------------------------------
	public void setLong(Long value)
	{
		this.value = value;
	}// -----------------------------------------------
	public void setDouble(Double value)
	{
		this.value = value;
	}// -----------------------------------------------	
	public void setShort(Short value)
	{
		this.value = value;
	}// -----------------------------------------------	
	public void setByte(Byte value)
	{
		this.value = value;
	}// -----------------------------------------------	
	public void setFloat(Float value)
	{
		this.value = value;
	}// -----------------------------------------------	
	public void setInt(Integer value)
	{
		this.value = value;
	}// -----------------------------------------------	
	public void setChar(Character value)
	{
		this.value = value;
	}// -----------------------------------------------		
	public void setDate(Date value)
	{
		this.value = value;
	}// -----------------------------------------------		
	private Object value = null;
	private String fieldName  = null;
	private Class<?> objClass = null;
}
