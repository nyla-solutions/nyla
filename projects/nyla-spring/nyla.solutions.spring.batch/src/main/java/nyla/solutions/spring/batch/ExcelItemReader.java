package nyla.solutions.spring.batch;

import java.io.IOException;

import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import nyla.solutions.core.patterns.conversion.ArrayConversion;
import nyla.solutions.core.util.Config;
import nyla.solutions.office.msoffice.excel.Excel;
import nyla.solutions.office.msoffice.excel.ExcelSheet;


public class ExcelItemReader<T> implements ItemReader<T>
{
	@BeforeStep
	public void open()
	throws IOException
	{
		this.excel = Excel.getExcel(this.filePath);
		
		if(this.sheetName != null && this.sheetName.length() > 0)
			this.sheet = this.excel.retrieveSheet(this.sheetName);
		else
			this.sheet = this.excel.retrieveSheet(this.sheetNumber);
		
		this.rowCount = this.sheet.getRowCount();
				
		index = 1;
	}// --------------------------------------------------------

	/**
	 * @return the current row (convert if conversion was provided)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException
	{
		//read
		if(index > this.rowCount -1)
			return null;   //end of the sheet
		
		if(this.conversion != null)
			return this.conversion.convert(sheet.getRow(this.index++));
		
		
		return (T)sheet.getRow(this.index++);
	}// --------------------------------------------------------
	
	/**
	 * Dispose the excel object
	 */
	@AfterStep
	public void dispose()
	{
		if(excel != null)
			try{ excel.dispose(); } catch(Exception e){}
	}// --------------------------------------------------------
	
	
	/**
	 * @return the filePathProperty
	 */
	public String getFilePathProperty()
	{
		return filePathProperty;
	}

	/**
	 * @param filePathProperty the filePathProperty to set
	 */
	public void setFilePathProperty(String filePathProperty)
	{
		this.filePathProperty = filePathProperty;
		
		this.filePath = Config.getProperty(this.filePathProperty);
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName()
	{
		return sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName)
	{
		this.sheetName = sheetName;
	}

	/**
	 * @return the sheetNumber
	 */
	public int getSheetNumber()
	{
		return sheetNumber;
	}

	/**
	 * @param sheetNumber the sheetNumber to set
	 */
	public void setSheetNumber(int sheetNumber)
	{
		this.sheetNumber = sheetNumber;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath()
	{
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}


	private String sheetName;
	private int sheetNumber;
	private ExcelSheet sheet;
	private ArrayConversion<T,String> conversion;
	private int index;
	private int rowCount;
	private String filePath;
	private String filePathProperty;
	private Excel excel;

}
