package nyla.solutions.core.patterns.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CacheFarmTest
{
	private CacheFarm<String, String> expected;

	@BeforeEach
	public void setUp()
	{
		expected = new CacheFarm<>();
	}

	@Test
	public void testGet_Expire()
	{
		expected.put("test","hello");
		assertEquals("hello", expected.get("test"));
	}

	@Test
	void test_clear()
	{
		expected.put("String","hello");
		assertFalse(expected.isEmpty());
		expected.clear();;
		assertTrue(expected.isEmpty());
	}

	@Test
	void test_clone()
	{
		expected.put("add","one");

		CacheFarm<String,String>  actual = (CacheFarm)expected.clone();
		assertEquals(actual.get("add"),expected.get("add"));
		assertEquals(actual.size(),expected.size());
		assertEquals(actual.entrySet(),expected.entrySet());
	}

	@Test
	void test_contains()
	{

		expected.put("child","Josiah");
		assertTrue(expected.contains("Josiah"));

	}

	@Test
	void test_containsValue()
	{

		expected.put("have","fun");
		assertTrue(expected.containsValue("fun"));

	}

	@Test
	void test_contains_key()
	{
		expected.put("food","burger");
		assertTrue(expected.containsKey("food"));
	}

	@Test
	void test_elements()
	{
		expected.put("savior","Jesus");
		assertTrue(expected.elements().hasMoreElements());
		assertEquals("Jesus",expected.elements().nextElement());
	}

	@Test
	void test_entrySet()
	{
		expected.put("d","John");
		expected.put("d2","James");

		assertEquals(2,expected.entrySet().size());
	}

	@Test
	void test_empty()
	{
		assertTrue(expected.isEmpty());
		expected.put("some","thing");
		assertFalse(expected.isEmpty());
	}

	@Test
	void test_keys()
	{
		expected.put("1","1");
		expected.put("2","2");
		assertTrue(expected.keys().hasMoreElements());
	}

	@Test
	void test_keySet()
	{
		expected.put("1","1");
		expected.put("2","2");
		assertEquals(2,expected.keySet().size());
	}

	@Test
	void test_putAll()
	{
		HashMap<String,String> map = new HashMap<>();
		map.put("fancy1","bree");
		map.put("fancy2","nancy");

		expected.putAll(map);

		assertEquals("bree",expected.get("fancy1"));
		assertEquals("nancy",expected.get("fancy2"));
	}

	@Test
	void test_remove()
	{
		expected.put("fancy","nancy");
		assertEquals("nancy",expected.get("fancy"));
		expected.remove("fancy");
		assertNull(expected.get("fancy"));

	}

	@Test
	void test_size()
	{
		assertEquals(0,expected.size());
		expected.put("jedi","Luke");
		assertEquals(1,expected.size());
		expected.put("jedi","Luke2");
		assertEquals(1,expected.size());
		expected.remove("jedi");
		assertEquals(0,expected.size());


	}

	@Test
	void test_values()
	{
		expected.put("jedi1","tano");
		expected.put("jedi2","ahsoka");
		assertTrue(expected.values().contains("tano"));
		assertTrue(expected.values().contains("ahsoka"));

	}
}
