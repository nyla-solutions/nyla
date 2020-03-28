package nyla.solutions.core.patterns.iteration;



import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class PagingCollectionTest
{

	@Test
	public void testEqualsObject()
	{
		
		PagingCollection<String> collection1 = new PagingCollection<>(Collections.singleton("hello"), new PageCriteria());
		PagingCollection<String> collection2 = new PagingCollection<>(Collections.singleton("hello"), new PageCriteria());
		assertEquals(collection1, collection2);
		
		collection2 = new PagingCollection<>(Collections.singleton("world"), new PageCriteria());
		assertNotEquals(collection1, collection2);
	}

}
