package nyla.solutions.core.util;

import nyla.solutions.core.data.MapEntry;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.iteration.PagingCollection;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.*;

import java.util.*;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Organizer
 * 
 * @author Gregory Green
 *
 */
public class OrganizerTest
{

	private List<Object> list;

	@BeforeEach
	void setUp() {
		list = asList("1","2","3");
	}

	@Test
	void getByIndex() {
		var actual = Organizer
				.arrange(Organizer.change().toList("1"))
				.getByIndex(0);

		assertEquals("1", actual);
	}


	@Test
	void given_listList_when_getByIndex_then_returnNull() {
		assertNull(Organizer.arrange((List)null).getByIndex(3));

		assertNull(Organizer.arrange(Organizer.change().toList(""))
				.getByIndex(3));
	}

	@Test
	void toQueue()
	{
		assertNull(Organizer.arrange().toQueue());
		assertNotNull(Organizer.arrange("1").toQueue());
		assertEquals(1, Organizer.arrange("1").size());
	}

	@Test
	void toArrayString()
	{
		assertNull(Organizer.change().toArrayString(null));

		assertNull(Organizer.change().toArrayString(Collections.emptyList()));

		String expected = "hi";
		Collection<String> expectedList = asList(expected);
		String[] actual = Organizer.change().toArrayString(expectedList);
		assertTrue(actual != null && actual.length > 0);
		assertEquals(expected,actual[0]);
	}

	@Test
	void add() {
		String item = "World";
		String[]  items = {"Hello"};
		var results = Organizer.arrange(item).add(items);
		assertEquals(2, results.length);
		assertThat(results).contains(item);
	}

	@Test
	void arrangeBug() {

		var list = new ArrayList<>(List.of("1","2","3"));

		var actual = Organizer.arrange(list).getByIndex(0);

		assertThat(actual).isEqualTo("1");
	}

	@Test
	void sortByJavaBeanProperty() {
		var propertyName =  "email";

		var user1 = UserProfile.builder().email("C")
				.firstName("C").lastName("C").login("C").build();
		var user2 = UserProfile.builder().email("B")
				.firstName("B").lastName("B").login("B").build();

		var user3 = UserProfile.builder().email("A")
				.firstName("A").lastName("A").login("A").build();

		Collection<?> collection = asList(user1,user2,user3);
		boolean descending = false;
		var actual = Organizer.sort().sortByJavaBeanProperty(propertyName,collection,descending);

		assertThat(actual).isNotNull();

		var iterator = actual.iterator();

		UserProfile first = (UserProfile)iterator.next();
		assertNotNull(first);
		String expected = "A";
		assertThat(first.getFirstName()).isEqualTo(expected);
		assertThat(first.getLastName()).isEqualTo(expected);
		assertThat(first.getEmail()).isEqualTo(expected);
		assertThat(first.getLogin()).isEqualTo(expected);


	}

	@Test
	public void at() throws Exception
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
	public void given_inputNullflatten_thenDoNothing() throws Exception
	{
		assertDoesNotThrow( ()-> Organizer.flats().flatten(null,null));

	}

	@DisplayName("Given Flatten")
	@Nested
	public class GivenFlatten{


		@DisplayName("When Input Empty list Then Do Nothing")
		@Test
		public void inputEmpty_ThenDoNothing() throws Exception
		{
			assertDoesNotThrow( ()-> Organizer.flats().flatten(Collections.EMPTY_LIST,null));
		}

		@DisplayName("When Input Null Then Do Nothing")
		@Test
		public void inputNull_ThenDoNothing() throws Exception
		{
			assertDoesNotThrow( ()-> Organizer.flats().flatten(null,null));
		}


		@DisplayName("When inputs and out not null Then return flatten ")
		@Test
		public void flatten() throws Exception
		{
			Collection<String> c = Collections.singleton(null);
			ArrayList<String> a = new ArrayList<>();
			Organizer.flats().flatten(c, a);

			assertTrue(a.isEmpty());

			c = new HashSet<>(10);
			a = new ArrayList<>();
			Organizer.flats().flatten(c, a);

			assertTrue(a.isEmpty());
		}

		@DisplayName("When list has a list Then result is a flat list")
		@Test
		void flattenListOfList() {
			Collection<?> input = asList("1",asList("2","3"));
			Collection<?> actual = new ArrayList<>();
			Organizer.flats().flatten(input,actual);

			assertNotNull(actual);
			assertEquals(3, actual.size());
			Collection<String> expected = asList("1","2","3");

			assertEquals(expected, actual);
		}
	}

	@DisplayName("Given FlattenPaging")
	@Nested
	public class FlattenPagingSuite{

		private PageCriteria criteria;

		@BeforeEach
		void setUp() {
			criteria= new PageCriteria();
		}

		@DisplayName("When null or empty then return null")
		@Test
		void flattenPaging_null() {
			assertNull(Organizer.flats().flattenPaging(null));
			assertNull(Organizer.flats().flattenPaging(Collections.emptyList()));
		}

		@Test
		void flattenPaging() {
			Collection<String> list1 = Organizer.change().toList("1","2");
			Collection<String> list2 = Organizer.change().toList("A","B");

			PagingCollection<String> pagingCollection1 = new PagingCollection<>(list1,criteria);
			PagingCollection<String> pagingCollection2 = new PagingCollection<>(list2,criteria);

			Collection<Paging<String>> input = asList(pagingCollection1,pagingCollection2);

			var actual = Organizer.flats().flattenPaging(input);

			assertNotNull(actual);
			assertEquals(4, actual.size());
		}
	}


	@Test
	public void first()
	{
		assertNull(Organizer.first(null));
		String [] text = {"first"};
		
		assertEquals("first",Organizer.first(text));
		
		String [] nums = {"one","second"};
		assertEquals("one",Organizer.first(nums));
	}

	@Test
	void toDoubles() {
		assertNull(Organizer.change().toDoubles(null));

		List<Double> doubles = Organizer.change().toList(3.3);
		var actual = Organizer.change().toDoubles(doubles);

		assertNotNull(actual);
		assertThat(actual[0]).isEqualTo(3.3);
	}

	@Test
	public void fill()
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
		
	}

	@Test
	public void toSet() throws Exception
	{
		assertNull(Organizer.change().toSet());
		
		assertTrue(Organizer.change().toSet("a","b") !=null);
		assertTrue(Organizer.change().toSet("a","b").size() ==2);
		
	}

	@Test
	public void testToList() throws Exception
	{
		assertNull(Organizer.change().toList());
		
		assertTrue(Organizer.change().toList("a","b").size() ==2);
		
	}

	@Test
	void toArrayList()
	{
		assertNull(Organizer.change().toArrayList(null));
		assertNull(Organizer.change().toArrayList(Collections.EMPTY_LIST));


		String expectedText = "hi";
		List<String> expected =  Organizer.change().toList(expectedText);
		ArrayList<String> actual = Organizer.change().toArrayList(expected);
		assertEquals(expected,actual);

	}

	@Test
	void toMap()
	{
		Object[] expected = null;
		Map<String, String> actual =  Organizer.change().toMap(expected);
		assertNull(actual);

		expected = new Object[0];
		actual =  Organizer.change().toMap(expected);
		assertNull(actual);

		actual =  Organizer.change().toMap("hello");
		assertNotNull(actual);
		assertNull(actual.get("hello"));

		actual = Organizer.change().toMap("hello","world");
		assertNotNull(actual);
		assertEquals("world", actual.get("hello"));


		actual = Organizer.change().toMap("hello","world","imani");
		assertNotNull(actual);
		assertNull(actual.get("imani"));


		assertTrue(actual.keySet().contains("hello"));
		assertTrue(actual.keySet().contains("imani"));
	}



	@Test
		public void testToPages()
		{
			assertNull(Organizer.change().toPages(null,0));
			assertNull(Organizer.change().toPages(new ArrayList<String>(),0));
		
			assertNotNull(Organizer.change().toPages(Collections.singleton(null),1));
			
			assertEquals(2,Organizer.change().toPages(asList(1,1,1,1), 2).size());
			
			assertEquals(3,Organizer.change().toPages(asList(1,1,1,1,3), 2).size());
			
			List<Collection<Integer>> list = Organizer.change().toPages(asList(1,1,1,1,3),2);

			assertEquals(Integer.valueOf(3), list.get(2).iterator().next());
		}

		@Test
		public void testToKeyPages()
		{
			assertNull(Organizer.change().toKeyPages(null,0));
			assertNull(Organizer.change().toKeyPages(new ArrayList<Map.Entry<Object, Object>>(),0));
		
			assertNull(Organizer.change().toKeyPages(Collections.singleton(null),0));
			
			assertEquals(2,Organizer.change().toKeyPages(asList(
			new MapEntry<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(1)),
			new MapEntry<Integer, Integer>(Integer.valueOf(2), Integer.valueOf(2)),
			new MapEntry<Integer, Integer>(Integer.valueOf(3), Integer.valueOf(3)),
			new MapEntry<Integer, Integer>(Integer.valueOf(4), Integer.valueOf(4))
			), 2).size());
			
			assertEquals(3,Organizer.change().toKeyPages(asList(
			new MapEntry<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(1)),
			new MapEntry<Integer, Integer>(Integer.valueOf(2), Integer.valueOf(2)),
			new MapEntry<Integer, Integer>(Integer.valueOf(3), Integer.valueOf(3)),
			new MapEntry<Integer, Integer>(Integer.valueOf(4), Integer.valueOf(4)),
			new MapEntry<Integer, Integer>(Integer.valueOf(5), Integer.valueOf(5))
			), 2).size());
			
					
			TreeSet<Map.Entry<Integer, Integer>> set = new TreeSet<Map.Entry<Integer, Integer>>(new BeanComparator("value"));
			
			set.addAll(asList(
			new MapEntry<Integer, Integer>(Integer.valueOf(1), Integer.valueOf(125)),
			new MapEntry<Integer, Integer>(Integer.valueOf(2), Integer.valueOf(122)),
			new MapEntry<Integer, Integer>(Integer.valueOf(3), Integer.valueOf(123)),
			new MapEntry<Integer, Integer>(Integer.valueOf(4), Integer.valueOf(124)),
			new MapEntry<Integer, Integer>(Integer.valueOf(5), Integer.valueOf(121))
			));
			
			List<Collection<Integer>> list = Organizer.change().toKeyPages(set, 2);

			assertEquals(Integer.valueOf(1), list.get(2).iterator().next());
		}

		@Nested
		@DisplayName("Given findMapValueByKey")
		class FindMapValueByKey
		{
			private Map<String,String> map = Organizer.change().toMap("a","1","b","2");

			@DisplayName("When all all Then Return")
			@Test
			void whenNullsReturnNull() {
				assertNull(Organizer.search().findValueByKeyWithDefault(null,null,null));
			}


			@Test
			void whenKeyInMapThenReturn() {
				String actual = Organizer.search().findValueByKeyWithDefault(map,"a","23");
				assertEquals("1", actual);
			}

			@Test
			void whenKeyNotInMapThenReturnDefault() {
				String actual = Organizer.search().findValueByKeyWithDefault(map,"z","23");
				assertEquals("23", actual);
			}
		}

	@Test
	void isStringIn_true() {

		assertThat(Organizer.search().isStringIn("hello","1","2","3","hello")).isTrue();
	}

	@Test
	void isStringIn_False() {

		assertThat(Organizer.search().isStringIn("imani","1","2","3","hello")).isFalse();
	}


	@Test
	void findByTextIgnoreCase() {
		String expected = "2";
		var actual = Organizer.search().findByTextIgnoreCase(list,expected);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void findByTextIgnoreCase_throwsNoDataFound() {
		assertNull(Organizer.search().findByTextIgnoreCase(list,"nowhere"));
	}

	@Test
	void addAll() {
		ArrayList<String> list = new ArrayList<>();
		String[] array = {"1","2"};

		Organizer.addAll(list,array);

		assertThat(list.size()).isEqualTo(array.length);

	}

	@Test
	void doesListContainData() {

		var user1 = new UserProfile();
		user1.setEmail("user1@email.com");

		var user2 = new UserProfile();
		user2.setTitle("user2 bos");

		Object[] objList = {user1,user2};
		var dataMap = Map.of("email",user1.getEmail());
		Assertions.assertTrue(Organizer.search().doesListContainData(objList,dataMap));

		dataMap = Map.of("title",user2.getTitle());
		assertTrue(Organizer.search().doesListContainData(objList,dataMap));

		dataMap = Map.of("noNo",user2.getTitle());
		assertFalse(Organizer.search().doesListContainData(objList,dataMap));
	}

	@Test
	void size() {

		assertEquals(1, Organizer.arrange("1").size());
		assertEquals(0, Organizer.arrange().size());
		assertEquals(3, Organizer.arrange("a","b","c").size());
	}
}
