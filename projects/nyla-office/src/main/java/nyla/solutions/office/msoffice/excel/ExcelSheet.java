package nyla.solutions.office.msoffice.excel;
import java.util.Collection;

import nyla.solutions.core.data.Nameable;

/**
 * Abstraction for an excel sheet
 * @author Gregory Green
 *
 */
public interface ExcelSheet extends Nameable
{
	   public String getCell(int column, int row);
	   
	   
	   public String getCell(String cellName);
	   
	   public int getRowCount();
	   
	   /**
	    * Get the row string column data
	    * @param i row count starts from zero
	    * @return row of strings for each column
	    */
	   public String[] getRow(int i);
	   
	   public int getColumnCount();
	   
	   /**
	    * 
	    * @return the collection of rows
	    */
	   public Collection<String[]> getRows();
	   
}