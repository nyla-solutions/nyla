package nyla.solutions.spring.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import nyla.solutions.core.exception.RequiredException;

/**
 * 
 * <b>ValdiationTask</b> task to validate objects.
 * 
 * This object can be used in the worker queue design pattern
 * @author Gregory Green
 *
 */
public class ValidationTask implements Runnable
{
   public ValidationTask(Validator validator, Object target, Errors errors)
   {
      if(validator == null)
         throw new RequiredException("validator in ValdiationTask.ValdiationTask");
      
      Collection<Validator> collection = new ArrayList<Validator>();
      collection.add(validator);
      
      init(collection, target, errors);
   }// --------------------------------------------

   public ValidationTask(Collection<Validator> validators, Object target, Errors errors)
   {
      init(validators, target, errors);
   }// --------------------------------------------
   /**
    * Initialize the tasks
    * @param validators the validate implementation
    * @param target the target object to test
    * @param error the error 
    */
   private void init(Collection<Validator> validators, Object target, Errors errors)
   {
      if(validators == null)
         throw new RequiredException("validators in ValdiationTask.ValdiationTask");
      
      if(target == null)
         throw new RequiredException("target in ValdiationTask.ValdiationTask");
      
      if(errors == null)
         throw new RequiredException("errors in ValdiationTask.ValdiationTask");
      
      this.errors = errors;
      
      this.validators = validators;
      
      this.target = target;
   }// --------------------------------------------
   /**
    * Use the validate implementation to check the given target object
    *
    * @see java.lang.Runnable#run()
    */
   public void run()
   {
      if(validators == null || target == null)
      {
         throw new IllegalStateException("nothing to validate");
      }
      
      Validator validator = null;
      for (Iterator<Validator> i = validators.iterator(); i.hasNext();)
      {
         validator = (Validator) i.next();
         
         validator.validate(target, errors);
      }
      
   }// --------------------------------------------
   /**
    * @return the target
    */
   public Object getTarget()
   {
      return target;
   }// --------------------------------------------
   /**
    * @param target the target to set
    */
   public void setTarget(Object target)
   {
      this.target = target;
   }// --------------------------------------------


   /**
    * @return the validate implementation
    */
   public Collection<Validator> getValidators()
   {
      return validators;
   }// --------------------------------------------


   /**
    * @param validators the validate implementation to set
    */
   public void setValidators(Collection<Validator> validators)
   {
      this.validators = validators;
   }// --------------------------------------------


   /**
    * @return the errors
    */
   public Errors getErrors()
   {
      return errors;
   }

   /**
    * @param errors the errors to set
    */
   public void setErrors(Errors errors)
   {
      this.errors = errors;
   }

   
   private Object target = null;
   private Collection<Validator> validators = null;
   private Errors errors = null;
}
