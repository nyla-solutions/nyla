package nyla.solutions.core.io.converter;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.patterns.conversion.Converter;

import java.io.File;
import java.io.IOException;

public class ConversionFileAuthor<ObjectType>
{
	public ConversionFileAuthor(File file, Converter<ObjectType, String> toHeader, Converter<ObjectType, String> toRow)
	{
		this.file = file;
		this.toHeader = toHeader;
		this.toRow = toRow;
	}//------------------------------------------------
	/**
	 * @param obj the object to file
	 * @throws IOException and IO error occurs
	 */
	public void appendFile(ObjectType obj) throws IOException
	{

		if (!file.exists())
		{
			IO.writeFile(file, toHeader.convert(obj));
		}

		IO.writeAppend(file, toRow.convert(obj));

	}// --------------------------------------------

	private final Converter<ObjectType, String> toHeader;
	private final Converter<ObjectType, String> toRow;
	private final File file;
}
