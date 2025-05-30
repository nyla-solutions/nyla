package nyla.solutions.web.spring.propEditors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nyla.solutions.global.util.Debugger;
import nyla.solutions.global.util.Text;



/**
 * <pre>
 *  DatePropertyEditor provides a set of functions to
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class DatePropertyEditor extends PropertyEditorSupport
{
   public DatePropertyEditor()
   {      
   }
   public String getAsText() 
   {

      Date value = (Date) getValue();
      Debugger.println(this, "value="+value);
      return value != null ? this.dateFormat.format(value) : "";
   }

   public void setAsText(String text) throws IllegalArgumentException 
   {
      if (Text.isNull(text)) 
      {
        setValue(null);
      } 
      else 
      {
        try 
        {
          setValue(this.dateFormat.parse(text));
         } 
        catch (ParseException ex) 
        {
            throw new IllegalArgumentException("Expected format: dd/MM/yyyy Invalid Date: "+text);
        }
      }
   }

   private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

}
