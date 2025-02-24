package nyla.solutions.spring.batch.file;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class FileItemReaderTest
{

	@Test
	public void testRead()
	throws Exception
	{
		FileItemReader fileItemReader = new FileItemReader(new File("src/test/resources/fileReaderTest"), "*.xml");
		
		File file = fileItemReader.read();
		
		Assert.assertNotNull(file);
		
		Assert.assertTrue(file.exists());
		
		file = fileItemReader.read();
		Assert.assertNotNull(file);
		
		file = fileItemReader.read();
		Assert.assertNull(file);
		
	}

}
