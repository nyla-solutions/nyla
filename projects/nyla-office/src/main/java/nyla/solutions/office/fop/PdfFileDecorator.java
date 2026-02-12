package nyla.solutions.office.fop;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.Fileable;
import nyla.solutions.core.util.Debugger;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;


/**
 * Creates a PDF file from the FO XML in a given textable object
 * See EmailFileExecutable for sample usage
 * @author Gregory Green
 *
 */
public class PdfFileDecorator implements Fileable
{
	private final Textable foTextable;
	private final String filePath;

    public PdfFileDecorator(Textable foTextable, String filePath) {
        this.foTextable = foTextable;
        this.filePath = filePath;
    }

    /**
	 * The PDF file output and return the file handle
	 * @return the file 
	 */
	public File getFile()
	{
		try
		{

			var file= Paths.get(filePath).toFile();
			
			FOP.writePDF(this.foTextable.getText(), file);
			
			return file;
		}
		catch (FileNotFoundException e)
		{
			throw new SystemException(Debugger.stackTrace(e));
		}
	}

}
