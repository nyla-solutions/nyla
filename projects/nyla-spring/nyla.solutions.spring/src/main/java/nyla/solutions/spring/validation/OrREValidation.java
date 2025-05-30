package nyla.solutions.spring.validation;

import java.util.Iterator;
import java.util.TreeSet;

import org.springframework.validation.Errors;

import nyla.solutions.core.util.Text;


public class OrREValidation extends AbstractValidation 
{
   /**
    * 
    * @param re the regular expression to map
    */
   public void addRegularExpresion(String re)
   {
      regularExpressions.add(re);
      
   }// --------------------------------------------

   /**
    * 
    *
    * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
    */
   public void validate(Object object, Errors errors)
   {
      
      //get value
      String value = this.retrieveTextValue(object);
      
      //loop thru re
      String re = null;
      for (Iterator<String> i = this.regularExpressions.iterator(); i.hasNext();)
      {
         re = (String) i.next();
         
         if(Text.matches(value, re))
         {
            return; //found match
         }
      }
      
      errors.reject(value); //does not match any of the regular expressionsb
      
   }// --------------------------------------------

   private java.util.Set<String> regularExpressions = new TreeSet<String>();
   


}
