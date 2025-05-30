package nyla.solutions.spring.validation;

import java.lang.reflect.InvocationTargetException;

import nyla.solutions.core.util.JavaBean;



public abstract class AbstractValidation implements org.springframework.validation.Validator
{

   /**
    * Get a value from a given object
    * @param target the target object to retrieve the value from
    * @return the string value
    * @throws Exception 
    * @throws NoSuchMethodException 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    */
   protected String retrieveTextValue(Object target)    
   {
      if(target instanceof String)
      {
         return (String)target ;
      }
      else
      {
         Object value =  JavaBean.getProperty(target, this.fieldName);
         
         if(value == null)
            return null;
         else
            return value.toString();
         
      }
   }// --------------------------------------------
   /**
    * @return Returns the typeName.
    */
   public String getTypeClassName()
   {
      return typeClassName;
   }//--------------------------------------------
   /**
    * @param typeClassName The typeName class to set.
    */
   public void setTypeClassName(String typeClassName)
   {
      if (typeClassName == null)
         typeClassName = "";

      this.typeClassName = typeClassName;
   }//--------------------------------------------
   /**
    * @return Returns the fieldName.
    */
   public final String getFieldName()
   {
      return fieldName;
   }//--------------------------------------------
   /**
    * @param fieldName The fieldName to set.
    */
   public final void setFieldName(String fieldName)
   {
      if (fieldName == null)
         fieldName = "";

      this.fieldName = fieldName;
   }//--------------------------------------------
   /**
    * 
    * This class supports the validation of any type specified in
    * the type name attribute. 
    * class. 
    * @see org.springframework.validation.Validator#supports(java.lang.Class)
    */
   public boolean supports(Class<?> aClass)
   {      
         return aClass != null && this.typeClassName.equals(aClass.getName());      
   }// -------------------------------------------
   private String typeClassName = String.class.getName();
   private String fieldName = "";
}
