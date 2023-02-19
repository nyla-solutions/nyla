package nyla.solutions.core.data;

import java.io.Serializable;


/**
 * 
 * <pre>
 * Criteria is a value object representation of data that
 * can be selected from a datastore and has an integer primary key 
 * identifier.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class Criteria extends Data 
implements Comparable<Object>, PrimaryKey, Cloneable, Copier, Serializable, Identifier
{
   private String id;
   static final long serialVersionUID = Criteria.class.getName().hashCode();

   /**
    * 
    * Constructor for Criteria initializes primaryKey to -1.
    */

   protected Criteria()
   {
      primaryKey = -1;
   }

   /**
    * 
    * Constructor for Criteria initializes internal 
    * data settings.
    * @param aPK the primary key
    * @throws IllegalArgumentException
    */

   public Criteria(int aPK) throws IllegalArgumentException
   {
	   if(aPK < 0)
		{
		   this.primaryKey = Data.NULL;
		}
	   else
	   {
		   this.primaryKey = aPK;
		   this.id = String.valueOf(this.primaryKey);
	   }
   }

   /**
    * 
    * Constructor for Criteria initializes primaryKey to aPK
    * @param criteria the primary key
    * @throws IllegalArgumentException When aPK is invalid
    * 
    */   
   public Criteria(Criteria criteria) throws IllegalArgumentException
   {

      primaryKey = -1;

      if(criteria == null )
      {
    	  return;
      }
      
      this.id = criteria.id;
      this.primaryKey = criteria.primaryKey;

   }


   /**
    * 
    * Constructor for Criteria initializes internal 
    * data settings.
    * @param aPK the primary key the set
    * @throws IllegalArgumentException
    */

   public Criteria(String aPK) throws IllegalArgumentException
   {
     this.id = aPK;
   }


   /**

    * 
    * @return the primary key
    * @see PrimaryKey#getPrimaryKey()
    */
   public int getPrimaryKey()
   {

      return primaryKey;

   }

   public Criteria clone() throws CloneNotSupportedException
   {
      return (Criteria)super.clone();    
   }


   /**
    * Set primary key
    * @param primaryKey the primary key to set
    * @throws IllegalArgumentException primary key is &lt; 1
    */
   public void setPrimaryKey(int primaryKey) 
   throws IllegalArgumentException
   {

      if (primaryKey <= NULL)
      {
         this.primaryKey = Data.NULL;
      }
      else
      {

         this.primaryKey = primaryKey;

         //resetNew();

         return;
      }
   }

   public void setPrimaryKeyInteger(Integer aInteger)
   {
      if (aInteger == null)
         throw new IllegalArgumentException(
         "aInteger required in Criteria.setPrimaryKey");
      
      setPrimaryKey(aInteger.intValue());
   }


   /**
    * Set primary key
    * @param aCriteria the primary key to set
    * @throws IllegalArgumentException primary key is &lt; 1
    */
   protected void setPrimaryKeyObject(PrimaryKey aCriteria)
   throws IllegalArgumentException
   {

      if (aCriteria == null)
      {
         return;
      }
      else
      {

         setPrimaryKey(aCriteria.getPrimaryKey());
         return;
      }
   }


   /**
    * @param object the object to compare
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    * @return new Integer(getPrimaryKey())).compareTo(new Integer(vo
    * .getPrimaryKey())
    */

   public int compareTo(Object object)
   {

      if (!(object instanceof Criteria))
      {
         return -1;
      }
      else
      {

         Criteria vo = (Criteria) object;

         return Integer.compare(getPrimaryKey(), vo.getPrimaryKey());
      }

   }


   protected void setPrimaryKeyString(String primaryKey)

         throws IllegalArgumentException

   {

      setPrimaryKey(Integer.parseInt(primaryKey));

   }

   /**
    * Set primaryKey = Data.NULL
    *
    */
   public void resetNew() 
   {
       this.primaryKey = Data.NULL;
       
   }

   /**
    * 
    * 
    * @see nyla.solutions.core.data.Copier#copy(nyla.solutions.core.data.Copier)
    */
   public void copy(Copier aOther)
   {

      Criteria other = (Criteria)aOther;

      if(other == null)
         return;

      

      if(this == other)
         return;

     if(other.primaryKey > 0) 
       this.primaryKey = other.primaryKey;

   }

   /**
    * 
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object o)

   {

      if (o == null)
         return false;

      if (o instanceof Criteria)
      {

         Criteria otherKey = (Criteria) o;
         return primaryKey == otherKey.primaryKey;
      }

      else
      {
         return false;
      }

   }

   /**
    *
    * @return  new Integer(primaryKey)).hashCode()
    * @see java.lang.Object#hashCode()
    */

   public int hashCode()
   {
      return (Integer.valueOf(primaryKey)).hashCode();
   }

   /**
    * 
    * @return null if primaryKey less than 1 else
    * new Integer(primaryKey)
    */
   public String getId()
   {
     return id;
   }

   /**
    * @return this.getId()
    * @see nyla.solutions.core.data.Mappable#getKey()
    */
   public Object getKey()
   {
      return this.getId();
   }

   
   private int primaryKey = Data.NULL;
   /**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

}
