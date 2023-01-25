package nyla.solutions.core.patterns.reflection;

import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static nyla.solutions.core.util.Config.settings;


/**
 * This object using reflect to manage object fields 
 * @author Gregory Green
 *
 */
public class Mirror
{
	/**
	 * DEFAULT_DATE_FORMAT = MM/dd/yyyy hh:mm:ss:SS
	 * 
	 * Note: this can override using a Config property
	 */
	public final static String DEFAULT_DATE_FORMAT =  settings().getProperty(Mirror.class,"DEFAULT_DATE_FORMAT","MM/dd/yyyy hh:mm:ss:SS");
	
	
	
	/**
	 * Create reflector for a given object
	 * @param target the object to reflect
	 */
	public Mirror(Object target)
	{
		this.target = target;
		this.targetClass = target.getClass(); 
		
		Field[] fields = targetClass.getDeclaredFields();
		Field field  = null;
		int fieldSize =  fields.length;
		for (int i=0; i < fieldSize;i++)
		{
			field = fields[i];
			
			fieldMap.put(field.getName(), field);
		}
		
		
	}// -----------------------------------------------
	/**
	 * Return the class schema meta-data
	 * @param aClass the class to process
	 * @return class schema
	 * @throws ClassNotFoundException 
	 */
	public static ClassSchema toClassSchema(Class<?> aClass) 
	throws ClassNotFoundException
	{
		return new ClassSchema(aClass);
	}// -----------------------------------------------
	/**
	 * Return the class 
	 * @param className the class name
	 * @return Class schema
	 * @throws ClassNotFoundException 
	 */
	public static ClassSchema toClassSchema(String className) 
	throws ClassNotFoundException
	{
		return new ClassSchema(Class.forName(className));
	}// -----------------------------------------------
	/**
	 * Create an instance of the given object
	 * @param className the class name 
	 * @return the object reflector instance
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Mirror newInstanceForClassName(String className) 
	throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		className = className.trim();
		Class<?> objClass = Class.forName(className);

		   
		return new Mirror(ClassPath.newInstance(objClass));
	}// -----------------------------------------------
	public static boolean isPrimitive(Class<?> aClass )
	{
		//return true;
		return ClassPath.isPrimitive(aClass);
	}// -----------------------------------------------

	/**
	 * Support types
	private String myString = null;
	private int myInt =  0;
	private float myFloat = 0;
	private double myDouble = 0;
	private Date myDate = Calendar.getInstance().getTime();
	private long myLong = 0;
	private byte mybyte = 0;
	private char mychar = 'f';
	private short myshort = 0;
	 * @param fieldName the object field name
	 * @param fieldValue the representation of the field value
	 * @throws IllegalAccessException the illegal access exception
	 */
	public void setField(String fieldName, Object fieldValue)
	throws IllegalAccessException
	{
		//Get Field
		Field field = (Field)this.fieldMap.get(fieldName);
		if(field == null)
			throw new IllegalAccessException("field:"+fieldName+" not found");
		
		field.setAccessible(true);
		
		//check if type match 
		//TODO: may need to cache this
		try
		{
			if(fieldValue == null)
			{
				//set field null
				field.set(target, null);
				return;
			}
			
			Object fieldObj =  field.get(target);
			if(fieldObj == null)
			{
				//create new 
				fieldObj = ClassPath.newInstance(field.getType());
			}
			
			Class<?> fieldClass = fieldObj.getClass();
			Class<?> fieldObjClass = fieldValue.getClass();
			if(fieldClass.equals(fieldObjClass))
			{
				field.set(target, fieldValue);	
			}
			else
			{
				//Performance transformation
				//TODO use enum for performance improvement
			
				String fiedValueString = fieldValue.toString();
				
				if(fieldClass == String.class)
				{
					field.set(target, fiedValueString);
					return;
				}
				
				if(fieldClass == Integer.class)
				{
				  
					field.set(target, Integer.valueOf(fiedValueString));
				}	
				else if(fieldClass == Date.class)
				{
					
					try
					{
						Date date = null;
						
						if(fiedValueString != null)
						{
							//trim date
							fiedValueString = fiedValueString.trim();
							
							//parse date
							if(fiedValueString.length() > 0)
								date = this.dateFormat.parse(fiedValueString.trim());
						}
						
						field.set(target,date);
					}
					catch(ParseException e)
					{
						throw new RuntimeException(e.getMessage()+" value:"+fieldValue.toString()+" format:"+this.datePattern);
					}
				}
				else if(fieldClass == Double.class)
				{
					field.set(target, Double.valueOf(fiedValueString));
				}
				else if(fieldClass == Long.class)
				{
					field.set(target, Long.valueOf(fiedValueString));
				}
				else if(fieldClass == Short.class)
				{
					field.set(target, Short.valueOf(fiedValueString));
				}
				else if(fieldClass == Character.class)
				{
					field.setChar(target, fiedValueString.charAt(0));
				}	
				else if(fieldClass == Byte.class)
				{
					field.set(target, Byte.valueOf(fiedValueString));
				}						
				else if(fieldClass == Float.class)
				{
					field.set(target, Float.valueOf(fiedValueString));
				}				
				else
				{
					throw new RuntimeException("Not supported Class "+fieldClass.getName());
				}
	
			}// -----------------------------------------------
			
			field.setAccessible(false);
		}		
		catch(RuntimeException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SecurityException("field:"+field.getName()+" error:"+Debugger.stackTrace(e));
		}
	}// -----------------------------------------------
	/**
	 * @return the dateFormat
	 */
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}
	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}
	
	/**
	 * @return the target
	 */
	public Object getObject()
	{
		return target;
	}// -----------------------------------------------

	/**
	 * @return the fieldMap
	 */
	public Collection<Field> getFields()
	{
		return fieldMap.values();
	}// -----------------------------------------------


	private String datePattern = DEFAULT_DATE_FORMAT;
	private DateFormat dateFormat = new SimpleDateFormat(datePattern);
	private Object target = null;
	private Class<?> targetClass = null;
	private Map<String, Field> fieldMap = new HashMap<String, Field>();
}
