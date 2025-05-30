package nyla.solutions.global.json;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nyla.solutions.core.util.Text;

/**
 * Handles server side transformation of the JSON date times
 * @author Gregory Green
 *
 */
public class GSONDateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date>
{
	public Date deserialize(JsonElement jsonelement, Type type,
			JsonDeserializationContext jsondeserializationcontext)
			throws JsonParseException
	{
		
		if(!(jsonelement instanceof JsonPrimitive))
            throw new JsonParseException("The date should be a string value");
		
		String text = jsonelement.getAsString();
		
		if(text == null || text.length() == 0)
			return null;
		
        Date date = Text.toDate(jsonelement.getAsString(),JSON.DATE_FORMAT);
        
        if(java.util.Date.class.equals(type))
            return date;
        if(java.sql.Timestamp.class.equals(type))
            return new Timestamp(date.getTime());
        if(java.sql.Date.class.equals(type))
            return new java.sql.Date(date.getTime());
        else
            throw new JsonParseException((new StringBuilder()).append(getClass()).append(" cannot deserialize to ").append(type).toString());
	}// -----------------------------------------------
	public JsonElement serialize(Date date, Type arg1,
			JsonSerializationContext arg2)
	{
        return new JsonPrimitive(Text.formatDate(JSON.DATE_FORMAT, date));
	}// -----------------------------------------------

}
