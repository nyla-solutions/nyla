package nyla.solutions.core.io.csv;

import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test for the CsvWriter class
 * @author Gregory Green
 *
 */
public class CsvWriterTest
{

	private Writer outputWriter;
	private CsvWriter subject;


	@Test
	public void addCells() throws Exception
	{
		var builder = new StringBuilder();
		CsvWriter.addCells(builder, "hello","world");
		
		assertEquals(builder.toString(),"\"hello\",\"world\"");
		
	}
	@Test
	public void toCsv() throws Exception
	{
		assertEquals(CsvWriter.toCSV("hello","world"),"\"hello\",\"world\"");
		
	}

	@Test
	public void AppendRowStringArray()
	throws IOException
	{
		var file = new File("target/runtime/CsvWriterTest.csv");
		var writer = new CsvWriter(file);
		
		int cnt  = 0;
		var ts = String.valueOf(System.currentTimeMillis());
		writer.appendRow(ts,String.valueOf(cnt));
		
		assertTrue(file.exists());
		assertTrue(IO.readFile(file).contains(ts));
		
	}

}
