package nyla.solutions.commas.json;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import nyla.solutions.core.patterns.reflection.ClassSchema;
import nyla.solutions.core.patterns.reflection.ClassSchemaVisitor;
import nyla.solutions.core.patterns.reflection.ClassType;
import nyla.solutions.core.patterns.reflection.MethodSchema;
import nyla.solutions.core.patterns.reflection.TypeSchema;


/**
 * Visitor to generate a sample JSON object for a class based on its..
 * Usage
 * 
 * JsonExampleVistor argVisitor = new JsonExampleVistor();
			
			argClassSchema.accept(argVisitor);
			
			this.jsonInput = argVisitor.getJsonStringBuilder().toString();
			
 * schema
 * @author Gregory Green
 *
 */
public class JsonExampleVistor implements Serializable,ClassSchemaVisitor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5038710981586345153L;
	
	/**
	 * Decorator a class
	 * @see nyla.solutions.global.patterns.reflection.ClassSchemaVisitor#visitClass(nyla.solutions.global.patterns.reflection.ClassSchema)
	 */
	public void visitClass(ClassSchema classSchema)
	{
		visitClass(classSchema,new HashSet<TypeSchema>());
	}// --------------------------------------------------------
	public void visitClass(ClassSchema classSchema, Set<TypeSchema> visitedSchemas)
	{
		jsonStringBuilder.setLength(0); //clear previous
		
		ClassType classType = classSchema.getClassType();
		boolean primitive = classType.equals(ClassType.primitive);
		
		if(primitive|| classType.equals(ClassType.time)|| classType.equals(ClassType.timestamp)|| classType.equals(ClassType.date))
		{
			emptyValue();
			
			return;
		}
		
		//Process ENUM with initial value
		boolean isEnum = classType.equals(ClassType.ENUM);
		
		if(isEnum)
		{
			Object[] enumConstants = classSchema.getEnumConstants();
			
			if(enumConstants != null  && enumConstants.length > 0)
				jsonStringBuilder.append("\"").append(enumConstants[0]).append("\"");
			else
				emptyValue();
			return;
		}
			
		beginDefine();
		TypeSchema[] typeSchemas =classSchema.getFieldSchemas();
		if(typeSchemas != null)
		{
			int count = 0;
			for (TypeSchema typeSchema : typeSchemas)
			{
				if(count > 0)
					separator();
					
				visitField(typeSchema,visitedSchemas); //test for recursive
				visitedSchemas.add(typeSchema);
				count++;
				
			}
		}
		endDefine();
		
	}// --------------------------------------------------------
	/**
	 * Define each field
	 * @see nyla.solutions.global.patterns.reflection.ClassSchemaVisitor#visitField(nyla.solutions.global.patterns.reflection.TypeSchema)
	 */
	
	public void visitField(TypeSchema typeSchema)
	{
		visitField(typeSchema, new HashSet<TypeSchema>());
	}// --------------------------------------------------------
	
	public void visitField(TypeSchema typeSchema, Set<TypeSchema> visitedTypeSchemas)
	{
		if(visitedTypeSchemas == null)
			return;
		
		if(visitedTypeSchemas.contains(typeSchema))
			return; //eliminate recursion
		
		
		visitedTypeSchemas.add(typeSchema);
		
		String fieldName = typeSchema.getFieldName();
		writeName(fieldName);
		
		//check if type exist 
		String fieldClassName =typeSchema.getClassName();
		
		String typeJson = fieldTypeDefMap.get(fieldClassName);
		if(typeJson != null)
		{
			this.jsonStringBuilder.append(typeJson);
			return;
		}
		
		ClassType classType = typeSchema.getClassType();
		JsonExampleVistor visitor = null;
		
		
		
	    switch(classType)
	    {
	    	case primitive: case time: case timestamp: case date: case calendar: 
	    		emptyValue(); 
	    	break;
	    	case ENUM: emptyValue(); 
	    	break;
	    	case array: 
	   
	    		beginArray();
	    	
	    		Class<?> fieldComponentType = typeSchema.getFieldClass().getComponentType();
	    		
	    		String fieldComponentTypeClassName = fieldComponentType.getName();
	    		
	    		//check if JSON exists
				String jsonFieldComponentType = fieldTypeDefMap.get(fieldComponentTypeClassName);
				
				if(jsonFieldComponentType == null)
				{
					visitor = new JsonExampleVistor();
					
					visitor.visitClass(new ClassSchema(fieldComponentType),visitedTypeSchemas);
					
					jsonFieldComponentType = visitor.jsonStringBuilder.toString();
					
					//save Json
					fieldTypeDefMap.put(fieldComponentTypeClassName,jsonFieldComponentType);
				}
				

				this.jsonStringBuilder.append(jsonFieldComponentType);
	    		
	    		endArray();
	    
	    	break;
	    	default:
				
				//check if JSON exists
	    		String fieldJson = fieldTypeDefMap.get(fieldClassName);
				if(fieldJson == null)
				{
	
					//complex type
					//get class schema
					visitor = new JsonExampleVistor();
					
					visitor.visitClass(new ClassSchema(typeSchema.getFieldClass()),visitedTypeSchemas);
					
					fieldJson = visitor.jsonStringBuilder.toString();
				}
				

				
				fieldTypeDefMap.put(fieldClassName, fieldJson);
				
				
				
				this.jsonStringBuilder.append(fieldJson);
		}
		
	}// --------------------------------------------------------
	/**
	 * Not implemented
	 * @see nyla.solutions.global.patterns.reflection.ClassSchemaVisitor#visitMethod(nyla.solutions.global.patterns.reflection.MethodSchema)
	 */
	public void visitMethod(MethodSchema methodSchema)
	{
	}// --------------------------------------------------------
	/**
	 * 
	 * @param name the object name to write
	 */
	private void writeName(String name)
	{
		quote();
		jsonStringBuilder.append(name);
		quote();
		
		jsonStringBuilder.append(":");
		
	}// --------------------------------------------------------
	private void separator()
	{
		jsonStringBuilder.append(",");
		
	}// --------------------------------------------------------
	private void emptyValue()
	{
		jsonStringBuilder.append("\"\"");
	}// --------------------------------------------------------
	private void quote()
	{
		jsonStringBuilder.append("\"");
	}
	private void beginArray()
	{
		jsonStringBuilder.append("[");
	}
	private void endArray()
	{
		jsonStringBuilder.append("]");
	}
	private void endDefine()
	{
		jsonStringBuilder.append("}");
	}
	private void beginDefine()
	{
		jsonStringBuilder.append("{");
	}
	
	/**
	 * @return the jsonStringBuilder
	 */
	public StringBuilder getJsonStringBuilder()
	{
		return jsonStringBuilder;
	}
	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return String.valueOf(jsonStringBuilder);
	}// --------------------------------------------------------
	private static HashMap<String,String> fieldTypeDefMap = new HashMap<String,String>();
	private StringBuilder jsonStringBuilder = new StringBuilder();
	
}
