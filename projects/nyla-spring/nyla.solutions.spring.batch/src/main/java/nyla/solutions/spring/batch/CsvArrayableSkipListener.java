package nyla.solutions.spring.batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.batch.core.SkipListener;

import nyla.solutions.core.data.Arrayable;
import nyla.solutions.core.exception.FatalException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Text;
import nyla.solutions.office.msoffice.excel.CSV;


/**
 * Used in a step
 * <step id="concreteStep3" parent="listenersParentStep">
    <tasklet>
        <chunk reader="itemReader" writer="itemWriter" commit-interval="5"/>
    </tasklet>
    <listeners merge="true">
        <listener ref="listenerTwo"/>
    </listeners>
</step>

<bean id="csvSkipListener" class="solutions.dao.spring.batch.CsvArrayableSkipListener">
  <property name="skipInReadFilePathProp" value="csvSkipListener.skipInReadFilePath"/>
  <property name="skipInProcessFilePathProp" value="csvSkipListener.skipInProcessFilePath"/>
  <property name="skipInWriterFilePathProp" value="csvSkipListener.skipInWriterFilePath"/>  
</bean>
 * 
 * @author Gregory Green
 *
 */
public class CsvArrayableSkipListener implements SkipListener<Arrayable<Object>, Arrayable<Object>>
{
	/**
	 * 
	 * @see org.springframework.batch.core.SkipListener#onSkipInProcess(java.lang.Object, java.lang.Throwable)
	 */
	@Override
	public void onSkipInProcess(Arrayable<Object> processObject, Throwable exception)
	{
		try
		{
			String path = this.skipInProcessFilePath;
			
			
            File file = new File(path);
			
			if(!file.exists() && this.skipInProcessFileHeader != null)
			   IO.writeFile(file, Text.appendNewLine(this.skipInProcessFileHeader));
			
			ArrayList<Object> output = new ArrayList<Object>();
			
			if(addLabelAndTimeStamp)
			{
				output.add(label);
				
				output.add(Text.formatDate(Calendar.getInstance()));
			}
			
			output.addAll(Arrays.asList(processObject.toArray()));
			
			output.add(exception);
			
			CSV.appendFile(file, output.toArray());
		}
		catch (IOException e)
		{
			throw new FatalException(e);
		}
	}// --------------------------------------------------------

	/**
	 * Write the read errors to skipInReadFilePath
	 * @see org.springframework.batch.core.SkipListener#onSkipInRead(java.lang.Throwable)
	 */
	@Override
	public void onSkipInRead(Throwable throwable)
	{
		String path = skipInReadFilePath;
		
		
		File file = new File(path);
		
		try
		{
			if(!file.exists() &&  this.skipInReadFileHeader != null)
			   IO.writeFile(file, Text.appendNewLine(this.skipInReadFileHeader));
		
			if(this.addLabelAndTimeStamp)
			{
				Object[] errors = {label,Text.formatDate(Calendar.getInstance()),throwable};
				
				CSV.appendFile(file, errors);
			}
			else
			{
				Object[] errors = {throwable};
				
				CSV.appendFile(file, errors);
			}

		}
		catch (IOException e)
		{
			throw new FatalException(e);
		}
		
	}// --------------------------------------------------------

	/**
	 * Write to skipInWriteFilePath
	 */
	@Override
	public void onSkipInWrite(Arrayable<Object> writeObject, Throwable exception)
	{	
		try
		{
			String path = skipInWriteFilePath;
			
			File file = new File(path);
			
			if(!file.exists() && this.skipInWriteFileHeader != null)
			   IO.writeFile(file, Text.appendNewLine(this.skipInWriteFileHeader));
			
			ArrayList<Object> output = new ArrayList<Object>();
			
			if(addLabelAndTimeStamp)
			{
				output.add(label);
				
				output.add(Text.formatDate(Calendar.getInstance()));
			}
		
			
			output.addAll(Arrays.asList(writeObject.toArray()));
			
			output.add(exception);
			
			CSV.appendFile(file, output.toArray());
		}
		catch (IOException e)
		{
			throw new FatalException(e);
		}
	}// --------------------------------------------------------
	
	
	/**
	 * @return the label
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return the skipInReadFilePath
	 */
	public String getSkipInReadFilePath()
	{
		return skipInReadFilePath;
	}

	/**
	 * @param skipInReadFilePath the skipInReadFilePath to set
	 */
	public void setSkipInReadFilePath(String skipInReadFilePath)
	{
		this.skipInReadFilePath = skipInReadFilePath;
	}

	/**
	 * @return the skipInProcessFilePath
	 */
	public String getSkipInProcessFilePath()
	{
		return skipInProcessFilePath;
	}

	/**
	 * @param skipInProcessFilePath the skipInProcessFilePath to set
	 */
	public void setSkipInProcessFilePath(String skipInProcessFilePath)
	{
		this.skipInProcessFilePath = skipInProcessFilePath;
	}

	/**
	 * @return the skipInWriteFilePath
	 */
	public String getSkipInWriteFilePath()
	{
		return skipInWriteFilePath;
	}

	/**
	 * @param skipInWriteFilePath the skipInWriteFilePath to set
	 */
	public void setSkipInWriteFilePath(String skipInWriteFilePath)
	{
		this.skipInWriteFilePath = skipInWriteFilePath;
	}// --------------------------------------------------------
	
	
	
	/**
	 * @return the skipInReadFileHeader
	 */
	public String getSkipInReadFileHeader()
	{
		return skipInReadFileHeader;
	}

	/**
	 * @param skipInReadFileHeader the skipInReadFileHeader to set
	 */
	public void setSkipInReadFileHeader(String skipInReadFileHeader)
	{
		this.skipInReadFileHeader = skipInReadFileHeader;
	}

	/**
	 * @return the skipInProcessFileHeader
	 */
	public String getSkipInProcessFileHeader()
	{
		return skipInProcessFileHeader;
	}

	/**
	 * @param skipInProcessFileHeader the skipInProcessFileHeader to set
	 */
	public void setSkipInProcessFileHeader(String skipInProcessFileHeader)
	{
		this.skipInProcessFileHeader = skipInProcessFileHeader;
	}

	/**
	 * @return the skipInWriteFileHeader
	 */
	public String getSkipInWriteFileHeader()
	{
		return skipInWriteFileHeader;
	}

	/**
	 * @param skipInWriteFileHeader the skipInWriteFileHeader to set
	 */
	public void setSkipInWriteFileHeader(String skipInWriteFileHeader)
	{
		this.skipInWriteFileHeader = skipInWriteFileHeader;
	}

	/**
	 * @return the addLabelAndTimeStamp
	 */
	public boolean isAddLabelAndTimeStamp()
	{
		return addLabelAndTimeStamp;
	}

	/**
	 * @param addLabelAndTimeStamp the addLabelAndTimeStamp to set
	 */
	public void setAddLabelAndTimeStamp(boolean addLabelAndTimeStamp)
	{
		this.addLabelAndTimeStamp = addLabelAndTimeStamp;
	}



	private String label = Config.getProperty(CsvArrayableSkipListener.class,"skipInReadFilePath", "CsvSkipListener");
	
	private String skipInReadFileHeader = Config.getProperty(CsvArrayableSkipListener.class,"skipInReadFileHeader","LABEL,TIMESTAMP,ERROR");
	
	private String skipInReadFilePath = Config.getProperty(CsvArrayableSkipListener.class,"skipInReadFilePath", 
			Config.getTempDir()+IO.fileSperator()+"skipInReadFile.csv");
	
	
	private String skipInProcessFileHeader;
	
	private String skipInProcessFilePath= Config.getProperty(CsvArrayableSkipListener.class,"skipInProcessFilePath", 
			Config.getTempDir()+IO.fileSperator()+"skipInProcessFilePath.csv");
	
	
	private String skipInWriteFileHeader;
	
	private String skipInWriteFilePath= Config.getProperty(CsvArrayableSkipListener.class,"skipInWriteFilePath", 
			Config.getTempDir()+IO.fileSperator()+"skipInWriteFilePath.csv");
	
	private boolean addLabelAndTimeStamp = true;

}
