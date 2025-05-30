package nyla.solutions.web.spring.test;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nyla.solutions.global.util.Debugger;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * <pre>
 * MockErrors provides a set of functions to
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class MockErrors implements Errors
{

   /**
    * Constructor for MockErrors initalizes internal 
    * data settings.
    */
   public MockErrors()
   {
      // TODO Auto-generated constructor stub
   }

   /**
    * 
    * @see org.springframework.validation.Errors#addAllErrors(org.springframework.validation.Errors)
    */
   public void addAllErrors(Errors errors)
   {
      // TODO Auto-generated method stub

   }

   /**
    * @return the map
    */
   public final Map getMap()
   {
      return map;
   }

   /**
    * @param map the map to set
    */
   public final void setMap(Map map)
   {
      if (map == null)
   
      this.map = map;
   }// --------------------------------------------


   /**
    * 
    * @see org.springframework.validation.Errors#getAllErrors()
    */
   public List getAllErrors()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getErrorCount()
    */
   public int getErrorCount()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldError()
    */
   public FieldError getFieldError()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldError(java.lang.String)
    */
   public FieldError getFieldError(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldErrorCount()
    */
   public int getFieldErrorCount()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldErrorCount(java.lang.String)
    */
   public int getFieldErrorCount(String field)
   {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldErrors()
    */
   public List getFieldErrors()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldErrors(java.lang.String)
    */
   public List getFieldErrors(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldType(java.lang.String)
    */
   public Class getFieldType(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getFieldValue(java.lang.String)
    */
   public Object getFieldValue(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getGlobalError()
    */
   public ObjectError getGlobalError()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getGlobalErrorCount()
    */
   public int getGlobalErrorCount()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getGlobalErrors()
    */
   public List getGlobalErrors()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getNestedPath()
    */
   public String getNestedPath()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#getObjectName()
    */
   public String getObjectName()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#hasErrors()
    */
   public boolean hasErrors()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#hasFieldErrors()
    */
   public boolean hasFieldErrors()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#hasFieldErrors(java.lang.String)
    */
   public boolean hasFieldErrors(String field)
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#hasGlobalErrors()
    */
   public boolean hasGlobalErrors()
   {
      // TODO Auto-generated method stub
      return false;
   }

   /**
    * 
    * @see org.springframework.validation.Errors#popNestedPath()
    */
   public void popNestedPath() throws IllegalStateException
   {
      // TODO Auto-generated method stub

   }

   /**
    * 
    * @see org.springframework.validation.Errors#pushNestedPath(java.lang.String)
    */
   public void pushNestedPath(String subPath)
   {
      // TODO Auto-generated method stub

   }

   /**
    * 
    * @see org.springframework.validation.Errors#reject(java.lang.String)
    */
   public void reject(String errorCode)
   {
      map.put(errorCode, errorCode);

   }

   /**
    * 
    * @see org.springframework.validation.Errors#reject(java.lang.String, java.lang.String)
    */
   public void reject(String errorCode, String defaultMessage)
   {
      map.put(errorCode, defaultMessage);
   }// --------------------------------------------


   /**
    * 
    * @see org.springframework.validation.Errors#reject(java.lang.String, java.lang.Object[], java.lang.String)
    */
   public void reject(String errorCode, Object[] errorArgs,
                      String defaultMessage)
   {
      map.put(errorCode, Debugger.toString(errorArgs)+defaultMessage);

   }// --------------------------------------------

   /**
    * 
    * @see org.springframework.validation.Errors#rejectValue(java.lang.String, java.lang.String)
    */
   public void rejectValue(String field, String errorCode)
   {
   }// --------------------------------------------


   /**
    * 
    * @see org.springframework.validation.Errors#rejectValue(java.lang.String, java.lang.String, java.lang.String)
    */
   public void rejectValue(String field, String errorCode, String defaultMessage)
   {
      map.put(field,errorCode);

   }

   /**
    * 
    * @see org.springframework.validation.Errors#rejectValue(java.lang.String, java.lang.String, java.lang.Object[], java.lang.String)
    */
   public void rejectValue(String field, String errorCode, Object[] errorArgs,
                           String defaultMessage)
   {
      map.put(field, errorCode+Debugger.toString(errorArgs)+defaultMessage);

   }// --------------------------------------------


   /**
    * 
    * @see org.springframework.validation.Errors#setNestedPath(java.lang.String)
    */
   public void setNestedPath(String nestedPath)
   {
      // TODO Auto-generated method stub

   }
   
   private Map map = new Hashtable();

}
