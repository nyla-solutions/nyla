package nyla.solutions.core.data.expiration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpiringKeyValueLookupTest
{
	private ExpiringKeyValueLookup<String,String> subject;
	private long maxSize = 2;

	@BeforeEach
	void setUp()
	{
		subject = ExpiringKeyValueLookup.withExpirationMS(1000);
	}

	@Test
	public void test_expiration()
	throws Exception
	{
		assertNotNull(subject);
		subject.putEntry("1","1");
		
		assertEquals("1",subject.getValue("1"));
		Thread.sleep(1001);
		
		assertNull(subject.getValue("1"));
	}


	@Test
	void whenAddBeyondMaxSize() throws InterruptedException
	{
		subject.setMaxSize(maxSize);
		subject.putEntry("1","1");
		Thread.sleep(1);
		subject.putEntry("2","2");
		subject.putEntry("3","3");

		assertEquals(2,subject.size());
		assertNull(subject.getValue("1"));
		assertEquals("2",subject.getValue("2"));
		assertEquals("3",subject.getValue("3"));
	}
}
