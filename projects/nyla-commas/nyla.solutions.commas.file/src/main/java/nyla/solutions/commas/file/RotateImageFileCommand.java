/**
 * 
 */
package nyla.solutions.commas.file;

import java.io.File;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.media.Graphics;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;




/**
 * Rotate an image file a given number of degrees
 * @author Gregory Green
 *
 */
public class RotateImageFileCommand implements FileCommand<Void>
{
	/**
	 * Rotate the file image
	 */
	public Void execute(File file)
	{	
		try 
		{
			//make output directory if it does not exist
			if(!IO.exists(this.outputPath))
				IO.mkdir(new File(this.outputPath));
			
			Graphics.rotateImage(file, new File(this.outputPath+Config.getFileSeparator()+file.getName()), format, degrees);
			return null;
		} 
		catch (Exception e) 
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}//---------------------------------------------
	
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
	 * @return the degrees
	 */
	public int getDegrees()
	{
		return degrees;
	}
	/**
	 * @param degrees the degrees to set
	 */
	public void setDegrees(int degrees)
	{
		this.degrees = degrees;
	}
	/**
	 * @return the format
	 */
	public String getFormat()
	{
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format)
	{
		this.format = format;
	}

	private String outputPath = Config.getProperty(getClass(),"outputPath",".");
	private int  degrees = Config.getPropertyInteger(this.getClass(),"degrees",0).intValue();
	private String format = Config.getProperty(this.getClass(),"format","png");
}
