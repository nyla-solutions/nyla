package nyla.solutions.web.spring.propEditors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import nyla.solutions.global.util.Debugger;
import nyla.solutions.global.util.Text;


/**
 * <pre>
 *  CalendarPropertyEditor provides a set of functions to
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class GregorianCalendarPropertyEditor extends PropertyEditorSupport
{
   public GregorianCalendarPropertyEditor()
   {
   }
   public String getAsText() 
   {
      GregorianCalendar value = (GregorianCalendar) getValue();
      
      Debugger.println(this, "value="+value);
      if(value == null)
         return "";
      
      return this.dateFormat.format(value.getTime());
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
           GregorianCalendar cal = (GregorianCalendar)GregorianCalendar.getInstance();
           
          cal.setTime(this.dateFormat.parse(text));
          setValue(cal);
         } 
        catch (ParseException ex) 
        {
            throw new IllegalArgumentException("Expected format: mm/dd/yyyy Invalid calendar: "+text);
        }
      }
   }

   private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

}
