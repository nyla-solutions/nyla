package nyla.solutions.commas.file;

import java.io.File;
import java.io.IOException;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Debugger;




/**
 * Backup the content of given file/folder
 * @author Gregory Green
 *
 */
public class BackupFileCommand implements FileCommand<Void>
{

	/**
	 * Constructor 
	 */
	public BackupFileCommand()
	{
		backupFolder = new File(this.backupPath);
				
		if(!backupFolder.exists())
			Debugger.println("mkdir:"+backupFolder.mkdir());
			
		if(!backupFolder.isDirectory())
			throw new SetupException("The provided backup path must be a directory");
	}//---------------------------------------------
	/**
	 * Implement the back logic
	 */
	public Void execute(File file)
	{
		if(backupFolder == null)
			throw new RequiredException("backupFolder in BackupFileCommand");
		
		try 
		{
			String[] list = backupFolder.list();
			
			if(list != null && list.length > 0)
			{
				//delete previous backup
				IO.delete(this.backupFolder);
				
				//recreate backup
				IO.mkdir(this.backupFolder);
			}

			
			//copy file to backup folder
			IO.copyDirectory(file, backupFolder);
			
			
			return null;
		} 
		catch (IOException e) 
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
		
	}//---------------------------------------------
	
	private File backupFolder = null;
	private String backupPath = Config.getProperty(BackupFileCommand.class,"backupPath","");

}
