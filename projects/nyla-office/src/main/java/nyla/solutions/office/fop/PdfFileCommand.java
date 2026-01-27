package nyla.solutions.office.fop;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.JavaBean;
import nyla.solutions.core.util.Text;


/**
 * This class uses FO XML to generate a PDF file contains a given image file contain
 * @author Gregory Green
 */
public class PdfFileCommand implements Function<File,Boolean>
{
	private String outputPath = Config.settings().getProperty(getClass(),"outputPath");
	private Textable foTextableTemplate = null;
	/**
	 * Implements the the FileCommand interface
	 * 
	 * Creates the output PDF in the directory
	 * 
	 * this.outputPath/<file-name>.pdf
	 * @param file the file create
	 * @return success flag
	 */
	public Boolean apply(File file)
	{		
		try 
		{
			//get FO text
			String foTemplate = foTextableTemplate.getText();
			
			Map<Object,Object> map = Config.settings().getProperties();

			//add current file location
			//add file properties
			map.putAll(JavaBean.toMap(file));			
			
			//format
			var foXML = Text.format().formatMap(foTemplate, map);
			
			//created PDF output
			FOP.writePDF(foXML, new File(outputPath+Config.getFileSeparator()+file.getName()+".pdf"));
			return true;
		} 
		catch (Exception e) 
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}
	
	/**
	 * @return the outputPath
	 */
	public String getOutputPath()
	{
		return outputPath;
	}
	/**
	 * @param outputPath the outputPath to set
	 */
	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}
	/**
	 * @return the foTextableTemplate
	 */
	public Textable getFoTextableTemplate()
	{
		return foTextableTemplate;
	}
	/**
	 * @param foTextableTemplate the foTextableTemplate to set
	 */
	public void setFoTextableTemplate(Textable foTextableTemplate)
	{
		this.foTextableTemplate = foTextableTemplate;
	}

}