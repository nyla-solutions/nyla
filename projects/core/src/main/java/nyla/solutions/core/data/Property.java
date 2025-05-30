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

public class Property <Key,Value extends Serializable>
implements Serializable, Mappable<Key,Value>, Comparable<Object>, Cloneable, Copier,
Attribute<Key,Value>
{
    private Value value;
   private Key key;
   static final long serialVersionUID = Property.class.getName().hashCode();

    public Property()
   {
   }

   /**
    * 
    * Constructor for Property initializes internal 
    * data settings.
    * @param key the property name
    * @param value the property value
    */
   public Property(Key key,Value value )
   {
      this.key = key;
      this.value = value;
      
   }

   public Object clone() throws CloneNotSupportedException
   {
      return super.clone();
   }
   /**
    * @param other the other property to compare
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    * @throws ClassCastException if the other is not a property
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public int compareTo(Object other)
   {
      Property otherProperty = (Property)other;
      
      if(this.key == null)
    	  return -1;

      String name = this.getName();
      //compare names
     int compare = name.compareTo(otherProperty.getName());
     
     if(compare == 0)
     {
    	 Object value = this.getValue();
    	 if(!(value instanceof Comparable))
    		 return compare;
    	 
    	 return ((Comparable)value).compareTo(otherProperty.getValue());
     }
     
     return compare;
   }

   /**
    * 
    * @return the property name
    */
   public String getName()
   {
      return Text.toString(key);

   }

//   /**
//    * Set property name
//    * @param name name to set
//    */
//   public void setName(String name)
//   {
//      if (name == null)
//      {
//        name = "";
//      }
//
//     this.name = name.trim();
//
//   }

   /**
    * 
    * @return the value of the property
    * @see nyla.solutions.core.data.Mappable#getValue()
    */
   public Value getValue()
   {
      return (Value)value;
   }
   /**
    * 
    * @param value the property value to set
    */
   public void setValue(Value value)
   {
      this.value = value;
   }


   /**
    * 
    * @return name of the property
    * 
    * @see nyla.solutions.core.data.Mappable#getKey()
    *  
    */
   public Key getKey()
   {

      return key;
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
      
      this.key = (Key)from.key;
      this.value = (Value)from.value;
   }

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
   }

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
      
   }

   /**
    * Set name to key
    * @param key the key to set
    */
   public void setKey(Key key)
   {
      if (key == null)
         throw new IllegalArgumentException("key required in Property.setKey");
      
      this.key = key;
   }

//   /**
//    *
//    * @param text the text value
//    */
//   public void setTextValue(String text)
//   {
//      this.setValue(text);
//   }// --------------------------------------------

   /**
    * 
    * @return (String)getValue()
    */
   public String getTextValue()
   {
      return (String)getValue();
   }

   


}

