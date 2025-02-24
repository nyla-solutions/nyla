package nyla.solutions.web.spring.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import nyla.solutions.global.operations.logging.Log;
import nyla.solutions.global.util.Debugger;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * <pre>
 * AbstractListener provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public abstract class AbstractFormListener 
//extends Simple
{
   public ModelAndView nextView()
   {
      return new ModelAndView(new RedirectView(this.getSuccessView()));   
   }// --------------------------------------------
   
   
   /**
    * 
    * 
    * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(java.lang.Object)
    */
   public abstract ModelAndView onSubmit(Object command) throws ServletException;
   
   
   protected abstract Object formBackingObject(HttpServletRequest request) 
   throws ServletException;
   
   protected void initBinder(HttpServletRequest request,
                             ServletRequestDataBinder binder)
                             throws Exception 
   {
      SpringMVC.initBinder(binder);
   }// --------------------------------------------
   
   /**
 * @return the successView
 */
public String getSuccessView()
{
	return successView;
}


/**
 * @param successView the successView to set
 */
public void setSuccessView(String successView)
{
	this.successView = successView;
}

private String successView = null;
   protected Log logger = Debugger.getLog(getClass());
}
