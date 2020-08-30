package nyla.solutions.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Named 
implements Comparable<Object>, Serializable, Nameable, Textable
{

   /**
    * 
    * Constructor for Named initializes internal
    * 
    * data settings.
    * 
    * 
    *  
    */

   public Named()

   {

   }//--------------------------------------------

   public Named(String aName)

   {

      name = "";

      init(aName);

   }//--------------------------------------------
   protected void init(String aName)
   {

      setName(aName);

   }//--------------------------------------------

   public String getName()

   {

      return name;

   }

   public void setName(String aName)
   {
      if (aName == null)
         aName = "";

      name = aName;
   }//--------------------------------------------

   public boolean hasName()
   {

      return !Data.isNull(name);

   }//--------------------------------------------

   public String getText()

   {

      return getName();

   }//--------------------------------------------

   public int compareTo(Object object)
   {

      if (object == null || !(object instanceof Named))

      {

         return -1;

      }
      else

      {

         Named vo = (Named) object;

         return getName().compareTo(vo.getName());

      }

   }

   public boolean equals(Object object)

   {

      if (super.equals(object))

         return true;

      if (object == null || !(object instanceof Named))

      {

         return false;

      }
      else

      {

         Named vo = (Named) object;

         return getName().equals(vo.getName()) && super.equals(vo);

      }

   }// --------------------------------------------------------
   public static Collection<Named> sortByName(Collection<Named> aNamedVOs)
   {

      List<Named> list = null;

      if (aNamedVOs instanceof List)

         list = (List<Named>) aNamedVOs;

      else

         list = new ArrayList<Named>(aNamedVOs);

      Collections.sort(list);

      return list;

   }//--------------------------------------------
   public int hashCode()
   {
      return super.hashCode() + name.hashCode();

   }//--------------------------------------------
   public void copy(Copier aFrom)
   {
      if(aFrom == null)
         return;
      
      Nameable from = (Nameable)aFrom;
      this.name = from.getName();
   }//--------------------------------------------
   /**
    * Calls setName(aText)
    * @param aText the text to set
    */
   public void setText(String aText)
   {
      setName(aText);
   }
   private String name = "";
   static final long serialVersionUID = Named.class.getName().hashCode();
}
