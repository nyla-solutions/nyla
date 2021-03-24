package nyla.solutions.core.data;


import nyla.solutions.core.util.Text;

import java.io.Serializable;

/**
 * 
 * 
 * <pre>
 * 
 *  Property is a value object representation of a entity Property with
 *  name and a value 
 * </pre>
 * 
 * @author Gregory Green
 * 
 * @version 1.0
 *  
 */

public class Property 
implements Serializable, Mappable<Object,Object>, Comparable<Object>, Cloneable, Copier,
Attribute<Object,Object>
{
   public Property()
   {
   }//--------------------------------------------
   /**
    * 
    * Constructor for Property initializes internal 
    * data settings.
    * @param aName the property name
    * @param aValue the property value
    */
   public Property(String aName,Serializable aValue )
   {
      this.name = aName;
      this.value = aValue;
      
   }//--------------------------------------------
   public Object clone() throws CloneNotSupportedException
   {
      return super.clone();
   }//----------------------------------------
   /**
    * @param other the other property to compare
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    * @throws ClassCastException if the other is not a property
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public int compareTo(Object other)
   {
      Property otherProperty = (Property)other;
      
      if(this.name == null)
    	  return -1;
      
      //compare names
     int compare = this.name.compareTo(otherProperty.getName());
     
     if(compare == 0)
     {
    	 Object value = this.getValue();
    	 if(!(value instanceof Comparable))
    		 return compare;
    	 
    	 return ((Comparable)value).compareTo(otherProperty.getValue());
     }
     
     return compare;
   }//--------------------------------------------
   /**
    * 
    * @return the property name
    */
   public String getName()
   {
      return name;

   }//--------------------------------------------
   /**
    * Set property name
    * @param name name to set
    */
   public void setName(String name)
   {

      if (name == null)
      {
        name = "";
      }

     this.name = name.trim();

   }//--------------------------------------------
   /**
    * 
    * @return the value of the property
    * @see nyla.solutions.core.data.Mappable#getValue()
    */
   public Object getValue()
   {
      return value;
   }//--------------------------------------------
   /**
    * 
    * @param value the property value to set
    */
   public void setValue(Serializable value)
   {
      this.value = value;
   }//--------------------------------------------

   /**
    * 
    * @return name of the property
    * 
    * @see nyla.solutions.core.data.Mappable#getKey()
    *  
    */
   public Object getKey()
   {

      return name;
   }//--------------------------------------------
   /**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Property other = (Property) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}
   /**
    * 
    * @see nyla.solutions.core.data.Copier#copy(nyla.solutions.core.data.Copier)
    */
   public void copy(Copier aFrom)
   {
      if (!(aFrom instanceof Property))
         throw new IllegalArgumentException("aFrom instanceof aFrom required in Property.copy");
      
      
      Property from = (Property)aFrom;
      
      this.name = from.name;
      this.value = from.value;
   }//--------------------------------------------
   /**
    * 
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
     StringBuffer text = new StringBuffer("[")
        .append(getClass().getName()).append("]")
        .append(" name: ").append(name)
        .append(" value: ").append(value);
     
      return text.toString();
   }//----------------------------------------
   /**
    * 
    * @param aValue the property value
    * @return true if string version of the property value
    * equals (ignore case) aValue
    */
   public boolean equalsValueIgnoreCase(Object aValue)
   {
      return String.valueOf(value).equalsIgnoreCase(
             String.valueOf(aValue));
   }//--------------------------------------------
   /**
    * 
    * @return integer type of value
    */
   public Integer getValueInteger()
   {
      String textValue = String.valueOf(value);
      if(Text.isInteger(textValue))
         return Integer.valueOf(textValue);
      
      return null;
      
   }//--------------------------------------------
   /**
    * Set name to key
    * @param key the key to set
    */
   public void setKey(Object key)
   {
      if (key == null)
         throw new IllegalArgumentException("key required in Property.setKey");
      
      this.name = key.toString();
   }//--------------------------------------------
   /**
    * 
    * @param text the text value
    */
   public void setTextValue(String text)
   {
      this.setValue(text);
   }// --------------------------------------------

   /**
    * 
    * @return (String)getValue()
    */
   public String getTextValue()
   {
      return (String)getValue();
   }// --------------------------------------------

   
   private String name = "";
   private Serializable value = "";
   static final long serialVersionUID = Property.class.getName().hashCode();

}

