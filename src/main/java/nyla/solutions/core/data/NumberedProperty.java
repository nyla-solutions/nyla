package nyla.solutions.core.data;

import java.io.Serializable;

/**
 * <pre>
 * NumberedProperty represents a number name/value object
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class NumberedProperty extends Property<String,Integer>
implements Comparable<Object>, Copier, Numbered
{
	public NumberedProperty(String name, Integer value)
	{
		super(name, value);
	}
	
   public NumberedProperty(int number)
	{
		super(null,number);
	}

/**
    * 
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object aOther)
   {
      NumberedProperty other = (NumberedProperty)aOther;
      
      //compare names
      int comparedName = other.getName().compareTo(this.getName());
      if(comparedName != 0)
         return comparedName;
      
      //compare numbers
      Integer othernumber = other.getValueInteger();
      
      return Integer.compare(this.getValueInteger(), othernumber.intValue());
   }


/**
    * 
    * @see nyla.solutions.core.data.Copier#copy(nyla.solutions.core.data.Copier)
    */
   public void copy(Copier aFrom)
   {
      if (aFrom == null)
         throw new IllegalArgumentException(
         "aFrom required in NumberedProperty.copy");
      
      if (!(aFrom instanceof NumberedProperty))
         throw new IllegalArgumentException(
         "aFrom instanceof NumberedProperty required in NumberedProperty.copy");
      
      NumberedProperty numberedProperty = (NumberedProperty)aFrom;
      this.setValue(numberedProperty.getValue());
      this.setName(numberedProperty.getName());
      this.setNumber(numberedProperty.getNumber());
   }

    void setName(String name) {

    }

    /**
    * Constructor for NumberedProperty initializes internal 
    * data settings.
    * 
    */
   public NumberedProperty()
   {
      super();
   }
   
   /**
    * @return Returns the number.
    */
   public int getNumber()
   {
	   Object value = this.getValue();
	   if(value == null)
		   return 0;
	   
	   if(value instanceof String && ((String)value).length() == 0)
		   return 0;
	   
	   Integer v = Integer.valueOf(this.getValue().toString());
	   
	   if(v == null)
		   return 0;
	   
      return v.intValue();
   }
   /**
    * @param number The number to set.
    */
   public void setNumber(int number)
   {
     this.setValue(Integer.valueOf(number));
   }

   static final long serialVersionUID = NumberedProperty.class.getName()
   .hashCode();
}
