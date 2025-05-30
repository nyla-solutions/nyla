package nyla.solutions.global.json;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Paging;

/**
 * Handles server side transformation of the JSON date times
 * @author Gregory Green
 * @param <T> the Page collection type
 *
 */
public class PagingSerializer<T> implements JsonSerializer<Paging<T>>, JsonDeserializer<Paging<T>>
{
	public PagingSerializer()
	{
		this.gson = new Gson();
	}// --------------------------------------------------------
	public Paging<T> deserialize(JsonElement jsonelement, Type type,
			JsonDeserializationContext jsondeserializationcontext)
			throws JsonParseException
	{
		/*
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
            
            */
		
		return null;
	}// -----------------------------------------------
	public JsonElement serialize(Paging<T> paging, Type arg1,
			JsonSerializationContext arg2)
	{
		if(paging == null)
			return null;
		
		GSON gson = new GSON();
	    
	    PageCriteria pageCriteria = paging.getPageCriteria();
	    	    
	    JsonObject results = new JsonObject();
	    

	    results.add("collection", toArray(paging));
	    results.add("pageCriteria", gson.toJsonTree(pageCriteria));
	    
	    results.addProperty("empty",String.valueOf(paging.isEmpty()));
	    
	    results.addProperty("first",String.valueOf(paging.isFirst()));
	    
	    results.addProperty("last",String.valueOf(paging.isLast()));
	    
	    return results;
	}// -----------------------------------------------
	private JsonArray toArray(Collection<T> collection)
	{
		if(collection == null || collection.isEmpty())
			return null;
		
		JsonArray array = new JsonArray();
			
		for (T object : collection)
		{
			array.add(gson.toJsonTree(object));
		}
		
		return array;
	}// --------------------------------------------------------
	
	private final Gson gson;

}
