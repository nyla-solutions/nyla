package nyla.solutions.core.io.csv;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static nyla.solutions.core.util.Config.settings;

/**
 * CSV writer utilty
 * @author Gregory Green
 *
 */
public class CsvWriter
{
	/**
	 * SEPARATOR = Config.getProperty(this.getClass().getName() + ".separator", ",")
	 */
	public static final String SEPARATOR = settings().getProperty(CsvWriter.class.getName() + ".separator", ",");
	
	private final File file;
	private String replacement = settings().getProperty(this.getClass().getName() + ".replacement", "");
	
	

	public CsvWriter(File file)
	{
		this.file = file;
	}//------------------------------------------------
	/**
	 * 
	 * @param cells the cells row to append
	 * @throws IOException when IO error occurs
	 */
	public void appendRow(String ... cells) throws IOException
	{
		if(cells == null)
			return;
		StringBuilder text = new StringBuilder();
		
		for (int i = 0; i < cells.length; i++)
		{
			if (i != 0)
				text.append(SEPARATOR);

			text.append("\"").append(format(cells[i])).append("\"");

		}

		IO.writeAppend(file, new StringBuilder(text.toString()).append(IO.newline()).toString());
	}//------------------------------------------------
	/**
	 * 
	 * @param row the row to append
	 * @throws IOException when IO error occurs
	 */
	public void appendRow(List<String> row) throws IOException
	{

		if (row == null)
			throw new RequiredException("objects in CSV.toRow");

		StringBuilder text = new StringBuilder();

		for (int i = 0; i < row.size(); i++)
		{
			if (i != 0)
				text.append(SEPARATOR);

			text.append("\"").append(format(row.get(i))).append("\"");

		}

		IO.writeAppend(file, text.toString() + IO.newline());
	}// --------------------------------------------

	public static String toCell(Object text)
	{
		return new StringBuilder("\"")
				.append(format(text))
				.append("\"").toString();
	}//------------------------------------------------
	/**
	 * 
	 * @param objects
	 * @return CSV format version of the objects values
	 */
	public String toRow(Object[] objects)
	{
		if (objects == null)
			throw new RequiredException("objects in CSV.toRow");

		StringBuilder text = new StringBuilder();

		for (int i = 0; i < objects.length; i++)
		{
			if (i != 0)
				text.append(SEPARATOR);

			text.append("\"").append(format(objects[i])).append("\"");

		}
		return text.toString() + IO.newline();
	}// --------------------------------------------

	/**
	 * 
	 * @param object
	 *            the text version of the object will be formatted
	 * @return text where the separator is replaced with replacement character
	 */
	private static String format(Object object)
	{
		String text = Text.toString(object);
		
		return Text.replace("\"", "\"\"", text); // TODO: need to check "
	}// --------------------------------------------

	public void writeHeader(String... header) throws IOException
	{

		writeHeader(toRow(header), IO.CHARSET);

	}// --------------------------------------------------------

	public void writeHeader(List<String> header) throws IOException
	{
		StringBuilder text = new StringBuilder();

		for (int i = 0; i < header.size(); i++)
		{
			if (i != 0)
				text.append(SEPARATOR);

			text.append("\"").append(format(header.get(i))).append("\"");

		}

		writeHeader(text.toString(), IO.CHARSET);

	}// --------------------------------------------------------

	public void writeHeader(String header, Charset charset) throws IOException
	{
		if (!file.exists() && header != null)
		{
			IO.writeFile(file, Text.appendNewLine(header), charset);
		}
	}// --------------------------------------------------------

	/**
	 * @return the replacement
	 */
	public String getReplacement()
	{
		return replacement;
	}// --------------------------------------------

	/**
	 * @param replacement
	 *            the replacement to set
	 */
	public void setReplacement(String replacement)
	{
		this.replacement = replacement;
	}// --------------------------------------------
	public static void addCell(StringBuilder builder, Object cell)
	{
		if(builder.length() != 0)
			builder.append(SEPARATOR);
		
		builder.append(toCell(cell));
	}//------------------------------------------------
	/**
	 * Add formatted CSV cells to a builder
	 * @param builder the string builder
	 * @param cells the cells to format as CSV cells in a row
	 */
	public static void addCells(StringBuilder builder, Object... cells)
	{
		if(builder == null || cells == null || cells.length == 0)
			return;
		
		for (Object cell : cells)
		{
			addCell(builder,cell);
		}
	}//------------------------------------------------
	/**
	 * Create a CSV line
	 * @param cells the cells for format
	 * @return the CSF formatted line
	 */
	public static String toCSV(Object... cells)
	{
		if(cells == null || cells.length == 0)
			return null;
		
		StringBuilder builder = new StringBuilder();
		addCells(builder, cells);
		return builder.toString();
	}
}
