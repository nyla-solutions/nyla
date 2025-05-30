package nyla.solutions.spring.batch.file;

import java.io.File;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import nyla.solutions.core.io.IO;

/**
 * @author Gregory Green
 *
 */
public class FileItemReader implements ItemReader<File>
{
	/**
	 * 
	 * @param file the file
	 */
	public FileItemReader(File directory, String pattern)
	{
		this.files = IO.listFiles(directory, pattern);
	
	}//------------------------------------------------
	/**
	 * Read the next file
	 */
	@Override
	public File read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException
	{ 
		if(files == null || files.length == 0 || index >= files.length)
			return null;
		
			
		return files[index++];
	}//------------------------------------------------

	private int index = 0;
	private final File[] files;
}
