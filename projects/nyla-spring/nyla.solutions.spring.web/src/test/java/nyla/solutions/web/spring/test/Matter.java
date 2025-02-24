package nyla.solutions.web.spring.test;

/**
 * <pre>
 * Matter provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class Matter
{
   

   /**
    * @return the name
    */
   public final String getName()
   {
      return name;
   }
   /**
    * @return the number
    */
   public final String getNumber()
   {
      return number;
   }// --------------------------------------------

   /**
    * @param name the name to set
    */
   public final void setName(String name)
   {
      if (name == null)
         name = "";
   
      this.name = name;
   }
   /**
    * @param number the number to set
    */
   public final void setNumber(String number)
   {
      if (number == null)
         number = "";
   
      this.number = number;
   }// --------------------------------------------

   
   private String number = "";
   private String name = "";
}
