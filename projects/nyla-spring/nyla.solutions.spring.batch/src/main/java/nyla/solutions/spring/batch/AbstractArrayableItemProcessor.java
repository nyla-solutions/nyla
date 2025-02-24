package nyla.solutions.spring.batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.batch.item.ItemProcessor;

import nyla.solutions.core.data.Arrayable;
import nyla.solutions.core.exception.FatalException;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.patterns.decorator.Styles;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;
import nyla.solutions.office.msoffice.excel.CSV;



/**
 * Template function for item processing
 * @author Gregory Green
 *
 * @param <I>
 * @param <O>
 */
public abstract class AbstractArrayableItemProcessor<I, O> implements ItemProcessor<I, O>
{	
	/**
	 * Delete previous skip file
	 */
	protected void deleteSkipFile()
	{
		if(this.skipFilePath == null)
			return;
		
		File file = new File(this.skipFilePath);
		
		
		if(file.exists())
			file.delete();
		
	}// --------------------------------------------------------
	/**
	 * Write CSV file with data that was skipped
	 * @param dataRow the row to audit
	 */
	protected void auditSkip(Arrayable<Object> dataRow)
	{
		auditSkip(dataRow,null);
	}// --------------------------------------------------------
	/**
	 * 
	 * @param dataRow the row to audit
	 * @param reason the explanation
	 */
	protected void auditSkip(Arrayable<Object> dataRow, String reason)
	{
		try
		{
			File file = new File(skipFilePath);
			
			if(!file.exists())
			   CSV.writeHeader(file,this.skipFileHeader);
			
			ArrayList<Object> values = new ArrayList<Object>();
			values.addAll(Arrays.asList(dataRow.toArray()));
			
			if(reason != null)
			{
				Debugger.printInfo("Adding skip reason:"+reason);
				values.add(reason);
			}
			
			CSV.appendFile(file, values.toArray());
		}
		catch(NullPointerException e)
		{
			if(this.skipFilePath == null)
				throw new RequiredException(this.getClass().getName()+".skipFilePath");
			
			throw e;
		}
		catch (IOException e)
		{
			throw new FatalException(e);
		}
	}// --------------------------------------------------------
	
	/**
	 * @return the skipFileHeader
	 */
	public String getSkipFileHeader()
	{
		return skipFileHeader;
	}
	/**
	 * @param skipFileHeader the skipFileHeader to set
	 */
	public void setSkipFileHeader(String skipFileHeader)
	{
		this.skipFileHeader = skipFileHeader;
	}
	/**
	 * @return the skipFilePath
	 */
	public String getSkipFilePath()
	{
		return skipFilePath;
	}
	/**
	 * @param skipFilePath the skipFilePath to set
	 */
	public void setSkipFilePath(String skipFilePath)
	{
		if(skipFilePath !=null && skipFilePath.indexOf(Styles.DEFAULT_PREFIX) > -1)
		{
			try
			{
				skipFilePath = Text
						.format(skipFilePath, Config.getProperties());
			}
			catch (FormatException e)
			{
				throw new SystemException(e);
			}
		}
		
		this.skipFilePath = skipFilePath;
	}

	private String skipFileHeader;
	private String skipFilePath;
}
