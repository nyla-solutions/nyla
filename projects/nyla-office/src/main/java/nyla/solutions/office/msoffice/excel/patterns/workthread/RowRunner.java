package nyla.solutions.office.msoffice.excel.patterns.workthread;

public interface RowRunner extends Runnable
{
   /**
    * 
    * @param row the current row
    */
   void setRow(String[] row);
   
}
