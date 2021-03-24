package nyla.solutions.core.exception;


import nyla.solutions.core.util.Debugger;


/**
 *  System exception when a value is larger than expected
 *  @author Gregory Green
 *  @version 1.0
 */
public class SizeViolationException extends SystemException
{
   /**
    *     Sets the exception message to  "Duplicate Row Exception" 
    */
   public SizeViolationException()
   {
      super("Provided value is larger than expected");

   }//----------------------------------------------
   /**
    * 
    * @param aInputs the inputs that may have the voliation
    */
   public void setInputs(Object[] aInputs)
   {
      StringBuffer message = new StringBuffer(this.getMessage());
      
      message.append(" Inputs=").append(Debugger.toString(aInputs));
      
      super.setMessage(message.toString());
   }//----------------------------------------------
   /**
    * 
    * Constructor for SizeViolationException initalizes internal 
    * data settings.
    * @param aMessage the size voliation message
    */
   public SizeViolationException(String aMessage)
   {
      super(aMessage);
   }//----------------------------------------------
   static final long serialVersionUID = SizeViolationException.class.getName()
   .hashCode();
}
