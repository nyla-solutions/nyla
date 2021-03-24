package nyla.solutions.core.data.expiration;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExpiringItemTest
{

	@Test
	public void test_expiration()
	throws Exception
	{
		ExpiringItem<String> e = new ExpiringItem<>("hello",
			LocalDateTime.now().plusSeconds(1));
		
		assertEquals("hello",e.value());
		
		Thread.sleep(1050);
		
		assertNull(e.value());
		
	}

}
