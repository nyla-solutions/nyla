package nyla.solutions.spring.batch;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.springframework.batch.core.SkipListener;

import nyla.solutions.core.exception.FatalException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Text;
import nyla.solutions.xml.XML;
import nyla.solutions.xml.XMLInterpreter;


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

<bean id="xmlSkipListener" class="solutions.dao.spring.batch.XmlInterpreterSkipListener">
  <property name="skipInReadFilePathProp" value="csvSkipListener.skipInReadFilePath"/>
  <property name="skipInProcessFilePathProp" value="csvSkipListener.skipInProcessFilePath"/>
  <property name="skipInWriterFilePathProp" value="csvSkipListener.skipInWriterFilePath"/>  
</bean>
 * 
 * @author Gregory Green
 *
 */
public class XmlInterpreterSkipListener implements SkipListener<Object, Object>
{
	/**
	 * 
	 * @see org.springframework.batch.core.SkipListener#onSkipInProcess(java.lang.Object, java.lang.Throwable)
	 */
	@Override
	public void onSkipInProcess(Object processObject, Throwable exception)
	{
		try
		{
			String path = this.skipInProcessFilePath;
			
	        File file = new File(path);
			
			if(!file.exists() && this.rootElement != null)
			   IO.writeFile(file, Text.appendNewLine(this.rootElement));
			
			report(file, processObject, exception);
		}
		catch (IOException e)
		{
			throw new FatalException(e);
		}
	}// --------------------------------------------------------
	/**
	 * Report the issue to a file
	 * @param file
	 * @param processObject
	 * @param exception
	 * @throws IOException
	 */
	private void report(File file, Object processObject, Throwable exception)
	throws IOException
	{
		
			XMLInterpreter xmlInterpreter = XML.getInterpreter();
			
			StringBuilder output = new StringBuilder();
			
			output.append("<").append(this.errorElementName).append(">");
			
			if(timeElementName != null && timeElementName.length() > 0)
			{
				output.append("<").append(timeElementName).append(">");
				
				output.append(Text.formatDate(Calendar.getInstance()));
				
				output.append("</").append(timeElementName).append(">");
			}
			
			if(processObject != null)
				output.append(xmlInterpreter.toXML(processObject));
			
			if(exception != null)
				output.append(xmlInterpreter.toXML(exception));
			
			
			output.append("</").append(this.errorElementName).append(">");
			
			IO.writeFile(file, output.toString(),true);
			
		
	}// --------------------------------------------------------

	/**
	 * Write the read errors to skipInReadFilePath
	 * @see org.springframework.batch.core.SkipListener#onSkipInRead(java.lang.Throwable)
	 */
	@Override
	public void onSkipInRead(Throwable exception)
	{
		String path = skipInReadFilePath;
		
		
		File file = new File(path);
		
		try
		{
			if(!file.exists() &&  this.rootElement != null)
			   IO.writeFile(file, Text.appendNewLine(this.rootElement));
		
		
			this.report(file, null, exception);

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
	public void onSkipInWrite(Object writeObject, Throwable exception)
	{	
		try
		{
			String path = skipInWriteFilePath;
			
			File file = new File(path);
			
			if(!file.exists() && this.rootElement != null)
			   IO.writeFile(file, Text.appendNewLine(this.rootElement));
			
			this.report(file, writeObject, exception);
		}
		catch (IOException e)
		{
			throw new FatalException(e);
		}
	}// --------------------------------------------------------
	
	

	/**
	 * @return the timeElementName
	 */
	public String getTimeElementName()
	{
		return timeElementName;
	}
	/**
	 * @param timeElementName the timeElementName to set
	 */
	public void setTimeElementName(String timeElementName)
	{
		this.timeElementName = timeElementName;
	}
	/**
	 * @return the errorElementName
	 */
	public String getErrorElementName()
	{
		return errorElementName;
	}
	/**
	 * @param errorElementName the errorElementName to set
	 */
	public void setErrorElementName(String errorElementName)
	{
		this.errorElementName = errorElementName;
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
	 * @return the rootElement
	 */
	public String getRootElement()
	{
		return rootElement;
	}
	/**
	 * @param rootElement the rootElement to set
	 */
	public void setRootElement(String rootElement)
	{
		this.rootElement = rootElement;
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
	}



	private String timeElementName = Config.getProperty(XmlInterpreterSkipListener.class,"timeElementName", "time");
	private String errorElementName = Config.getProperty(XmlInterpreterSkipListener.class,"recordElementName", "error");

	
	private String skipInReadFilePath = Config.getProperty(XmlInterpreterSkipListener.class,"skipInReadFilePath","skipInRead.xml");
	
	private String rootElement = Config.getProperty(XmlInterpreterSkipListener.class,"rootElementName","<skips>");
	
	private String skipInProcessFilePath= Config.getProperty(XmlInterpreterSkipListener.class,"skipInProcessFilePath","skipInProcess.xml");
		
	private String skipInWriteFilePath= Config.getProperty(XmlInterpreterSkipListener.class,"skipInWriteFilePath","skipInWrite.xml");

}
