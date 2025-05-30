package nyla.solutions.spring.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class Violations implements Errors
{

   public void addAllErrors(Errors error)
   {
      // TODO Auto-generated method stub

   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
  public List getAllErrors()
   {
      // TODO Auto-generated method stub
      return null;
   }

   public int getErrorCount()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   public FieldError getFieldError()
   {
      // TODO Auto-generated method stub
      return null;
   }

   public FieldError getFieldError(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }

   public int getFieldErrorCount()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   public int getFieldErrorCount(String field)
   {
      // TODO Auto-generated method stub
      return 0;
   }
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public List getFieldErrors()
   {
      // TODO Auto-generated method stub
      return null;
   }
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public List getFieldErrors(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }
   public Class<?> getFieldType(String field)
   {
      // TODO Auto-generated method stub
      return null;
   }

   public Object getFieldValue(String field)
   {
      return null;
   }

   public ObjectError getGlobalError()
   {
      return null;
   }// --------------------------------------------


   public int getGlobalErrorCount()
   { 
      return errorMap.values().size();
   }
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public List getGlobalErrors()
   {
      
       return (List)errorMap.get(GLOBAL_ERROR_KEY);
   }

   public String getNestedPath()
   {
      return nestedPath;
   }

   
   public String getObjectName()
   {
      // TODO Auto-generated method stub
      return objectName;
   }// --------------------------------------------


   public boolean hasErrors()
   {
      return !errorMap.isEmpty();
   }

   public boolean hasFieldErrors()
   {
      return !errorMap.isEmpty();
   }

   public boolean hasFieldErrors(String field)
   {
      return !errorMap.isEmpty();
   }

   public boolean hasGlobalErrors()
   {
      
      return !errorMap.isEmpty();
   }

   public void popNestedPath() throws IllegalStateException
   {
      // TODO Auto-generated method stub

   }

   public void pushNestedPath(String subPath)
   {
      // TODO Auto-generated method stub

   }

   public void reject(String errorCode) 
   {
      globalErrors.add(errorCode);
      errorMap.put(GLOBAL_ERROR_KEY, globalErrors);

   }

   public void reject(String errorCode, String defaultMessage)
   {
      globalErrors.add(defaultMessage);
      errorMap.put(GLOBAL_ERROR_KEY, globalErrors);

   }

   public void reject(String errorCode, Object[] errorArgs, String defaultMessage)
   {
      globalErrors.add(defaultMessage);
      errorMap.put(GLOBAL_ERROR_KEY, globalErrors);

   }

   public void rejectValue(String field, String errorCode) 
   {
      errorMap.put(field, errorCode);

   }

   public void rejectValue(String field, String errorCode, String defaultMessage) 
   {
      errorMap.put(errorCode, defaultMessage);

   }

   public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) 
   {
      errorMap.put(errorCode, defaultMessage);

   }

   /**
 * @param nestedPath the nestedPath to set
 */
public void setNestedPath(String nestedPath)
{
	this.nestedPath = nestedPath;
}

/**
 * @param objectName the objectName to set
 */
public void setObjectName(String objectName)
{
	this.objectName = objectName;
}

private String nestedPath = null;
private String objectName = null;
private final static String GLOBAL_ERROR_KEY  = "GLOBAL";
   private Collection<String> globalErrors = new ArrayList<String>();
   private Map<String,Object> errorMap = new HashMap<String,Object>();

}
