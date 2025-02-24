package nyla.solutions.core.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Named 
implements Comparable<Object>, Serializable, Nameable, Textable
{
   private String name = "";
   static final long serialVersionUID = Named.class.getName().hashCode();

   /**
    * 
    * Constructor for Named initializes internal
    * 
    * data settings.
    */
   public Named()
   {
   }

   public Named(String name)
   {
      this.name = "";
      init(name);

   }

   protected void init(String name)
   {
      setName(name);

   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      if (name == null)
         name = "";

      this.name = name;
   }

   public boolean hasName()
   {

      return !Data.isNull(name);

   }

   public String getText()
   {
      return getName();
   }

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
   }

   public static Collection<Named> sortByName(Collection<Named> namedVOs)
   {

      List<Named> list = null;

      if (namedVOs instanceof List)
         list = (List<Named>) namedVOs;
      else
         list = new ArrayList<Named>(namedVOs);

      Collections.sort(list);

      return list;

   }

   public int hashCode()
   {
      return super.hashCode() + name.hashCode();
   }

   public void copy(Copier aFrom)
   {
      if(aFrom == null)
         return;
      
      Nameable from = (Nameable)aFrom;
      this.name = from.getName();
   }

   /**
    * Calls setName(aText)
    * @param aText the text to set
    */
   public void setText(String aText)
   {
      setName(aText);
   }

}
