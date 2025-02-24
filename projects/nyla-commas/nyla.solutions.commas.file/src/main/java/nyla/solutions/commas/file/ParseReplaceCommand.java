package nyla.solutions.commas.file;

import java.io.File;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.SynchronizedIO;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;




public class ParseReplaceCommand implements FileCommand<Void>
{
	/**
	 * Parse text content and replace file with results
	 */
	public Void execute(File file)
	{		
		try 
		{
			String content = IO.readFile(file.getAbsolutePath());
			content = Text.parseText(content, start, end);
			//overwrite file
			SynchronizedIO.getInstance().writeFile(file.getAbsolutePath(), content);
			
			return null;
		} 
		catch (Exception e) 
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}//---------------------------------------------
	
	/**
	 * @return the start
	 */
	public String getStart()
	{
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(String start)
	{
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public String getEnd()
	{
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(String end)
	{
		this.end = end;
	}

	private String start = "";
	private String end = "";

}
