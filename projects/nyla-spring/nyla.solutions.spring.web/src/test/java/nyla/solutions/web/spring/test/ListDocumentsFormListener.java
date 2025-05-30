package nyla.solutions.web.spring.test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import nyla.solutions.global.util.Debugger;
import nyla.solutions.web.spring.web.AbstractFormListener;

import org.springframework.web.servlet.ModelAndView;

/**
 * <pre>
 * ListDocumentsFormListener form listener
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class ListDocumentsFormListener extends AbstractFormListener
{

      public ModelAndView onSubmit(Object command) throws ServletException
      {
         if (command == null)
            throw new IllegalArgumentException(
            "command required in DocImportAction.onSubmit");
         
         Matter matter = (Matter)command;

         Debugger.dump(matter);
         
         
         return super.nextView();
      }// --------------------------------------------

      /**
       * 
       * @param request HTTP reques
       * @return a new matter object
       * @throws ServletException
       */
      protected Object formBackingObject(HttpServletRequest request) throws ServletException
      {
         Matter matter = new Matter();
         return matter;
      }// --------------------------------------------

}
