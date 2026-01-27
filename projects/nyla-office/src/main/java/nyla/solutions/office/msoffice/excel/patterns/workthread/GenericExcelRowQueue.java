//package nyla.solutions.office.msoffice.excel.patterns.workthread;
//
//import nyla.solutions.core.exception.RequiredException;
//
//public class GenericExcelRowQueue extends AbstractExcelRowQueue
//{
//   /**
//    *
//    * Constructor for GenericExcelRowQueue initializes internal
//    * @param inputFile
//    * @param rowRunner
//    * @throws Exception
//    */
//   public GenericExcelRowQueue(String inputFile, RowRunner rowRunner)
//   throws Exception
//   {
//      super(inputFile);
//
//      if (rowRunner == null)
//         throw new RequiredException(
//         "rowRunner in GenericExcelRowQueue.GenericExcelRowQueue");
//      
//      this.rowRunner= rowRunner;
//   }// --------------------------------------------
//
//   public Runnable nextTask()
//   {
//      if(!this.hasMoreTasks())
//         return null;
//
//
//      //create ValditionTask
//
//      String[]row = super.nextRow();
//
//       this.rowRunner.setRow(row);
//
//
//      return rowRunner;
//   }
//
//   private RowRunner rowRunner = null;
//
//
//}
