package nyla.solutions.web.spring.web;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nyla.solutions.global.data.Data;
import nyla.solutions.global.exception.SystemException;
import nyla.solutions.global.operations.logging.Log;
import nyla.solutions.global.util.Debugger;
import nyla.solutions.global.util.Text;
import nyla.solutions.web.spring.propEditors.CalendarPropertyEditor;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


/**
 * <pre>
 * AbstractDispatchAction provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public abstract class AbstractDispatchAction extends MultiActionController implements Controller
{  
   /**
    * @param aRequest
    * @param view
    * @return
    */
   protected String getRequiredParameter(String aParameterName, HttpServletRequest aRequest, ModelAndView view)
   {
      //get study number
      String studyNumber = aRequest.getParameter(aParameterName);     
      
      //if neither blank set error message
      
      if(Text.isNull(studyNumber))
      {
         addError(view, aParameterName+" required");
      }
      return studyNumber;
   }// --------------------------------------------
   
   protected int getRequiredInt(String aParameterName, HttpServletRequest aRequest, ModelAndView view)
   {
      int value = Data.NULL;
      String text = this.getRequiredParameter(aParameterName, aRequest, view);
      
      if(Text.isInteger(text))
         value = Integer.valueOf(text).intValue();
      
      return value;
   }// --------------------------------------------
   protected Calendar getRequiredCalendar(String aParameterName, HttpServletRequest aRequest, ModelAndView view)
   {
      Calendar cal = null;
      
      String text = this.getRequiredParameter(aParameterName, aRequest, view);
      
      if(Text.isNull(text))
         return null;
      
      Date date = Text.toDate(text);
      
      if(date != null)
      {
         cal = Calendar.getInstance();
         cal.setTime(date);
      }
      
      return cal;
   }// --------------------------------------------
   
   
   /**
    * 
    * @param aParameterName
    * @param aRequest
    * @param view
    * @return
    */
   protected boolean getRequiredBoolean(String aParameterName, HttpServletRequest aRequest, ModelAndView view)
   {
      String text = this.getRequiredParameter(aParameterName, aRequest, view);
            
      return Boolean.valueOf(text).booleanValue();
   }// --------------------------------------------
   protected void sendRedirect(HttpServletResponse aResponse, String aURL)
   {
         try
         {
            aResponse.sendRedirect(aURL);
         }
         catch (IOException e)
         {
            throw new SystemException(Debugger.stackTrace(e));
         }

   }// --------------------------------------------

   protected void setDate(ModelAndView aView, String aName, Calendar aCalendar)
   {
      CalendarPropertyEditor c = new CalendarPropertyEditor();
      c.setValue(aCalendar);
      if(aCalendar != null)
          aView.addObject(aName,aCalendar.getTime());
   }// --------------------------------------------

   protected boolean hasErrors(ModelAndView aView)
   {
      Object error = aView.getModelMap().get("error");
      
      return error != null && error.toString().length() > 0;
   }// --------------------------------------------

   protected void addError(ModelAndView aView, String aErrorMessage)
   {
      String error = (String)aView.getModelMap().get("error");
      
      
      if(error != null && error.length() > 0)
         error = error+", "+aErrorMessage;
      else
         error = aErrorMessage;
      
      aView.addObject("error",error);
   }// --------------------------------------------

   /**
    * @return the successView
    */
   public String getSuccessView()
   {
      return successView;      
   }// --------------------------------------------
   protected void initBinder(HttpServletRequest request,
                             ServletRequestDataBinder binder)
                             throws Exception 
   {
                             
      SpringMVC.initBinder(binder);
   }// --------------------------------------------
   /**
    * @param successView the successView to set
    */
   public void setSuccessView(String successView)
   {
      if (successView == null)
         successView = "";
   
      this.successView = successView;
   }// --------------------------------------------

   /**
    * @return the errorView
    */
   public final String getErrorView()
   {
      return errorView;
   }
   /**
    * @param errorView the errorView to set
    */
   public final void setErrorView(String errorView)
   {
      if (errorView == null)
         errorView = "";
   
      this.errorView = errorView;
   }// --------------------------------------------
   /**
    * 
    * @return the success view
    */
   protected ModelAndView nextView()
   {
      if(successView ==null || successView.length() == 0)
         return null;
      
      return new ModelAndView(successView);
   }// --------------------------------------------

   protected ModelAndView errorView()
   {
      if(errorView ==null || errorView.length() == 0)
         return null;
      
      return new ModelAndView(errorView);
   }// --------------------------------------------
   protected Log logger = Debugger.getLog(getClass());
   
   private String errorView = "";
   private String successView = "";
}
