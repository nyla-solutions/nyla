package nyla.solutions.core.patterns.decorator;

import nyla.solutions.core.util.Text;

/**
 * Textable implementation interface to return the toString of a target object.
 * @author Gregory Green
 *
 */
public class ToStringTextDecorator implements TextDecorator<Object>
{
   /**
    * 
    * Constructor for ToStringWrapperText initializes internal
    */
   public ToStringTextDecorator()
   {   
   }//--------------------------------------------

   /**
    * 
    * Constructor for ToStringWrapperText initializes internal 
    * @param target
    */
   public ToStringTextDecorator(Object target)
   {
      super();
      this.setTarget(target);
   }//--------------------------------------------

   /**
    * 
    * @return the targeted object
    */
   public Object getTarget()
   {
      return target;
   }//--------------------------------------------

   /**
    * 
    * @param target the target object
    */
   public void setTarget(Object target)
   {

      this.target = target;
   }//--------------------------------------------
   public String getText()
   {
     return Text.toString(this.target);
   }//--------------------------------------------

   private Object target = null;

}
