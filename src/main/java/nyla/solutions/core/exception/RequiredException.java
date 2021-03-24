package nyla.solutions.core.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * RequiredException used to indicate that a given field is required.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class RequiredException extends DataException
{
   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = -5598182068206670293L;

   /**
    * Constructor for RequiredException initializes internal 
    * data settings.
    * @param aField the field that is required
    */
   public RequiredException(String aField)
   {
      if(aField == null)
         aField = "";
      
      Map<Object,Object>  map = new HashMap<Object,Object> (1);
      map.put("field", aField);
      try
      {
         formatMessage("RequiredException.required", map);   
      }
      catch(Exception e)
      {
         setMessage(aField+" required");
      }
      
   }// --------------------------------------------
   
}
