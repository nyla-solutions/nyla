package nyla.solutions.commas.file;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import nyla.solutions.core.exception.RequiredException;


/**
 * The Macro file command
 * @author Gregory Green
 *
 */
public class MacroFileCommand implements FileCommand<Object>
{
	/**
	 * Execute the file on each given file command
	 */
	public synchronized Void execute(File file)
	{
		if(this.fileCommands == null)
			throw new RequiredException("this.fileCommands in MacroFileCommand");

		FileCommand<Object> fileCommand = null;
		for(Iterator<FileCommand<Object>> i = this.fileCommands.iterator();i.hasNext();)
		{
			fileCommand = i.next();
			
			fileCommand.execute(file);
		}
		
		return null;
	}//---------------------------------------------
	/**
	 * @return the fileCommands
	 */
	public synchronized Collection<FileCommand<Object>> getFileCommands()
	{
		return fileCommands;
	}//---------------------------------------------


	/**
	 * @param fileCommands the fileCommands to set
	 */
	public synchronized void setFileCommands(Collection<FileCommand<Object>> fileCommands)
	{
		this.fileCommands = fileCommands;
	}


	private Collection<FileCommand<Object>> fileCommands = null;
	

}
