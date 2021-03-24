package nyla.solutions.core.patterns.reflection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Meta-data for a object
 * @author Gregory Green
 *
 */
public class GenericObject implements Serializable
{
	/**
	 * serialVersionUID = -8914553596553153625L
	 */
	private static final long serialVersionUID = -8914553596553153625L;
	
	public GenericObject(String objectClassName)
	{
		if(objectClassName == null)
			throw new IllegalArgumentException("objectClassName required");
		
		this.objectClassName = objectClassName;
		
	}// -----------------------------------------------	
	/**
	 * 
	 * @param objectClassName the object class name
	 * @param fields the object fields
	 */
	public GenericObject(String objectClassName,GenericField[] fields)
	{
		this(objectClassName);
		
		addFields(fields);
	}// -----------------------------------------------
	public void addFields(GenericField[] fields)
	{
		for (int i=0; i < fields.length;i++)
		{
			this.fieldMap.put(fields[i].getFieldName(), fields[i]);
		}
	}// -----------------------------------------------
	/**
	 * @return the objectClassName
	 */
	public String getObjectClassName()
	{
		return objectClassName;
	}// -----------------------------------------------
	/**
	 * List all fields
	 * @return the array of generic fields
	 */
	public GenericField[] getFields()
	{
		Collection<GenericField> values = fieldMap.values();
		
		if(values == null || values.isEmpty())
			return null;
		
		GenericField[] fieldMirrors = new GenericField[values.size()];
		
		values.toArray(fieldMirrors);
		
		return fieldMirrors;
	}// -----------------------------------------------
	/**
	 * 
	 * @param fieldName the field to get
	 * @return the GenericField
	 */
	public GenericField retrieveField(String fieldName)
	{
		return (GenericField)fieldMap.get(fieldName);
	}// -----------------------------------------------
	private Map<String, GenericField> fieldMap = new HashMap<String, GenericField>();
	private String objectClassName = null;
}
