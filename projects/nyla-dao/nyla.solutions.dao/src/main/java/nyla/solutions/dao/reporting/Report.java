package nyla.solutions.dao.reporting;

import java.sql.ResultSet;

/**
 * 
 * <b>Report</b> is a interface to produce report information 
 * @author Gregory Green
 *
 */
public interface Report
{
   /**
    * 
    * @param resultSet the database results
    */
   public void printReport(ResultSet resultSet);
   
   
  public void printRow(Object[] row);

  /**
   * Print a single row 
   * @param dataRow the row to print
   */
  public void printHeader(Object[] row);
  
}
