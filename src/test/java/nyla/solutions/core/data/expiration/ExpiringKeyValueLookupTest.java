package nyla.solutions.core.data.expiration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpiringKeyValueLookupTest
{

	@Test
	public void test_expiration()
	throws Exception
	{
		ExpiringKeyValueLookup<String,String> bag = ExpiringKeyValueLookup.withExpirationMS(1000);
		assertNotNull(bag);
		bag.putEntry("1","1");
		
		assertEquals("1",bag.getValue("1"));
		Thread.sleep(1001);
		
		assertNull(bag.getValue("1"));
	}

}
