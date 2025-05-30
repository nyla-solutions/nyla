package nyla.solutions.office.msoffice.excel.patterns.workthread;


import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.workthread.WorkQueue;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.office.msoffice.excel.Excel;
import nyla.solutions.office.msoffice.excel.ExcelSheet;

/**
 * <pre>
 * ExcelRowQueue work queue for processing rows within an excel spread sheet
 * </pre> 
 * @author Gregory Green
 * @version 1.0
 */
public abstract class AbstractExcelRowQueue implements WorkQueue
{
   /**
    * Constructor for ExcelRowQueue initializes internal 
    * data settings.
    * @param aInputFile  the input file
    * @throws Exception when an unknown error occurs
    */
   public AbstractExcelRowQueue(String aInputFile)
   throws Exception       
   {
      this(aInputFile, null);
   }// --------------------------------------------
   /**
    * Constructor for ExcelRowQueue initializes internal 
    * data settings.
	 * @param aInputFile  the input file
	 * @param aSheet  the sheet
	 * @throws Exception when an unknown error occurs
    */
   public AbstractExcelRowQueue(String aInputFile, String aSheet)
   throws Exception       
   {
      if (aInputFile == null)
         throw new IllegalArgumentException(
         "aInputFile required in ClosedMatterQueue.ClosedMatterQueue");
      
      try
      {
         this.excel = Excel.getExcel(aInputFile);
         if(aSheet != null && aSheet.length() > 0 )
         {
            this.sheet = this.excel.retrieveSheet(aSheet);   
            
            if(this.sheet == null)
            {
               throw new IllegalArgumentException("Sheet not found "+aSheet);
            }

         }
         else
         {
            this.sheet = this.excel.retrieveSheet(0); //first sheet
         }
         
         
         this.rowCount = this.sheet.getRowCount();
      }
      catch (Exception e)
      {
        throw new SystemException("Unable to open file "+aInputFile+"  error: "+Debugger.stackTrace(e));
      }
   }// --------------------------------------------
   public  synchronized boolean hasMoreTasks()
   {      
      if(currentRowNumber + 1 >= rowCount)
         return false;
      
      String firstColumn  = sheet.getCell(0, currentRowNumber + 1);
      
      return firstColumn != null && firstColumn.length() != 0;
   }// --------------------------------------------
   protected synchronized String[] nextRow()
   {
      String[] cells = this.sheet.getRow(this.currentRowNumber + 1);
      currentRowNumber++;
       
      return cells;
   }// --------------------------------------------
   /**
    * 
    * @return sheet.getRows()
    */
   public final int size()
   {
      return rowCount;
   }// --------------------------------------------

   /**
    * @return Returns the currentRowNumber.
    */
   public final int getCurrentRowNumber()
   {
      return currentRowNumber;
   }// --------------------------------------------

   private ExcelSheet sheet = null;
   private final int rowCount;
   private Excel excel = null;
   private int currentRowNumber = 0;
}
