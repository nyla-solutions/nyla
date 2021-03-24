package nyla.solutions.core.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * ReadOnlyException used to indicate that a given field is read only.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class ReadOnlyException extends DataException
{

   /**
    * Comment for <code>serialVersionUID</code>
    */
   private static final long serialVersionUID = -4137544711334684776L;

   /**
    * Constructor for RequiredException initializes internal 
    * data settings.
    * @param aField the field
    */
   public ReadOnlyException(String aField)
   {
      if(aField == null)
         aField = "";
      
      Map<Object,Object> map = new HashMap<Object,Object> (1);
      map.put("field", aField);
      formatMessage("ReadOnlyException.message", map);
   }// --------------------------------------------
   
}
