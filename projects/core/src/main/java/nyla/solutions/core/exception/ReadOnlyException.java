package nyla.solutions.core.exception;

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
    * @param field the field
    */
   public ReadOnlyException(String field)
   {
      super(new StringBuilder()
              .append(field)
              .append(" is read only").toString());
   }

}
