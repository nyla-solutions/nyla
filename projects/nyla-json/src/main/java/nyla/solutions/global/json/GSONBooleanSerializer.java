package nyla.solutions.global.json;

import java.lang.reflect.Type;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GSONBooleanSerializer implements JsonSerializer<Boolean>, JsonDeserializer<Boolean>
{

	@Override
	public Boolean deserialize(JsonElement jsonelement, Type type,
			JsonDeserializationContext arg2) throws JsonParseException
	{
		
		
		if(!(jsonelement instanceof JsonPrimitive))
            throw new JsonParseException("Boolean must be primitive");
		
		String text = jsonelement.getAsString();
		
		if(text == null || text.length() == 0)
			return Boolean.FALSE;
		
		return Boolean.valueOf(text); 
	}// --------------------------------------------------------

	@Override
	public JsonElement serialize(Boolean bool, Type arg1,
			JsonSerializationContext arg2)
	{
		if(bool != null)
			return new JsonPrimitive(bool.toString());
		else
			return new JsonPrimitive(Boolean.FALSE.toString());
	}
	

}
