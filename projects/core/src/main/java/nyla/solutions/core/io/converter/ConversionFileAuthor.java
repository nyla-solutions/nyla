package nyla.solutions.core.io.converter;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.conversion.Converter;

import java.io.File;
import java.io.IOException;

/**
 * Create or append to a file with a needed header and converted each role
 * @param <ObjectType> row object Type
 */
public class ConversionFileAuthor<ObjectType>
{
	private final Converter<ObjectType, String> toHeader;
	private final Converter<ObjectType, String> toRow;
	private final File file;

	/**
	 * Constructor
	 * @param file the file to write
	 * @param toHeader the conversion to create a header row
	 * @param toRow the conversion to convert the object type to a row
	 */
	public ConversionFileAuthor(File file, Converter<ObjectType, String> toHeader, Converter<ObjectType, String> toRow)
	{
		this.file = file;
		this.toHeader = toHeader;
		this.toRow = toRow;
	}
	/**
	 * Convert the given object to append results to the file
	 * @param rowObject the object data to append file
	 */
	public void appendFile(ObjectType rowObject)
	{

		if (!file.exists())
		{
			IO.writer().writeFile(file, toHeader.convert(rowObject));
		}

		IO.writer().writeAppend(file, toRow.convert(rowObject));

	}
}
