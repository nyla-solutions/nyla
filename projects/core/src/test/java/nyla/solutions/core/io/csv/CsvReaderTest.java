package nyla.solutions.core.io.csv;


import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.io.csv.CsvReader.DataType;
import nyla.solutions.core.io.csv.formulas.SumStatsByMillisecondsFormular;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The CSV Reader test
 * @author Gregory Green
 *
 */
public class CsvReaderTest
{

	@Test
	void parseIssue() throws IOException {
			String csv = "\"sku1\", \"Peanut butter\"\n" +
					"\"sku2\", \"Jelly\"\n" +
					"\"sku3\", \"Bread\"\n" +
					"\"sku4\", \"Milk\"";

			var subject = new CsvReader(new StringReader(csv));
	}

	@Test
	public void testMerge()
	throws Exception
	{
		File file = Paths.get("src/test/resources/csv/csvmerged.csv").toFile();
		
		SumStatsByMillisecondsFormular formula = new SumStatsByMillisecondsFormular(0, 1,1000);
		CsvReader subject = new CsvReader(file);
		subject.calc(formula);
		
		assertEquals(9,subject.size());
		assertEquals(10, formula.getMin());
		assertEquals(30, formula.getMax());
		assertEquals(20.0, formula.getAvg(),0);
		
	}
	@Test
	public void testMergeCalculate()
	throws Exception
	{
		System.out.println(Paths.get("target/runtime").toFile().mkdirs());
		
		File file1 = Paths.get("target/runtime/csvReader1.csv").toFile();
		System.out.println(file1.delete());
		
		CsvWriter writer = new CsvWriter(file1);
		
		String oldest = String.valueOf(System.currentTimeMillis());
		String ts1 = String.valueOf(System.currentTimeMillis());
		Thread.sleep(2000);
		String ts2 = String.valueOf(System.currentTimeMillis());
		Thread.sleep(2000);
		String ts3 = String.valueOf(System.currentTimeMillis());
		
		writer.appendRow(ts3,"10");
		writer.appendRow(ts2,"20");
		writer.appendRow(ts1,"30");
		
		
		File file2 = Paths.get("target/runtime/csvReader2.csv").toFile();
		System.out.println(file2.delete());
		
		writer = new CsvWriter(file2);
		Thread.sleep(2000);
		ts1 = String.valueOf(System.currentTimeMillis());
		Thread.sleep(2000);
		ts2 = String.valueOf(System.currentTimeMillis());
		Thread.sleep(2000);
		ts3 = String.valueOf(System.currentTimeMillis());
		
		writer.appendRow(ts3,"10");
		writer.appendRow(ts2,"20");
		writer.appendRow(ts1,"30");
		
		
		File file3 = Paths.get("target/runtime/csvReader3.csv").toFile();
		System.out.println(file3.delete());
		
		writer = new CsvWriter(file3);
		Thread.sleep(2000);
		ts1 = String.valueOf(System.currentTimeMillis());
		Thread.sleep(2000);
		ts2 = String.valueOf(System.currentTimeMillis());
		Thread.sleep(2000);
		ts3 = String.valueOf(System.currentTimeMillis());
		
		
		writer.appendRow(ts3,"10");
		writer.appendRow(ts2,"20");
		writer.appendRow(ts1,"30");
		
		File merged = Paths.get("target/runtime/csvmerged.csv").toFile();
		IO.mergeFiles(merged, file1,file2,file3);
		
		CsvReader reader = new CsvReader(merged);
		
		assertEquals(9,reader.size());
		//assertEquals(ts1,reader.row(0).get(0));
		
		reader.sortRowsForIndexByType(0, DataType.Long);
		
		//TODO: flaky assertEquals(oldest,reader.row(0).get(0));
		
		SumStatsByMillisecondsFormular formula = new SumStatsByMillisecondsFormular(0, 1,1000);
		reader.calc(formula);
		
		System.out.println("formula:"+formula);
		
		assertEquals(20, formula.getAvg(),0);
		assertEquals(10, formula.getMin());
		assertEquals(30, formula.getMax());
		
		
		File file = Paths.get("target/runtime/csvReader.csv").toFile();
		System.out.println(file.delete());
		writer = new CsvWriter(file);
		
		writer.appendRow(ts3,"22");
		 reader = new CsvReader(file);
		 reader.calc(formula);
		 
		 System.out.println("formula:"+formula);
		 
		assertEquals(22, formula.getAvg(),0);
		assertEquals(22, formula.getMin());
		assertEquals(22, formula.getMax());
		
		
		System.out.println(file.delete());
		writer.appendRow("");
		 reader = new CsvReader(file);
		 reader.calc(formula);
			
			assertEquals(-1, formula.getAvg(),0);
			assertEquals(-1, formula.getMin());
			assertEquals(-1, formula.getMax());
			
	}

	@Test
	void size() throws IOException
	{
		StringReader reader = new StringReader(IO.reader().readClassPath("csv/csv_test.csv"));
		CsvReader subject = new CsvReader(reader);

		assertEquals(3,subject.size());
	}

	@Test
	void given_csvWith_DoubleQuotes_when_get_then_doubleQuotesReturned() throws IOException {

		StringReader reader = new StringReader("""
  		"string1",1
		""");
		CsvReader subject = new CsvReader(reader);

		String actual = subject.get(0,0,DataType.String);
		assertEquals("string1", actual);
	}

	@Test
	public void testParse() throws Exception
	{
		assertNull(CsvReader.parse(null));
		assertNull(CsvReader.parse(""));
		List<String> results = CsvReader.parse("1,2");
		assertNotNull(results);
		assertEquals("1", results.get(0));
		assertEquals("2", results.get(1));
		
		results = CsvReader.parse("\"1,2\"");
		assertEquals("1,2", results.get(0));
		
		
		results = CsvReader.parse("0,\"1,2\"");
		assertEquals("0", results.get(0));
		assertEquals("1,2", results.get(1));
		
		results = CsvReader.parse("0,\"1,2\",\"Greg's\"");
		assertEquals("0", results.get(0));
		assertEquals("1,2", results.get(1));
		assertEquals("Greg's", results.get(2));
		
		results = CsvReader.parse("0,\"1,2\",\"Greg's\",\"last");
		assertEquals("last", results.get(3));
		
		
		results = CsvReader.parse("\"0\",\"The \"\"GOOD\"\"\",2");
		
		assertEquals("0", results.get(0));
		assertEquals("The \"GOOD\"", results.get(1));
		assertEquals("2", results.get(2));
		
	}
	@Test
	void iterations()
	throws IOException
	{
		String line = "Hello,World";
		File file = Paths.get("build/tmp/line.csv").toFile();
		file.getParentFile().mkdirs();

		IO.writer().writeFile(file,line);
		CsvReader subject = new CsvReader(file);
		String actual = null;
		for (List<String> lineList: subject)
		{
			actual = lineList.get(0);
		}

		assertEquals("Hello",actual);
	}
	@Test
	void streams()
	throws IOException
	{
		String line = "Hello,World";
		File file = Paths.get("build/tmp/line.csv").toFile();
		file.getParentFile().mkdirs();

		IO.writer().writeFile(file,line);
		CsvReader subject = new CsvReader(file);
		List<String> actual = new ArrayList<>();

		subject.stream().forEach(l -> actual.add(l.get(0)));
		assertEquals("Hello",actual.get(0));

	}

	@Test
	void singleLineFileSize()
	throws IOException
	{
		String line = "Hello,World";
		File file = Paths.get("build/tmp/line.csv").toFile();
		file.getParentFile().mkdirs();

		IO.writer().writeFile(file,line);
		CsvReader reader = new CsvReader(file);
		assertEquals(1,reader.size());

	}


	@Test
	void fixStringIndexOutOfBoundsException() throws IOException
	{
		String csv = IO.reader().readClassPath("csv/tweets.csv");

		assertThrows(FormatException.class , () -> { CsvReader reader = new CsvReader(new StringReader(csv));});
	}

	@Test
	public void multipleStringReader()
	throws Exception
	{
		StringReader reader = new StringReader("one\ntwo");
		CsvReader subject = new CsvReader(reader);
		
		
		List<String> row = subject.row(0);
		assertEquals("one",row.get(0));
		row = subject.row(1);
		assertEquals("two",row.get(0));
		
	}

	@Test
	void getData() throws IOException {
		StringReader reader = new StringReader("one\ntwo");
		CsvReader subject = new CsvReader(reader);

		assertThat(subject.getData().size()).isEqualTo(2);
	}

	@Test
	void given_csvContent_when_orderByColsGroupByCols() throws IOException {

		List<String> expected = asList(
				"""
"1","c2","p4","23"
"1","c2","p5","23"

    """.trim(),
				"""
"3","c1","pc","5"
"3","c1","pb","3"
"3","c1","pc","4"

""".trim(),
				"""
"5","c1","pc","4"

    """.trim()

		);

		var input = """
"3","c1","pc","5"
"1","c2","p4","23"
"3","c1","pb","3"
"1","c2","p5","23"
"1","c3","p5","23"
"3","c1","pc","4"
"3","c3","pc","4"
"5","c1","pc","4"
"4","c1","pc","4"
"0","c1","pc","4"
""";

		var reader = new StringReader(input);

		var subject = new CsvReader(reader);
		List<String> actual  = subject.selectBuilder()
                .where( line ->
                        (line.get(0).equals("3") || line.get(0).equals("1"))
                                && (line.get(1).equals("c1") || line.get(1).equals("c3"))
                        )
				.orderBy(2)
				.groupBy(0)
				.buildCsvText();

		assertEquals(5, actual.size());

		for (String out:actual) {
			System.out.println("----START--------");
			System.out.println(out);
			System.out.println("-----END-------");
		}
	}
}
