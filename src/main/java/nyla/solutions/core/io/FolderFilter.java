package nyla.solutions.core.io;

import java.io.File;
import java.io.FileFilter;

/**
 * IO file filter to list folders
 * @author Gregory Green
 *
 */
public class FolderFilter implements FileFilter
{

	/**
	 * Accept folder
	 * @return true if the pathname is folder
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File pathname)
	{
		
		return pathname != null && pathname.isDirectory();
	}//---------------------------------------------

}
