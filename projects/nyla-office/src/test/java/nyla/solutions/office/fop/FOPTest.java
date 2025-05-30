package nyla.solutions.office.fop;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;

public class FOPTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void test()
	throws Exception
	{
		String fo = IO.readClassPath("pdf/example.fop");
		
		File file = new File("src/test/resources/pdf/test.pdf");
		boolean results = file.delete();
		
		Debugger.println("previous deleted:"+results);
		
		FOP.writePDF(fo, file);
		Assert.assertTrue(file.exists());
		
	}

}
