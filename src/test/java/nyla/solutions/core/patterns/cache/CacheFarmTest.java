package nyla.solutions.core.patterns.cache;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CacheFarmTest
{

	@Test
	public void testGet_Expire()
	{
		Cache<String, String> cache = CacheFarm.getCache();
		
		cache.put("test","hello");
		assertEquals("hello",cache.get("test"));
	}

}
