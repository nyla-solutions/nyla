package nyla.solutions.global.json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.creational.BuilderDirector;
import nyla.solutions.core.util.Config;

/**
 * To add additional register serialize
 * 
 * Use the following property
 * solutions.global.json.GSON.builderDirectorClassName=someClass
 * 
 * 
 * Usage
 * 
 * JSON json = JSON.newInstance();
		
   String jsonString = json.toJson(paging);
		
 * @author Gregory Green
 *
 */
public class GSON extends JSON
{
	/**
	 * Constructor
	 */
	public GSON()
	{
	
		director = ClassPath.newInstance(builderDirectorClassName);
		
		GsonBuilder builder = new GsonBuilder();		     
		director.construct(builder);
		
		this.gson = builder.create();
	}// -----------------------------------------------
	public synchronized String toJson(Object src)
	{
		return gson.toJson(src);
	}// -----------------------------------------------
	@SuppressWarnings("rawtypes")
	public synchronized String toJson(Object src, Class classType)
	{
		return gson.toJson(src, classType);
	}// -----------------------------------------------

	@SuppressWarnings({ "unchecked"})
	public synchronized <T> T fromJson(String json, Class<?> classOfT)
	{
		return (T)gson.fromJson(json, classOfT);
	}// -----------------------------------------------

	
	/**
	 * @param src the source object
	 * @return the Json element
	 * @see com.google.gson.Gson#toJsonTree(java.lang.Object)
	 */
	public JsonElement toJsonTree(Object src)
	{
		return gson.toJsonTree(src);
	}// --------------------------------------------------------


	private final BuilderDirector<GsonBuilder> director;
	private String builderDirectorClassName = Config.settings().getProperty(GSON.class,"builderDirectorClassName",GsonBuilderDirector.class.getName());
	private final Gson gson;
	
}
