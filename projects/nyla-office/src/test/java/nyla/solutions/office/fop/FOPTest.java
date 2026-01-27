package nyla.solutions.office.fop;

import java.io.File;

import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FOPTest
{


	@Test
	public void test()
	throws Exception
	{
		String fo = IO.reader().readClassPath("pdf/example.fop");
		
		File file = new File("src/test/resources/pdf/test.pdf");
		boolean results = file.delete();
		
		Debugger.println("previous deleted:"+results);
		
		FOP.writePDF(fo, file);
		Assertions.assertTrue(file.exists());
		
	}

}
