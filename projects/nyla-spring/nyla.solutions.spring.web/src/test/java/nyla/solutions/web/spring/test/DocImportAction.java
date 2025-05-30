package nyla.solutions.web.spring.test;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * <pre>
 *  DocImportAction is a web controller. This class act a mediator
 *  for transform HTTP request to an Document object to be process to DocImport Service. 
 *  It also handles request validation/authorization.
 * </pre>
 * 
 * @author Gregory Green
 * @version 1.0
 */
public class DocImportAction extends MultiActionController implements
Controller
{
   /**
    * Display helloword
    * 
    * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
    *      javax.servlet.http.HttpServletResponse)
    */
   public ModelAndView listDocuments(HttpServletRequest httpservletrequest,
                                     HttpServletResponse httpservletresponse) throws Exception
   {
      System.out.println("Hello World");
      ModelAndView mv = new ModelAndView("hello");
      mv.addObject("message", "Hello World!");
      return mv;
   }// --------------------------------------------

   public Collection retrieveDocuments()
   {
      Collection list = new ArrayList();

      return list;

   }// --------------------------------------------

}



