package nyla.solutions.office.msoffice.excel.patterns.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.office.msoffice.excel.patterns.workthread.AbstractExcelRowQueue;
import nyla.solutions.spring.validation.ValidationTask;

public class ValidateExcelRowQueue extends AbstractExcelRowQueue
{
   /**
    * ]
    * Constructor for ValidateExcelRowQueue initializes internal 
    * @param aInputFile the input file
    * @param aSheet the Excel sheet
    * @param validator the validation
    * @param validationColumn the column to validate
    * @param errors the error to add exceptions/rejections
    * @throws Exception
    */
   public ValidateExcelRowQueue(String aInputFile, String aSheet, Validator validator, int validationColumn, Errors errors)
   throws Exception
   {
      super(aInputFile, aSheet);
      
      if(validator == null)
         throw new RequiredException("validator in ValidateExcelRowQueue.ValidateExcelRowQueue");
      
      
      this.validator =  validator;
      
      this.validationColumn = validationColumn;
      
      this.errors = errors; 
      
   }// --------------------------------------------
   public Runnable nextTask()
   {
      
      if(!this.hasMoreTasks())
         return null;
      

      //create ValditionTask
      
      String[]row = super.nextRow();
      
      String text = row[validationColumn];
            
      
      return new ValidationTask(validator,text,errors);
   }// --------------------------------------------
   private Validator validator = null;
   private Errors errors;
   private int validationColumn = 0;

}
