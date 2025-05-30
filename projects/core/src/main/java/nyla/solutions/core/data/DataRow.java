package nyla.solutions.core.data;

import java.io.Serializable;
import java.util.*;


/**
 * Represent a row of data
 * @author Gregory Green
 *
 */
public class DataRow implements Arrayable<Object>, Serializable, Mapped<String, Serializable>
{
	private static final int START_WITH_ZERO = 1;
	private final ArrayList<Object> positionEntries;
	private Map<String, Serializable> nameEntries;
	private int rowNum;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6651575339358295855L;
	
	public  DataRow()
	{
		this.positionEntries = new ArrayList<Object>();
		
	}// --------------------------------------------------------
	/***
	 * 
	 * @param inputs initial row values
	 */
	public  DataRow(Object[] inputs)
	{
		this.positionEntries = new ArrayList<Object>(Arrays.asList(inputs));
	}// --------------------------------------------------------
	/***
	 * 
	 * @param inputs initial row values
	 */
	public  DataRow(Map<String,Serializable> inputs)
	{
		this();
		
		this.nameEntries = inputs;
	}// --------------------------------------------------------
	   /**
	    * 
	    * Constructor for DataRow initializes internal from the result set 
	    * @param rowNum starts from 0
	    */
	   public DataRow(int rowNum)
	   {     
		   this();
	      this.rowNum = rowNum;
	   }// --------------------------------------------   
	   public DataRow(int rowNum, int size)
	   {     
	     this.rowNum = rowNum;
	     
	     this.positionEntries = new ArrayList<Object>(size);
	     
	     for (int i = 0; i < size; i++)
		 {
			this.positionEntries.add(null);	
		  }
	   }// --------------------------------------------   
	   
	   /**
	    * 
	    * @param aEntry added data
	    */
	   protected void add(Object aEntry)
	   {
	      positionEntries.add(aEntry);
	   }// --------------------------------------------
	   /**
	 * @param aPosition the position
	 * @return the string
	 * @see nyla.solutions.core.data.DataRow#retrieveString(int)
	 */
	   public String retrieveString(int aPosition)
	   {
		 try
		 {
		   
	      Object  o = positionEntries.get(aPosition -1);
	      
	      if(o == null)
	    	  return null;
	      
	      return String.valueOf(o);
		 }
		 catch(ArrayIndexOutOfBoundsException e)
		 {
			 if(aPosition < START_WITH_ZERO)
				 throw new ArrayIndexOutOfBoundsException("Invalid Position "+aPosition+" less than 1 ");
			 
			 throw e;
		 }
	   }// --------------------------------------------
	   /**
		 * @param aPosition the position to get
		 * @param <T> the 
		 * @return the object in the row at the position
		 * @see nyla.solutions.core.data.DataRow#retrieveObject(int)
		 */
	   @SuppressWarnings("unchecked")
	   public <T> T retrieveObject(int aPosition)
	   {
	      return (T)positionEntries.get(aPosition -1);
	   }// --------------------------------------------------------
	   /**
	 * @param aPosition the position
	 * @param obj  the object to assign
	 * @see nyla.solutions.core.data.DataRow#assignObject(int, java.lang.Object)
	 */
	   public void assignObject(int aPosition , Object obj)
	   {
		   if(positionEntries.size() < aPosition)
		   {
			   for (int i = positionEntries.size(); i < aPosition; i++)
				 {
					this.positionEntries.add(null);	
				  }
		   }
		   
			 
		   positionEntries.set(aPosition-1, obj);
	   }// --------------------------------------------------------
	   
		/**
		 * @param key the key to store
		 * @param value the value for the key
		 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
		 */
		public void assignObject(String key, Serializable value)
		{
			if(nameEntries == null)
				nameEntries = new  HashMap<String, Serializable>();
			
			nameEntries.put(key, value);
		}// --------------------------------------------------------
	   
	   /**
	 * @param aPosition the positions
	 * @return the double at the position
	 * @see nyla.solutions.core.data.DataRow#retrieveDouble(int)
	 */
	   public Double retrieveDouble(int aPosition)
	   {
	      Object  o = positionEntries.get(aPosition -1);
	      
	      if(o ==null)
	         return null;
	      
	      String text = String.valueOf(o);
	      
	      if(text == null || text.length() == 0)
	    	  return null;
	      
	      return  Double.valueOf(text);
	   }// --------------------------------------------
	   /**
	 * @param aPosition the position
	 * @return the integer at the position
	 * @see nyla.solutions.core.data.DataRow#retrieveInteger(int)
	 */
	   public Integer retrieveInteger(int aPosition)
	   {
	      Object  o = positionEntries.get(aPosition -1);
	      
	      if(o ==null)
	         return -1;
	      
	      String text = String.valueOf(o);
	      
	      if(text == null || text.length() == 0)
	    	  return -1;
	      
	      return Integer.valueOf(text);
	   }// --------------------------------------------
	   
	   /**
	 * @param aPosition the row column positions
	 * @return object at the position
	 * @see nyla.solutions.core.data.DataRow#retrieveDate(int)
	 */
	   public Date retrieveDate(int aPosition)
	   {
	      Object  o = positionEntries.get(aPosition -1);
	      
	      if(o ==null)
	         return null;
	            
	     return (Date)o;
	  
	   }// --------------------------------------------
	  
	   
    /**
	 * @return array of string for the rows
     * @see nyla.solutions.core.data.DataRow#getStrings()
	 */
	   public String[] getStrings()
	   {
		   String [] strings;
		   
	      try
	      {
	         if(this.positionEntries == null || this.positionEntries.isEmpty())
	            return null;
	         
	         strings = new String[this.size()];
	         
	         for(int i = 0;i < this.size();i++)
	         {
	            strings[i] = this.retrieveString(i+1);
	         }
	         
	         return strings;
	      }
	      catch (RuntimeException e)
	      {        
	         throw new RuntimeException("entries="+positionEntries+" ",e);
	      }
	   }// --------------------------------------------
	  

		/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return new StringBuilder("DataRow [")
				.append(" positionEntries=")
				.append(String.valueOf(positionEntries)).append(", nameEntries=").append(nameEntries).append(", rowNum=").append(rowNum).append("]").toString();
	}// --------------------------------------------------------
		/**
	    * 
	    * @return entries.size()
	    */
	   public int size()
	   {
	      if(positionEntries == null)
	         return 0;
	      
	      return positionEntries.size();
	   }//--------------------------------------------
	   
	   /**
		 * @return the number of the row
		 * @see nyla.solutions.core.data.DataRow#getRowNum()
		 */
		public int getRowNum()
		{
			return rowNum;
		}// --------------------------------------------------------
		/**
		 * @param rowNum the row number
		 * @see nyla.solutions.core.data.DataRow#setRowNum(int)
		 */
		public void setRowNum(int rowNum)
		{
			this.rowNum = rowNum;
		}// --------------------------------------------------------
		/**
		 * @return arry entries of the row
		 * @see nyla.solutions.core.data.DataRow#toArray()
		 */
		public Object[] toArray()
		{
			return positionEntries.toArray();
		}
		
		
		
		/**
		 * @param key the key
		 * @return the string for
		 * @see java.util.HashMap#get(java.lang.Object)
		 */
		public String retrieveString(String key)
		{
			if(nameEntries == null)
				return null;
			
			return (String)nameEntries.get(key);
		}// --------------------------------------------------------
		/**
		 * @param key the key
		 * @return date the for the key
		 * @see java.util.HashMap#get(java.lang.Object)
		 */
		public Date retrieveDate(String key)
		{
			if(nameEntries == null)
				return null;
			
			return (Date)nameEntries.get(key);
		}// --------------------------------------------------------
		/**
		 * @param key the key to value
		 * @return double the for the value
		 * @see java.util.HashMap#get(java.lang.Object)
		 */
		public Double retrieveDouble(String key)
		{
			if(nameEntries == null)
				return null;
			
			return (Double)nameEntries.get(key);
		}// --------------------------------------------------------		
		/**
		 * @param key the key
		 * @return the integer for the key
		 * @see java.util.HashMap#get(java.lang.Object)
		 */
		public Integer retrieveInteger(String key)
		{
			if(nameEntries == null)
				return null;
			
			return (Integer)nameEntries.get(key);
		}// --------------------------------------------------------			
		/**
		 * @param <T> the type of the object
		 * @param key the key
		 * @return the object
		 * @see java.util.HashMap#get(java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		public <T> T retrieveObject(String key)
		{
			if(nameEntries == null)
				return null;
			
			return (T)nameEntries.get(key);
		}// --------------------------------------------------------				

		/**
		 * 
		 * @see nyla.solutions.core.data.Mapped#getMap()

		 */
		@Override
		public Map<String, Serializable> getMap()
		{
			
			return this.nameEntries;
		}// --------------------------------------------------------
		/**
		 * 
		 * @see nyla.solutions.core.data.Mapped#setMap(java.util.Map)
		 */
		//@Override
		public void setMap(Map<String, Serializable> map)
		{
			this.nameEntries = map;
		}// --------------------------------------------------------

}