package nyla.solutions.core.patterns.creational.builder.mapped;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Text;

import java.util.Map;
import java.util.TreeMap;


/**
 * @param <K> the key
 * @param <V> the value
 * Implements a MapFactory by id.
 * 
 * The LookupTable is a map of maps where the key is a regular expression.
 * @author Gregory Green 
 *
 */
public class   ReLookupTextMapFactory<K,V>  implements MapFactoryById<K,V>
{
	/**
	 * @return Map of Textable objects
	 */
	public Map<K,V> createMap()
	{
		if(Text.isNull(this.id))
			throw new RequiredException("ReLookupTextMap.id");
			
		if (this.lookupTable == null || lookupTable.isEmpty())
			throw new RequiredException(
					"this.lookupTable in ReLookupTextMapById.lookupTextMap");
		String re = null;
		 for (Map.Entry<String,Map<K,V>> entry : lookupTable.entrySet())
         {
                   //process next source value
                  re = entry.getKey();

                  //the key contains a RE, test if it matches the current source value
                  if(Text.matches(id,re))//sourceValue.matches(re)
                  {
                     return entry.getValue();
                     
                  }//end for re iteration
                  
         }//end lookup re    
	
		 throw new SystemException("No map found for \""+id+" in regExp keys "+this.lookupTable.keySet());
	}// --------------------------------------------
	
	/**
	 * @return the lookupTable
	 */
	public Map<String,Map<K,V>>  getLookupTable()
	{
		return lookupTable;
	}
	/**
	 * The key of the lookup table is a regular expression
	 * @param lookupTable the lookupTable to set
	 */
	public void setLookupTable(Map<String,Map<K,V>>  lookupTable)
	{
		this.lookupTable = new TreeMap<String,Map<K,V>> (lookupTable);
	}// --------------------------------------------
	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}// --------------------------------------------

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	private TreeMap<String,Map<K,V>>  lookupTable = null;
	private String id = null;


}
