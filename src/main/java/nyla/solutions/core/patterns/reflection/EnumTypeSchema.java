package nyla.solutions.core.patterns.reflection;

import java.lang.reflect.Field;

/**
 * Meta-data information for a class field
 * @author Gregory Green
 *
 */
public class EnumTypeSchema extends PrimitiveTypeSchema
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public EnumTypeSchema(Field field)
	{
		this(field.getName(), field.getType());
	}// -----------------------------------------------
	public EnumTypeSchema(Class<?> aClass)
	{
		super(aClass);
		
	}// -----------------------------------------------	
	public EnumTypeSchema(String fieldName, Class<?> aClass)
	{
		super(fieldName,aClass);
	}// -----------------------------------------------	

	/**
	 * @return ClassType.primitive
	 */
	public ClassType getClassType()
	{
		return ClassType.ENUM;
	}


}
