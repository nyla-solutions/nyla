//package nyla.solutions.office.msoffice.excel;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class CSVTest
//{
//
//	@Before
//	public void setUp() throws Exception
//	{
//	}//--------------------------------------------------------
//	/**
//	 * General use case test
//	 * @throws IOException when an occur
//	 */
//	@Test
//	public void testCSV()
//	throws IOException
//	{
//		File file = new File("src/test/resources/csv/output.csv");
//		if(file.delete())
//			System.out.println("previous file deleted");
//		CSV.writeHeader(file, "header1","header2","header3");
//		CSV.appendFile(file, 11,12,13);
//
//		Object[] row2 = {21,22,23};
//		CSV.appendFile(file, row2);
//
//	}
//
//}
