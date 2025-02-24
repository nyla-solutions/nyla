package nyla.solutions.office.fop;

import java.io.File;
import java.io.FileNotFoundException;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.ConfigException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.Fileable;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;




/**
 * Creates a PDF file from the FO XML in a given textable object
 * See EmailFileExecutable for sample usage
 * @author Gregory Green
 *
 */
public class PdfFileDecorator implements Fileable
{
	private Textable foTextable = null;
	private String filePath = Config.settings().getProperty(getClass(),"filePath");


	/**
	 * The PDF file output and return the file handle
	 * @return the file 
	 */
	public File getFile()
	{
		try
		{
			if(foTextable == null)
			{
				throw new ConfigException("fo required on "+this.getClass().getName());
			}

			if(Text.isNull(filePath))
			{
				throw new ConfigException("filePath required on "+this.getClass().getName());
			}

			File file= new File(filePath);
			
			FOP.writePDF(this.getFoTextable().getText(), file);
			
			return file;
		} 
		catch (FileNotFoundException e)
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}
	
	/**
	 * @return the foTextable
	 */
	public Textable getFoTextable()
	{
		return foTextable;
	}
	/**
	 * @param foTextable the FO XML textable to set
	 */
	public void setFoTextable(Textable foTextable)
	{
		this.foTextable = foTextable;
	}


	

}
