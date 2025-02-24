package nyla.solutions.spring.batch;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import nyla.solutions.core.data.DataRow;
import nyla.solutions.spring.batch.CsvArrayableSkipListener;

@Ignore
public class CsvSkipListenerTest
{

	@Test
	public void testOnSkipInProcess()
	{
		CsvArrayableSkipListener skipper = new CsvArrayableSkipListener();
		
		skipper.setSkipInProcessFilePath("${inReadFile}");
		
		Exception exception = new Exception("error");
		
		Object[] inputs = {};
		DataRow dataRow = new DataRow(inputs);
		skipper.onSkipInProcess(dataRow, exception);
	}

	@Test
	public void testOnSkipInRead()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testOnSkipInWrite()
	{
		fail("Not yet implemented");
	}


}
