package nyla.solutions.core.patterns.creational.builder.mapped;

import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.creational.servicefactory.ServiceFactory;
import nyla.solutions.core.util.Debugger;

import java.util.Map;

import static nyla.solutions.core.util.Config.settings;


public class MappedKeyDirector<K,V>
{
	/**
	 * 
	 * @param path the path 
	 * @return the map of text object
	 * @throws NoDataFoundException
	 */
	protected Map<K,V> constructMapToText(String path)
	throws NoDataFoundException
	{		
		MapFactoryById<K,V> factory = ServiceFactory.getInstance().create(this.mapFactoryByIdServiceName);
		factory.setId(path);
		
		return factory.createMap();
	}// --------------------------------------------
	/**
	 * Director method to construct a document
	 * @param id the for construction
	 * @param engineer the strategy creation
	 */
	public void constructDocument(String id, MappedKeyEngineer<K,V> engineer)
	{
		try
		{
			//construct map
			Map<K,V> textableMap = this.constructMapToText(id);
					
			engineer.construct(id,textableMap);
		}
		catch (NoDataFoundException e)
		{
			throw new SystemException("No textable found for id="+id+" ERROR:"+Debugger.stackTrace(e));
		}		
	}// --------------------------------------------
	
	/**
	 * @return the mapFactoryByIdServiceName
	 */
	public String getMapFactoryByIdServiceName()
	{
		return mapFactoryByIdServiceName;
	}
	/**
	 * @param mapFactoryByIdServiceName the mapFactoryByIdServiceName to set
	 */
	public void setMapFactoryByIdServiceName(String mapFactoryByIdServiceName)
	{
		this.mapFactoryByIdServiceName = mapFactoryByIdServiceName;
	}

	private String mapFactoryByIdServiceName = settings().getProperty(this.getClass(),
			"mapFactoryByIdServiceName",MapFactoryById.class.getName());
	

}
