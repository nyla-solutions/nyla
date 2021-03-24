package nyla.solutions.core.data;

import nyla.solutions.core.util.Debugger;

import java.io.Serializable;
import java.util.*;


/**

 * <b>Data</b> represents a Serializable data type.
 * @author Gregory Green
 * @version 1.0 

 */
public abstract class Data 
implements Serializable
{

   /**
	 * 
	 */
	private static final long serialVersionUID = -2411033885084918560L;



	/**
    * Represent a true value
    */
   public static final int NULL = -1;

   

   /**
    * Represent a true value
    */
   public static final String TRUE = "T";

   /**
    * Represent a flag value
    */
   public static final String FALSE = "F";

   

   /**
    * Represent a true value
    */
   public static final String YES = "Y";

   

   /**

    * Represent a flase value

    */

   public static final String NO = "N";

   



   /**

    * Determines whether the provided str is equal to null
    * or the length is equal to zero
    * @param text the text to text
    * @return true when str is null or length = 0
    */

   public static boolean isNull(String text)
   {

      return text == null || text.trim().length() == 0 ||

            "null".equals(text.trim());

   } //---------------------------------------------

   /**
    * @return the string representation of a object
    */
   public String toString()

   {

      return Debugger.toString(this);

   }

   /**
    * Sort a list 
    * @param <T> the type
	 * @param aVOs the value objects
	 * @return collection the sorted criteria
    */
   public static <T> Collection<T> sortByCriteria(Collection<T> aVOs)
   {

      final List<T> list;

      if (aVOs instanceof List)
         list = (List<T>) aVOs;
      else
         list = new ArrayList<T>(aVOs);

      Collections.sort(list, new CriteriaComparator());

      return list;

   } //-----------------------------------------------   

   /**
     * Sort a list by a date property (Updateable) 
	 * @param collection the collection 
	 * @param <T> THE TYPE
	 * @return COLLECTION OF type stored

       */
   public static<T> Collection<T> sortByUpdateDate(Collection<T> collection)
   {

       List<T> list = collection instanceof List ? (List<T>) collection : new ArrayList<T>(collection); 

      Collections.sort(list, new UpdateDateComparator());

      return list;

   } //-----------------------------------------------       

   /**
    * Comparator for the status active flag
    */

   public static class CriteriaComparator implements Comparator<Object>, Serializable
   {

      /**
       *  implementation for the Comparator interface
       *  @param first the first object to be compared
       *  @param second the second object to be compared to
       * @return 0 when equals &lt; 0 less &gt;0 greater than

       *  @throws ClassCastException if first or second is not an instance of this class

       */

      public int compare(Object first, Object second)
      {

         if (first == null)

            return 1;

         Criteria vo1 = (Criteria) first;

         Criteria vo2 = (Criteria) second;

         return Integer.compare(vo1.getPrimaryKey(), vo2.getPrimaryKey());
   
      }
      
      static final long serialVersionUID = CriteriaComparator.class.getName()
      .hashCode();
   } //-------------------------------------------------------------- 

   /**
    * Comparator for the status active flag
    */

   public static class UpdateDateComparator implements Comparator<Object>, Serializable
   {

      /**
       *  implementation for the Comparator interface
       *  @param first the first object to be compared
       *  @param second the second object to be compared to
       * @return 0 when equals &lt; 0 less &gt; 0 greater than
       *  @throws ClassCastException if first or second is not an instance of this class
       */
      public int compare(Object first, Object second)
      {
         if (first == null)
            return 1;
         
         if (((Updateable)first).getUpdateDate() == null)
            return -1;

         return ((Updateable)first).getUpdateDate().compareTo(((Updateable)second).getUpdateDate());

      }
      
      static final long serialVersionUID = UpdateDateComparator.class.getName()
      .hashCode();
   } //--------------------------------------------------------------





}

