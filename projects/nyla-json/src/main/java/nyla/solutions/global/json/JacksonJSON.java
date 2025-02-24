package nyla.solutions.global.json;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;

import nyla.solutions.core.data.conversation.ArrayListBag;
import nyla.solutions.core.data.conversation.BaggedObject;
import nyla.solutions.core.data.conversation.SerializationMapKeyWrapper;
import nyla.solutions.core.data.conversation.SerializationRegionEntryWrapper;
import nyla.solutions.core.exception.SystemException;


public class JacksonJSON extends JSON
{

	@Override
	public String toJson(Object src)
	{
		return null;
	}

	@Override
	public String toJson(Object src, Class<?> classType)
	{
		return null;
	}

	@Override
	public <T> T fromJson(String json, Class<?> classOfT)
	{
		return null;
	}


	/**
	 * 
	 * @param file the file 
	 * @param object the object to write to file
	 * @throws Exception
	 */
	public static void writeObjectToFile(File file, Object object)
	throws Exception
	{
		ObjectMapper mapper = createObjectMapper();
		mapper.writeValue(file, object);
	}// --------------------------------------------------------
	/**
	 * @param <K>  the map key
	 * @param <V> the map value
	 * @param file the file to write
	 * @param map the region/map data
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException when an IO occurs occurs
	 */
	public static <K, V> void writeToFile(File file, Map<K, V> map)
			throws JsonGenerationException, JsonMappingException, IOException
	{
		Set<K> keySet = map.keySet();

		Collection<SerializationRegionEntryWrapper> collection = new ArrayList<SerializationRegionEntryWrapper>(
				keySet.size());

		ObjectMapper mapper = createObjectMapper();

		for (Object key : map.keySet())
		{
			constructWrappedCollection(collection, key, map);
		}

		// write data
		mapper.writeValue(file, collection);
	}// --------------------------------------------------------

	/**
	 * Directs the building of SerializationRegionEntryWrapper collection
	 * @param collection to build
	 * @param key the map key object to buiod
	 * @param map the map to build
	 */
	@SuppressWarnings("unchecked")
	private static <K, V> void constructWrappedCollection
			(Collection<SerializationRegionEntryWrapper> collection, Object key,Map<K, V> map) 
	{
		SerializationRegionEntryWrapper serializationWrapper;
		String keyClassName;
		Object value;
		String valueClassName;
		Object rawKey = key;
	
			keyClassName = key.getClass().getName();
	
		value = map.get(key);

		if (value == null)
			throw new IllegalArgumentException(
					"Internal error missing value for key" + rawKey);

		if(value instanceof ArrayList)
		{
			valueClassName = ArrayListBag.class.getName();
			value = new ArrayListBag((ArrayList<Object>)value);
		}
		else 
		{
			valueClassName = value.getClass().getName();

		}
		
		serializationWrapper = new SerializationRegionEntryWrapper(key,
				keyClassName, value, valueClassName);
		collection.add(serializationWrapper);
	}//-------------------------------------------------


	public static String toJSON(Object obj)
	{
		try
		{
			return createObjectMapper().writeValueAsString(obj);
		}
		catch (JsonProcessingException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}// --------------------------------------------------------
	/**
	 * Create the Jackson ObjectMapper with all required settings
	 * @return the object mapper
	 */
	public static ObjectMapper createObjectMapper()
	{
		
		
		ObjectMapper mapper = new ObjectMapper();
		mapper = mapper.registerModule(new MappedJacksonModule());
		
		mapper.getSerializerProvider().setNullKeySerializer(
				new DefaultNullKeySerializer());
		mapper.getSerializerProvider().setDefaultKeySerializer(
				new DefaultKeySerializer());
		
		
		//mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	   // mapper.enableDefaultTypingAsProperty(DefaultTyping.OBJECT_AND_NON_CONCRETE, "type");
		
		
		// Configure to be very forgiving
				mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

				mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
				mapper.disable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
				

		return mapper;
	}
	
	//---------------------------------
	private static class MappedJacksonModule extends SimpleModule
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = -3476376206891741283L;

		public MappedJacksonModule()
	    {
		
			super.setKeyDeserializers(new MySimpleKeyDeserializer());
			
			
	        addKeyDeserializer(
	        		String.class,
	            new DefaultKeyDeserializer() );
	        
	    }
	}
	//-----------------------------------
	/**
	 * Populate the map based on JSON data in a given file
	 * @param map the map to populate
	 * @param file the file containing the JSON input
	 * @return true if map populated
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws ClassNotFoundException
	 */
	public static boolean populateMap(Map<Object, Object> map, File file)
			throws IOException, JsonProcessingException, JsonParseException,
			JsonMappingException, ClassNotFoundException
	{
		Reader reader = null;
		
		try
		{
			reader = new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8"));
			
			return populateMap(map, reader);
		}
		finally
		{
			if(reader != null) reader.close();
		}
		
	}// --------------------------------------------------------
	/**
	 * 
	 * @param <T> the TYPE
	 * @param reader the reader
	 * @param clz the classed
	 * @return the class
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(Reader reader, Class<?> clz)
	{
		try
		{
			return (T)createObjectMapper().readValue(reader, clz);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}// --------------------------------------------------------
	/**
	 * Builds a map based on read JSON
	 * @param map the map to populate
	 * @param reader the reader that contains the JSON data
	 * @return true if the map was populated
	 * @throws IOException
	 * @throws JsonProcessingException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws ClassNotFoundException
	 */
	public static boolean populateMap(Map<Object, Object> map, Reader reader)
			throws IOException, JsonProcessingException, JsonParseException,
			JsonMappingException, ClassNotFoundException
	{

		// read JSON file

		ObjectMapper mapper = createObjectMapper();
		JsonNode node;
		JsonNode keyNode;
		JsonNode valueNode;
		JsonNode keyClassNameNode;
		JsonNode valueClassNameNode;
		Object key;
		Object value;
		// TokenBuffer buffer = new TokenBuffer
		JsonNode tree = mapper.readTree(reader);

		Iterator<JsonNode> children = tree.elements();

		if (children == null || !children.hasNext())
		{
			return false;
		}

		//String className = null;
		while (children.hasNext())
		{
			node = children.next();
			keyNode = node.get("key");
			valueNode = node.get("value");
			keyClassNameNode = node.get("keyClassName");
			valueClassNameNode = node.get("valueClassName");

			if (keyClassNameNode == null)
				throw new RuntimeException(
						"keyClassName required from all entries");

			if (valueClassNameNode == null)
				throw new RuntimeException(
						"valueClassName required from all entries");

			//className = keyClassNameNode.asText();
			
			
				try
				{
					key = mapper.readValue(keyNode.traverse(),
							forClassName(keyClassNameNode));
				}
				catch (JsonMappingException e)
				{
					throw new SystemException("keyNode"+keyNode+" keyClassNameNode"+keyClassNameNode,e);
				}
			

			//process VALUE
			//className = valueClassNameNode.asText();
			
			
				value = mapper.readValue(valueNode.traverse(),
						forClassName(valueClassNameNode));
			
		
			//unbag key
			if(key instanceof BaggedObject<?>)
			{
				key = ((BaggedObject<?>)key).unbag();
			}
			
			//unbag value
			if(value instanceof BaggedObject<?>)
			{
				value = ((BaggedObject<?>)value).unbag();
			}
			
			map.put(key, value);
			
		}
		return true;

	}// --------------------------------------------------------

	private static Class<?> forClassName(JsonNode jsonNode)
			throws ClassNotFoundException
	{
		if (jsonNode == null)
		{
			throw new ClassNotFoundException(
					"Class Name not found in json string");
		}

		String className = jsonNode.asText();

		if (className == null || className.length() == 0)
		{
			throw new ClassCastException("class name json string is empty: "
					+ jsonNode.toString());
		}

		try
		{
			return Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			throw new ClassNotFoundException("\"" + className + "\"", e);
		}
	}// --------------------------------------------------------

	static class MySimpleKeyDeserializer extends SimpleKeyDeserializers
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2733880016397822180L;

		@Override
		public KeyDeserializer findKeyDeserializer(JavaType type,
				DeserializationConfig config, BeanDescription beanDesc)
		{
				return new DefaultKeyDeserializer();
		}
	}// --------------------------------------------------------
	/**
	 * <pre>
	 * Inner class that handles Jackson deserialization implementation of the Map key that may
	 * not be strings.
	 * 
	 * This methods expects the JSON version of key as the field name.
	 * 
	 * "{\"key\":{\"prop1\":\"123\",\"port2\":\"ABC\"},\"keyClassName\":\"exampe.MyObject\"}"
	 * </pre>
	 *
	 */
	static class DefaultKeyDeserializer extends KeyDeserializer
	{

		/**
		 * Add the JSON version of the key as the
		 */
		@Override
		public Object deserializeKey(String keyString,
				DeserializationContext deserializationcontext)
				throws IOException, JsonProcessingException
		{

			if (keyString == null || keyString.length() == 0)
			{
				return null;
			}

			ObjectMapper objectMapper = new ObjectMapper();

			// JsonParser jp = deserializationcontext.getParser();

			JsonNode jsonNode = objectMapper.readTree(keyString);

			if (jsonNode.isTextual())
			{
				return keyString;
			}

			// jp.getCodec().readTree(jp);

			String jsonKey = jsonNode.get("key").toString();
			String keyClassName = jsonNode.get("keyClassName").asText();

			try
			{
				Object value = objectMapper.readValue(jsonKey,
						Class.forName(keyClassName));
				
				return value;
			}
			catch (UnrecognizedPropertyException e)
			{
				return null;
			}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException("Cannot create class name:"
						+ keyClassName, e);
			}
		}

	}// --------------------------------------------------------

	static class DefaultNullKeySerializer extends JsonSerializer<Object>
	{
		@Override
		public void serialize(Object nullKey, JsonGenerator jsonGenerator,
				SerializerProvider unused) throws IOException,
				JsonProcessingException
		{
			jsonGenerator.writeFieldName("");
		}
	}

	static class DefaultKeySerializer extends JsonSerializer<Object>
	{
		@Override
		public void serialize(Object key, JsonGenerator jsonGenerator,
				SerializerProvider unused) throws IOException,
				JsonProcessingException
		{
			

			if (key == null)
			{
				return;

			}

			// jsonGenerator.writeFieldName(obj.getClass().getName());

			ObjectMapper objectMapper = new ObjectMapper();

			StringWriter stringWriter = new StringWriter();

			objectMapper.writeValue(stringWriter,
					new SerializationMapKeyWrapper(key));
			jsonGenerator.writeFieldName(stringWriter.toString());

		}
		
	}// --------------------------------------------------------
	/**
	 * Determine is a given class is a primitive time
	 * @param aClass the class to test
	 * @return className.matches("(float|char|short|double|int|long|byte|boolean|(java.lang.
	 * (Date|Long|Integer|String|Float|Double|Short|Byte|Boolean)))");
	
	 */
	public static boolean isPrimitive(Class<?> aClass)
	{
		if(aClass == null)
			return false;
		
		String className = aClass.getName();
		return className.matches("(float|char|short|double|int|long|byte|boolean|(java.lang.(Date|Long|Integer|String|Float|Double|Short|Byte|Boolean)))");
		
	}// -----------------------------------------------
}
