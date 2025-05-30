package nyla.solutions.dao;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents an entity row
 * @author Gregory Green
 *
 */
public class ResultSetMapRow implements Map<String,Object>
{

	   /**
	    * 
	    * Constructor for DataRow initializes internal from the result set 
	    * @param resultSet the database result set
	    * @param rowNum starts from 0
	    * @throws SQLException
	    */
	public ResultSetMapRow(ResultSet resultSet)
	throws SQLException 
    {      
		   this(resultSet, 0);
	}// --------------------------------------------------------
   /**
    * 
    * Constructor for DataRow initializes internal from the result set 
    * @param resultSet the database result set
    * @param rowNum starts from 0
    * @throws SQLException
    */
   public ResultSetMapRow(ResultSet resultSet, int rowNum)
   throws SQLException 
   {      
	  this.rowNumber = rowNum;
	  
	  ResultSetMetaData meta = resultSet.getMetaData();
	  
      int columnCount = meta.getColumnCount();
      
      String columnName;
     for (int i = 0; i< columnCount; i++)
     {         
        
    	 columnName = meta.getColumnName(i+1);
    	 
    	 if(columnName == null)
    		 columnName = "col"+i;
    	 
    	 rowMap.put(columnName, resultSet.getObject(i+1));
     }
  
   }// --------------------------------------------   

   /**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		return rowMap.isEmpty();
	}
	/**
	 * @param obj
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object obj)
	{
		return rowMap.containsKey(obj);
	}
	/**
	 * @param obj
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object obj)
	{
		return rowMap.containsValue(obj);
	}
	/**
	 * @param obj
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Object get(Object obj)
	{
		return rowMap.get(obj);
	}
	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		rowMap.clear();
	}
	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet()
	{
		return rowMap.keySet();
	}
	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<String, Object>> entrySet()
	{
		return rowMap.entrySet();
	}
	/**
	 * @param obj
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		return rowMap.equals(obj);
	}
	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode()
	{
		return rowMap.hashCode();
	}
	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(String key, Object value)
	{
		return rowMap.put(key, value);
	}
	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		return rowMap.size();
	}
	/**
	 * @param obj
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object obj)
	{
		return rowMap.remove(obj);
	}
	/**
	 * @param map
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String, ? extends Object> map)
	{
		rowMap.putAll(map);
	}
	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<Object> values()
	{
		return rowMap.values();
	}
	
	
	/**
	 * @return the rowNumber
	 */
	public int getRowNumber()
	{
		return rowNumber;
	}


	private final int rowNumber;
   private Map<String,Object> rowMap = new HashMap<String,Object>();

}
