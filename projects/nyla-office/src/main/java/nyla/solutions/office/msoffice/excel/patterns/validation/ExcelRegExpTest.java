package nyla.solutions.office.msoffice.excel.patterns.validation;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import junit.framework.TestCase;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import nyla.solutions.core.patterns.workthread.Boss;
import nyla.solutions.core.patterns.workthread.WorkQueue;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.office.msoffice.excel.Excel;
import nyla.solutions.office.msoffice.excel.ExcelSheet;
import nyla.solutions.spring.validation.GenericValidation;
import nyla.solutions.spring.validation.OrREValidation;
import nyla.solutions.spring.validation.Violations;



@Ignore
public class ExcelRegExpTest extends TestCase
{
   /**
    * 
    * Constructor for ReLookupPreFlightTest initializes internal 
    * @param name the junit test name
    */
   public ExcelRegExpTest(String name)
   {
      super(name);
   }// --------------------------------------------

   @SuppressWarnings({ "unchecked"})
public void assertFolderPathFromExcel()
   throws Exception
   {
      //read excel workbook sheet that has regular expressions
      Excel reExcel = Excel.getExcel(regExpFilePath);
      
      //read all RE(e) into one big or(ed) Regular Expression      
      ExcelSheet sheet = reExcel.retrieveSheet(reSheetName);
      
      System.out.println("reSheetName="+reSheetName+" rows="+sheet.getRowCount());
      
      int rowCount = sheet.getRowCount();
      String[] row = null;
      String re = null;
      StringBuffer reBuffer = new StringBuffer(2000);
      for (int i = startRow; i < rowCount ; i++)
      {
         row = sheet.getRow(i);
       
         if(row.length == 0)
            continue;
         
         Assert.assertTrue("lenght="+row.length+" i="+i, row.length > 1);
         
         //get regular expression
         re = row[reColumn];
         
         //Pattern.compile(re);
         
         if(i != startRow)
            reBuffer.append("|"); //add or
         
         //add to string buffer
         reBuffer.append("(").append(re).append(")");
      }
      
      Debugger.println("RE="+reBuffer);
      
      //create validator
      GenericValidation validator = new GenericValidation();
      validator.setRegExp(reBuffer.toString());
      
      
      
      //read excel workbook sheet that has the texts
          
    //create Validator Worker Queue
      WorkQueue queue = new ValidateExcelRowQueue(testFilePath, testSheetName, validator, validationColumn, errors);
    
     //create worker thread
      Boss boss = new Boss(queue);
      boss.startWorkers(5); //start three workers
            
      
      if(errors.hasErrors())
      {
         //print errors
         WritableWorkbook outputExcel = null;
         
         try
         {
            outputExcel = Excel.createWritableWorkbook(outputExcelFile);
            WritableSheet outputSheet = outputExcel.createSheet("Failures", 0);
            rowCount = 1;
            Object[] headers = {
               "Folder" };
            Excel.writeRow(outputSheet, headers, 0);
            for (Iterator<Object> i = errors.getGlobalErrors().iterator(); i.hasNext(); rowCount++)
            {
               Object[] inputs = {
                  i.next() };
               Excel.writeRow(outputSheet, inputs, rowCount);
            }
            //throw new SystemException("Found errors "+errors.getGlobalErrors());
            
            Debugger.printInfo(this, "THERE ARE ISSUES");
         }
         finally
         {
            if(outputExcel != null)
               try{ Excel.close(outputExcel); } catch(Exception e){}
            // TODO: handle exception
         }
      }      
   }// --------------------------------------------
   public static void main(String[] args)
   {
      junit.textui.TestRunner.run(ExcelRegExpTest.class);
   }// --------------------------------------------

   @Test
   @SuppressWarnings("rawtypes")
public void testComplexRegExpressions()
   throws Exception
   {
      //read excel workbook sheet that has regular expressions
      Excel reExcel = Excel.getExcel(regExpFilePath);
      
      //read all RE(e)
      ExcelSheet sheet = reExcel.retrieveSheet(reSheetName);
      
      System.out.println("reSheetName="+reSheetName+" rows="+sheet.getRowCount());
      
      int rowCount = sheet.getRowCount();
      String[] row = null;
      String re = null;
      OrREValidation reValidation = new OrREValidation();
      
      
      for (int i = startRow; i < rowCount ; i++)
      {
         row = sheet.getRow(i);
       
         if(row.length == 0)
            continue;
         
         Assert.assertTrue("lenght="+row.length+" i="+i, row.length > 1);
         
         //get regular expression
         re = row[reColumn];
         
         //Pattern.compile(re);
         
         reValidation.addRegularExpresion(re);
         
      }
            
      
      
      //read excel workbook sheet that has the texts
          
    //create Validator Worker Queue
      WorkQueue queue = new ValidateExcelRowQueue(testFilePath, testSheetName, reValidation, validationColumn, errors);
    
     //create worker thread
      Boss boss = new Boss(queue);
      boss.startWorkers(5); //start three workers
            
      
      if(errors.hasErrors())
      {
         //print errors
         WritableWorkbook outputExcel = null;
         
         try
         {
            outputExcel = Excel.createWritableWorkbook(outputExcelFile);
            WritableSheet outputSheet = outputExcel.createSheet("Failures", 0);
            rowCount = 1;
            Object[] headers = {
               "Folder" };
            Excel.writeRow(outputSheet, headers, 0);
            for (Iterator i = errors.getGlobalErrors().iterator(); i.hasNext(); rowCount++)
            {
               Object[] inputs = {
                  i.next() };
               Excel.writeRow(outputSheet, inputs, rowCount);
            }
            //throw new SystemException("Found errors "+errors.getGlobalErrors());
            
            Debugger.printInfo(this, "THERE ARE ISSUES");
         }
         finally
         {
            if(outputExcel != null)
               try{ Excel.close(outputExcel); } catch(Exception e){}
            // TODO: handle exception
         }
      }      
   }// --------------------------------------------
   private Violations errors = new Violations();
   private int validationColumn = Config.getPropertyInteger("test.file.validation.column",0).intValue();
   private String reSheetName = Config.getProperty("regExp.file.sheet");
   private int startRow = 1;
   private int reColumn = 0;
   private String regExpFilePath  = Config.getProperty("regExp.file.path");
   private String testFilePath  = Config.getProperty("test.file.path");
   private String testSheetName = Config.getProperty("test.file.sheet");
   private String outputExcelFile = Config.getProperty("output.file.sheet");
}
