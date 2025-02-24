package nyla.solutions.xml;

import nyla.solutions.core.data.Textable;

/**
 * Converts a target object into XML
 * @author Gregory Green
 *
 */
public class XmlReflection implements Textable
{
   /**
    * @return the target object
    */
   public Object getTarget(Object target)
   {
      return target;
   }//--------------------------------------------

   /**
    * Set the target
    * @param target the target
    */
   public void setTarget(Object target)
   {
      this.target = target;
   }//--------------------------------------------
   /**
    *
    * @return return XML.getInterpreter().toXML(target)
    */
   public String getText()
   {          
         return XML.getInterpreter().toXML(target);

   }//--------------------------------------------
   private Object target = null;
}
