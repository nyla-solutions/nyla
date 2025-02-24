package nyla.solutions.global.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Handles server side transformation of the JSON date times
 * @author Gregory Green
 *
 */
public class GSONNumberSerializer implements JsonSerializer<Number>, JsonDeserializer<Number>
{

	public Number deserialize(JsonElement jsonelement, Type type,
			JsonDeserializationContext jsondeserializationcontext)
			throws JsonParseException
	{
		
		if(!(jsonelement instanceof JsonPrimitive))
            throw new JsonParseException("The number should be a string value");
		
		String text = jsonelement.getAsString();
		
		if(text == null || text.length() == 0)
			return 0; //
		
        
		if(int.class.equals(type))
			return Integer.parseInt(text);
		else if(Integer.class.equals(type))
            return Integer.valueOf(text);
		else if(long.class.equals(type))
            return Long.parseLong(text);		
        else if(java.lang.Long.class.equals(type))
        	return Long.valueOf(text);
        else if(float.class.equals(type))
        	return Float.parseFloat(text); 		
        else if(java.lang.Float.class.equals(type))
        	return Float.valueOf(text);       
        else if(java.lang.Double.class.equals(type))
        	return Double.valueOf(text);   
        else if(double.class.equals(type))
        	return Double.parseDouble(text);  
        else if(short.class.equals(type))
        	return Short.parseShort(text);		
        else if(java.lang.Short.class.equals(type))
        	return Short.valueOf(text);
        else if(BigDecimal.class.equals(type))
        	return new BigDecimal(text); 
        else
            throw new JsonParseException((new StringBuilder()).append(getClass()).append(" cannot deserialize to ").append(type).toString());
	}// -----------------------------------------------
	public JsonElement serialize(Number number, Type arg1,
			JsonSerializationContext arg2)
	{
		if(number != null)
			return new JsonPrimitive(number.toString());
		else
			return new JsonPrimitive("0");
	}// -----------------------------------------------

}
