package nyla.solutions.core.data;

/**
 * <pre>
 * Status is a value object representation of an object that has a status
 *  state.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */

@SuppressWarnings("rawtypes")
public class Status implements PrimaryKey, Mappable, Comparable, Nameable
{
   /**
    * Constructor for Priority initializes internal 
    * data settings.
    * 
    */

   public Status()

   {

      super();

   }//--------------------------------------------

   /**

    * 

    * @see nyla.solutions.core.data.PrimaryKey#getPrimaryKey()

    */

   public int getPrimaryKey()

   {

      return primaryKey;

   }//--------------------------------------------

   

   /**

    * @return Returns the name.

    */

   public String getName()

   {

      return name;

   }//--------------------------------------------
   /**
    * Set name to key
    * @param key the key to set
    */
   public void setKey(Object key)
   {
      if (key == null)
         throw new IllegalArgumentException("key required in Status.setKey");
      
      this.name = key.toString();
   }//--------------------------------------------
   /**

    * @param name The name to set.

    */

   public void setName(String name)

   {

      if (name == null)

         name = "";



      this.name = name;

   }//--------------------------------------------

   /**

    * @param primaryKey The primaryKey to set.

    */

   public void setPrimaryKey(int primaryKey)

   {

      this.primaryKey = primaryKey;

   }//--------------------------------------------

   /**

    * @return getName

    * @see nyla.solutions.core.data.Mappable#getValue()

    */

   public Object getValue()

   {

      return String.valueOf(primaryKey);

   }//--------------------------------------------

   public void setValue(Object aValue)

   {

      if(aValue == null)

         return;

      

      primaryKey =Integer.parseInt(aValue.toString());

   }//--------------------------------------------   

   /**
    * @return new Integer(primaryKey)
    * @see nyla.solutions.core.data.Mappable#getKey()
    */
   public Object getKey()
   {

     return getName();

   }//--------------------------------------------

   public void copy(Status aStatus)

   {

      if(aStatus== null )

	 return;



      if(aStatus== this )

         return;



      this.primaryKey = aStatus.primaryKey;

      this.name = aStatus.name;

   }//--------------------------------------------
   /**
    * name.compareTo(other.getName())
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object aOther)
   {
      if(aOther == null )
         return 1;
      
      if(aOther == this)
         return 0;
      
      Nameable other = (Nameable)aOther;
      
      return name.compareTo(other.getName());
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
	result = prime * result + primaryKey;
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
	Status other = (Status) obj;
	if (name == null)
	{
		if (other.name != null)
			return false;
	}
	else if (!name.equals(other.name))
		return false;
	if (primaryKey != other.primaryKey)
		return false;
	return true;
}

/**
    * 
    * @see java.lang.Object#toString()
    */
   public String toString()
   {  
      StringBuffer text = new StringBuffer("[")
      .append(getClass().getName()).append("] ")
      .append( "primaryKey=")
      .append(primaryKey)
      .append(" name=").append(name);
      return text.toString();
   }//--------------------------------------------
   public void copy(Copier aFrom)
   {
      if(aFrom == null)
         return;
      
      primaryKey = ((PrimaryKey)aFrom).getPrimaryKey();
      name = ((Nameable)aFrom).getName();
   }//--------------------------------------------
   private int primaryKey  = Data.NULL;

   private String name = "";
   static final long serialVersionUID = Status.class.getName().hashCode();

}

