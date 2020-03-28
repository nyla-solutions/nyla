package nyla.solutions.core.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CriteriaTest
{

	@Test
	public void test_Criteria_isNull()
	{
		Criteria from = null; 
		Criteria c = new Criteria(from);
		assertEquals(-1, c.getPrimaryKey());
		assertNull(c.getId());
	}
	
	@Test
	public void test_Criteria_notNull()
	{
		Criteria from = new Criteria(1); 
		Criteria c = new Criteria(from);
		assertEquals(1, c.getPrimaryKey());
		assertEquals("1",c.getId());
	}
	
	

}
