package nyla.solutions.web.spring.web;

import java.util.Calendar;

import nyla.solutions.global.util.Debugger;
import nyla.solutions.web.spring.propEditors.CalendarPropertyEditor;
import nyla.solutions.web.spring.propEditors.DatePropertyEditor;
import nyla.solutions.web.spring.propEditors.GregorianCalendarPropertyEditor;

import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * <pre>
 * SpringMVC provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class SpringMVC
{
   
   /**
    * @param binder
    */
   public static void initBinder(ServletRequestDataBinder binder)
   {
      Debugger.println("initBinder " );
      
      binder.registerCustomEditor(Calendar.class, new CalendarPropertyEditor());
      
      binder.registerCustomEditor(java.util.Date.class, new DatePropertyEditor());
      
      binder.registerCustomEditor(java.util.GregorianCalendar.class, new GregorianCalendarPropertyEditor());
      
   }

}
