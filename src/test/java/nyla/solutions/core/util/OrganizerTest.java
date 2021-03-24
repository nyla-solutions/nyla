package nyla.solutions.core.util;

import nyla.solutions.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Organizer
 * 
 * @author Gregory Green
 *
 */
public class OrganizerTest
{

	@Test
	void toQueue()
	{
		assertNull(Organizer.toQueue());
		assertNotNull(Organizer.toQueue("1"));
		assertEquals(1,Organizer.toQueue("1").size());
	}

	@Test
	void toArrayString()
	{
		assertNull(Organizer.toArrayString(null));

		assertNull(Organizer.toArrayString(Collections.emptyList()));

		String expected = "hi";
		Collection<String> expectedList = Arrays.asList(expected);
		String[] actual = Organizer.toArrayString(expectedList);
		assertTrue(actual != null && actual.length > 0);
		assertEquals(expected,actual[0]);
	}

	@Test
	public void testAt() throws Exception
	{
		String [] args = {"0","1"};
		
		assertNull(Organizer.at(0, null));
		assertEquals("0",Organizer.at(0, args));
		assertEquals("1",Organizer.at(1, args));
		assertNull(Organizer.at(3, args));
		assertNull(Organizer.at(-1, args));
		
		Integer [] nums = {1,2,3}; 
		assertEquals(Integer.valueOf(1),Organizer.at(0, nums));
		assertEquals(Integer.valueOf(2),Organizer.at(1, nums));
		assertNull(Organizer.at(4, nums));
		assertNull(Organizer.at(-1, nums));
	}//------------------------------------------------
	
	@Test
	public void testFlatten() throws Exception
	{
		Collection<String> c = Collections.singleton(null);
		ArrayList<String> a = new ArrayList<>();
		Organizer.flatten(c, a);
		
		assertTrue(a.isEmpty());
		
		
		c = new HashSet<>(10);
		
		a = new ArrayList<>();
		Organizer.flatten(c, a);
		
		assertTrue(a.isEmpty());
		
		
	}

	@Test
	public void testFirst()
	{
		assertNull(Organizer.first(null));
		String [] text = {"first"};
		
		assertEquals("first",Organizer.first(text));
		
		String [] nums = {"one","second"};
		assertEquals("one",Organizer.first(nums));
	}
	@Test
	public void testFill()
	{
		assertNull(Organizer.fill(0,null));
		
		assertNull(Organizer.fill(1,null));
		assertNull(Organizer.fill(3,null));
		
		String value = "";
		Collection<String> results = Organizer.fill(1,value);
		assertEquals(1, results.size());
		
		for (String string : results)
		{
			assertEquals(string,value);
		}
		
	}//------------------------------------------------
	@Test
	public void testToSet() throws Exception
	{
		assertNull(Organizer.toSet());
		
		assertTrue(Organizer.toSet("a","b") !=null);
		assertTrue(Organizer.toSet("a","b").size() ==2);
		
	}//------------------------------------------------
	@Test
	public void testToList() throws Exception
	{
		assertNull(Organizer.toList());
		
		assertTrue(Organizer.toList("a","b").size() ==2);
		
	}//------------------------------------------------

	@Test
	void toArrayList()
	{
		assertNull(Organizer.toArrayList(null));
		assertNull(Organizer.toArrayList(Collections.EMPTY_LIST));


		String expectedText = "hi";
		List<String> expected =  Organizer.toList(expectedText);
		ArrayList<String> actual = Organizer.toArrayList(expected);
		assertEquals(expected,actual);

	}

	@Test
	void toMap()
	{
		Object[] expected = null;
		Map<String, String> actual =  Organizer.toMap(expected);
		assertNull(actual);

		expected = new Object[0];
		actual =  Organizer.toMap(expected);
		assertNull(actual);

		actual =  Organizer.toMap("hello");
		assertNotNull(actual);
		assertNull(actual.get("hello"));

		actual = Organizer.toMap("hello","world");
		assertNotNull(actual);
		assertEquals("world", actual.get("hello"));


		actual = Organizer.toMap("hello","world","imani");
		assertNotNull(actual);
		assertNull(actual.get("imani"));


		assertTrue(actual.keySet().contains("hello"));
		assertTrue(actual.keySet().contains("imani"));
	}



	@Test
		public void testToPages()
		{
			assertNull(Organizer.toPages(null,0));
			assertNull(Organizer.toPages(new ArrayList<String>(),0));
		
			assertNotNull(Organizer.toPages(Collections.singleton(null),1));
			
			assertEquals(2,Organizer.toPages(Arrays.asList(1,1,1,1), 2).size());
			
			assertEquals(3,Organizer.toPages(Arrays.asList(1,1,1,1,3), 2).size());
			
			List<Collection<Integer>> list = Organizer.toPages(Arrays.asList(1,1,1,1,3),2);

			assertEquals(Integer.valueOf(3), list.get(2).iterator().next());
		}

		@Test
		public void testToKeyPages()
		{
			assertNull(Organizer.toKeyPages(null,0));
			assertNull(Organizer.toKeyPages(new ArrayList<Map.Entry<Object, Object>>(),0));
		
			assertNull(Organizer.toKeyPages(Collections.singleton(null),0));
			
			assertEquals(2,Organizer.toKeyPages(Arrays.asList(
			new MapEntry<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(1)),
			new MapEntry<Integer, Integer>(Integer.valueOf(2), Integer.valueOf(2)),
			new MapEntry<Integer, Integer>(Integer.valueOf(3), Integer.valueOf(3)),
			new MapEntry<Integer, Integer>(Integer.valueOf(4), Integer.valueOf(4))
			), 2).size());
			
			assertEquals(3,Organizer.toKeyPages(Arrays.asList(
			new MapEntry<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(1)),
			new MapEntry<Integer, Integer>(Integer.valueOf(2), Integer.valueOf(2)),
			new MapEntry<Integer, Integer>(Integer.valueOf(3), Integer.valueOf(3)),
			new MapEntry<Integer, Integer>(Integer.valueOf(4), Integer.valueOf(4)),
			new MapEntry<Integer, Integer>(Integer.valueOf(5), Integer.valueOf(5))
			), 2).size());
			
					
			TreeSet<Map.Entry<Integer, Integer>> set = new TreeSet<Map.Entry<Integer, Integer>>(new BeanComparator("value"));
			
			set.addAll(Arrays.asList(
			new MapEntry<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(125)),
			new MapEntry<Integer, Integer>(Integer.valueOf(2), Integer.valueOf(122)),
			new MapEntry<Integer, Integer>(Integer.valueOf(3), Integer.valueOf(123)),
			new MapEntry<Integer, Integer>(Integer.valueOf(4), Integer.valueOf(124)),
			new MapEntry<Integer, Integer>(Integer.valueOf(5), Integer.valueOf(121))
			));
			
			List<Collection<Integer>> list = Organizer.toKeyPages(set, 2);

			assertEquals(Integer.valueOf(1), list.get(2).iterator().next());
		}
}
