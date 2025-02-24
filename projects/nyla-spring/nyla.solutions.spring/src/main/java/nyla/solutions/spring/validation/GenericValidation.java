package nyla.solutions.spring.validation;


import org.springframework.validation.Errors;

import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;


/**
 * <pre>
 * GenericValidation  - spring validator implementation.
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public class GenericValidation extends AbstractValidation 
{
	   static final long serialVersionUID = GenericValidation.class.getName().hashCode();
	   private String regExp = "";
	   private String validateFlag = "v_";
	   private boolean required = false;
	   private Integer minLength = null;
	   private Integer maxLength = null;
	   
	

   /**b
    * Constructor for Validation initializes internal 
    * data settings.
    * 
    */
   public GenericValidation()
   {
      super();
   }//--------------------------------------------

   

   /**
    * 
    *   Test min and max lengths, required fields and regular expressions.
    * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
    */
   public void validate(Object target, Errors errors)
   {
      Debugger.println(this, "validate()");
      //Debugger.dump(this);
      
     
      final String value = retrieveTextValue(target);
      Debugger.println(this,"validating value=" + value + " against=" + this.regExp);

      
      if(this.isRequired() && (value == null || value.length() == 0))
      {
         errors.reject(this.getRequiredErrorCode());
      }
      
      if(this.minLength != null && (value == null || value.length() < minLength.intValue()))
      {
         errors.reject(this.getMinLengthErrorCode(), "Value must be less than "+minLength);
      }
      
      if(this.maxLength != null && (value == null || value.length() >  maxLength.intValue()))
      {
         errors.reject(this.getMaxLengthErrorCode(), "Value must be greater than "+maxLength);
      }
      
      if (!Text.matches(value,regExp))
      {
            String errorMessage = value;
            
            errors.reject(this.getRegExpErrorCode(), errorMessage);
            Debugger.printWarn(this,errorMessage);
                        
                        
      }
               
   }// --------------------------------------------

   /**
    * @return Returns the format.
    */
   public String getRegExp()
   {
      return regExp;
   }
   /**
    * @param re The format to set.
    */
   public void setRegExp(String re)
   {
      if (re == null)
    	  this.regExp = "";
      else
    	  this.regExp = re;
   }// --------------------------------------------
   /**
    * @return Returns the maxLength.
    */
   public Integer getMaxLength()
   {
      return maxLength;
   }//--------------------------------------------
   /**
    * @param maxLength The maxLength to set.
    */
   public void setMaxLength(String maxLength)
   {   
      if(Text.isNull(maxLength))
         setMaxLength((Integer)null);
      
      setMaxLength(Integer.valueOf(maxLength));
   }//--------------------------------------------
   /**
    * @param maxLength The maxLength to set.
    */
   public void setMaxLength(Integer maxLength)
   {

      this.maxLength = maxLength;
   }//--------------------------------------------
   /**
    * @return Returns the minLength.
    */
   public Integer getMinLength()
   {
      return minLength;
   }//--------------------------------------------
   /**
    * @param minLength The minLength to set.
    */
   public void setMinLength(String minLength)
   {
      if(Text.isNull(minLength))
       {
         setMinLength((Integer)null);
       }
      
      setMinLength(Integer.valueOf(minLength));
   }//--------------------------------------------
   /**
    * @param minLength The minLength to set.
    */
   public void setMinLength(Integer minLength)
   {
      this.minLength = minLength;
   }//--------------------------------------------

   /**
    * @return Returns the validateFlag.
    */
   public String getValidateFlag()
   {
      return validateFlag;
   }// --------------------------------------------
   /**
    * @param validateFlag The validateFlag to set.
    */
   public void setValidateFlag(String validateFlag)
   {
      if (validateFlag == null)
    	  this.validateFlag = "";
      else
    	  this.validateFlag = validateFlag;
   }//-------------------------------------------- 
   public String getRequiredErrorCode()
   {
      return getFieldName()+ "_required";
   } //------------------------------------------------------- 
   /**
    * 
    * @return getFieldName()+ "_regExp"
    */
   public String getRegExpErrorCode()
   {
      return getFieldName()+ "_regExp";
   } //-------------------------------------------------------
   /**
    * 
    * @return getFieldName()+ "_maxLength"
    */
   public String getMaxLengthErrorCode()
   {
      return getFieldName()+ "_maxLength";
   } //-------------------------------------------------------
   /**
    * 
    * @return getFieldName()+ "_minLength"
    */
   public String getMinLengthErrorCode()
   {
      return getFieldName()+ "_minLength";
   }// --------------------------------------------



   /**
    * @return Returns the requiredCode.
    */
   public final boolean isRequired()
   {
      return required;
   }//--------------------------------------------
   /**
    * @param required The requiredCode to set.
    */
   public final void setRequired(boolean required)
   {
      this.required = required;
   }// --------------------------------------------

  
}
