package nyla.solutions.core.io.csv;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.io.IO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Test for the CsvWriter class o
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
		assertTrue(IO.reader().readTextFile(file.toPath()).contains(ts));
		
	}

    @Test
    public void testToCellEscapesQuotes()
    {
        assertEquals("\"a\"\"b\"", CsvWriter.toCell("a\"b"));
    }

    @Test
    public void testToRowNullThrows()
    {
        var file = new File("target/runtime/CsvWriterMoreTest_toRowNull.csv");
        var writer = new CsvWriter(file);
        assertThrows(RequiredException.class, () -> writer.toRow((Object[]) null));
    }

    @Test
    public void testAppendRowListNullThrows()
    {
        var file = new File("target/runtime/CsvWriterMoreTest_appendListNull.csv");
        var writer = new CsvWriter(file);
        assertThrows(RequiredException.class, () -> writer.appendRow((List<String>) null));
    }

    @Test
    public void testWriteHeaderCreatesFile()
            throws Exception
    {
        var file = new File("target/runtime/CsvWriterMoreTest_header_create.csv");
        if (file.exists()) file.delete();
        var writer = new CsvWriter(file);

        writer.writeHeader("col1", "col2");

        assertTrue(file.exists());
        String content = IO.reader().readTextFile(file.toPath());
        assertTrue(content.contains("\"col1\""));
        // newline should be present
        assertTrue(content.endsWith(IO.newline()) || content.contains(IO.newline()));
    }

    @Test
    public void testWriteHeaderDoesNotOverwriteExistingFile()
            throws Exception
    {
        var file = new File("target/runtime/CsvWriterMoreTest_header_no_overwrite.csv");
        file.getParentFile().mkdirs();
        Files.writeString(file.toPath(), "EXISTING\n");
        var writer = new CsvWriter(file);

        writer.writeHeader("should","not","appear");

        String content = IO.reader().readTextFile(file.toPath());
        assertTrue(content.contains("EXISTING"));
        assertFalse(content.contains("\"should\""));
    }

    @Test
    public void testAddCellAddsSeparatorWhenNeeded()
    {
        var b = new StringBuilder();
        CsvWriter.addCell(b, "x");
        assertEquals("\"x\"", b.toString());

        CsvWriter.addCell(b, "y");
        assertEquals("\"x\",\"y\"", b.toString());
    }

    @Test
    public void testAddCellsNullAndEmptySafe()
    {
        var b = new StringBuilder();
        // no cells -> unchanged
        CsvWriter.addCells(b);
        assertEquals("", b.toString());

        // null builder -> no exception
        CsvWriter.addCells(null, "a");
    }

    @Test
    public void testToCSVNullAndEmpty()
    {
        assertNull(CsvWriter.toCSV());
        assertNull(CsvWriter.toCSV((Object[]) null));
    }

    @Test
    public void testGetSetReplacement()
    {
        var file = new File("target/runtime/CsvWriterMoreTest_replacement.csv");
        var writer = new CsvWriter(file);
        writer.setReplacement("X");
        assertEquals("X", writer.getReplacement());
    }

    @Test
    public void testToRowProducesQuotedRowWithNewline()
    {
        var file = new File("target/runtime/CsvWriterMoreTest_toRow.csv");
        var writer = new CsvWriter(file);
        String row = writer.toRow(new Object[] {"a", "b"});
        assertTrue(row.contains("\"a\""));
        assertTrue(row.contains("\"b\""));
        assertTrue(row.endsWith(IO.newline()));
    }

}
