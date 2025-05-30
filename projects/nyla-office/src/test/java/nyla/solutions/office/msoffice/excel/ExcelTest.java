package nyla.solutions.office.msoffice.excel;

import java.util.Arrays;

import org.junit.Assert;

import junit.framework.TestCase;
import nyla.solutions.core.util.Debugger;

/**
 * Test for the Excel object
 * @author Gregory Green
 *
 */
public class ExcelTest extends TestCase
{

	public ExcelTest(String name)
	{
		super(name);
	}

	public void testRetrieveSheetString()
	throws Exception
	{
		Excel excel = Excel.getExcel(filePath);
		
		Assert.assertNotNull(excel);
		
		ExcelSheet sheet = excel.retrieveSheet(sheetName);
		
		Assert.assertNotNull(sheet);
		
		String[] row = sheet.getRow(0);
		
		Assert.assertTrue(row != null && row.length > 0);
		
		Debugger.println(this,Debugger.toString(row));
		
		Assert.assertTrue(sheet.getColumnCount() >  1);
		
		Assert.assertTrue(sheet.getRowCount() >  1);
		
		Debugger.println(this,"sheet.getRowCount()="+sheet.getRowCount());
		
		for (String[] currentRow : sheet.getRows())
		{
			Debugger.println("row:"+Arrays.asList(currentRow));
		}
		
	}

	private String sheetName = "Sheet1";
	private String filePath = "src/test/resources/excel/input.xls";
}
