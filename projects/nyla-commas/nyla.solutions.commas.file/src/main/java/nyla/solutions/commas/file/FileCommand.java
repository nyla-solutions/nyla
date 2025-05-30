package nyla.solutions.commas.file;

import java.io.File;

import nyla.solutions.commas.Command;

/**
 * Interface to process a file
 * @author Gregory Green
 *
 */
public interface FileCommand<ReturnType> extends Command<ReturnType, File>
{
	/**
	 * Perform an operation on a file/directory
	 * @param file the file to process
	 */
	public ReturnType execute(File file);
}
