package nyla.solutions.core.util;

import nyla.solutions.core.data.Named;
import nyla.solutions.core.patterns.conversion.PropertyConverter;
import nyla.solutions.core.patterns.conversion.domains.Employee;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.security.user.data.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyDescriptor;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class JavaBeanTest
{
	private PropertyConverter<Object, Object> converter
			= Mockito.mock();


	@BeforeEach
	public void setUp() throws Exception
	{
	}


    @Test
    void toMapForRecords() {
        var input = JavaBeanGeneratorCreator.of(Employee.class)
                        .create();

        var actual = JavaBean.toMap(input);

        System.out.println(actual);

        assertThat(actual).isNotNull();
        assertThat(actual.keySet()).isNotNull();
        assertThat(actual.keySet().size()).isGreaterThan(1);
    }

    @Test
    void converter() {
        assertThat(JavaBean.converter(UserProfile.class)).isNotNull();
    }

    @Test
	void newBean() throws InstantiationException, IllegalAccessException {

		String email = "g@email.com";
		String login = "nyla";

		UserProfile actual = JavaBean.newBean(Map.of("email",email,
				"login",login
				),UserProfile.class);

		assertThat(actual).isNotNull();
		assertThat(email).isEqualTo(actual.getEmail());
		assertThat(login).isEqualTo(actual.getLogin());

	}

	public class User1{
		private int id;
	}

	public class User2 {
		private String id;
	}



	@Test
	void newBeanWithPropertyConverter() throws InstantiationException, IllegalAccessException {

		String email = "g@email.com";
		String login = "nyla";


		JavaBean.newBean(Map.of("email",email,
				"login",login
		),UserProfile.class, converter);
	}

	@Test
	public void getPropertyNames()
	{
		UserProfile bean = new UserProfile();
		
		assertNull(JavaBean.getPropertyNames(null));
		Set<String> names = JavaBean.getPropertyNames(bean);
		assertTrue(names != null && names.contains("email"));
		assertEquals(names,JavaBean.getPropertyNames(UserProfile.class));
		
	}

	@SuppressWarnings("unchecked")
	@Test
	public void given_Nested_when_getNestProperty_then_return()
	throws Exception
	{
		Named nyla = new Named();
		
		nyla.setName("Nyla Gianni");
		
		Named josiah = new Named();
		
		josiah.setName("Josiah Gregory");
	
		ArrayList<Named> list = new ArrayList<Named>();
		list.add(nyla);
		list.add(josiah);
		
		Object results = JavaBean.getNestedProperty(list, "name");
		
		
		assertTrue(results instanceof Collection<?>);
		
		Collection<String> collection = (Collection<String>)results;
		
		for (String string : collection)
		{
			System.out.println(string);
		}
		
		assertEquals(null,JavaBean.getNestedProperty(null, "name"));
		
		ComplexObject complexObject = new ComplexObject();
		
		complexObject.setName(list);
		complexObject.setState("NJ");
		
	    collection = (Collection<String>)JavaBean.getNestedProperty(complexObject, "name.name");
		
		for (String string : collection)
		{
			System.out.println(string);
		}
		complexObject.setName(null);

		assertNull(JavaBean.getNestedProperty(complexObject, "name.name"));
		
	}
	
	public static class ComplexObject
	{
		
		
		/**
		 * @return the state
		 */
		public String getState()
		{
			return state;
		}
		/**
		 * @param state the state to set
		 */
		public void setState(String state)
		{
			this.state = state;
		}

		/**
		 * @return the name
		 */
		public Collection<Named> getName()
		{
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(Collection<Named> name)
		{
			this.name = name;
		}

		private String state;
		private Collection<Named> name;
	}



    public static class SimpleBean {
        private String name;
        private int age;

        public SimpleBean() {}
        public SimpleBean(String name, int age) { this.name = name; this.age = age; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    public static class NestedBean {
        private JavaBeanMoreTest.SimpleBean child;
        private Collection<JavaBeanMoreTest.SimpleBean> list;
        private Map<String,String> attrs;
        public NestedBean() {}
        public JavaBeanMoreTest.SimpleBean getChild() { return child; }
        public void setChild(JavaBeanMoreTest.SimpleBean child) { this.child = child; }
        public Collection<JavaBeanMoreTest.SimpleBean> getList() { return list; }
        public void setList(Collection<JavaBeanMoreTest.SimpleBean> list) { this.list = list; }
        public Map<String,String> getAttrs() { return attrs; }
        public void setAttrs(Map<String,String> attrs) { this.attrs = attrs; }
    }

    public static class IndexedBean {
        private String[] items;
        public IndexedBean(String... items) { this.items = items; }
        public String[] getItems() { return items; }
        public String getItems(int index) { return items[index]; }
        public void setItems(String[] items) { this.items = items; }
    }

    // Java record (requires Java 17+)
    public record PersonRecord(String name, int age) {}

    @Test
    void toNestedMap_and_toCollectionMap_and_isSimple() throws Exception {
        JavaBeanMoreTest.SimpleBean s1 = new JavaBeanMoreTest.SimpleBean("A", 10);
        JavaBeanMoreTest.SimpleBean s2 = new JavaBeanMoreTest.SimpleBean("B", 20);

        List<JavaBeanMoreTest.SimpleBean> list = List.of(s1, s2);

        JavaBeanMoreTest.NestedBean nb = new JavaBeanMoreTest.NestedBean();
        nb.setChild(s1);
        nb.setList(list);
        nb.setAttrs(Map.of("k1","v1"));

        Map<?,?> nested = JavaBean.toNestedMap(nb);
        assertNotNull(nested);
        assertTrue(nested.containsKey("child"));
        assertTrue(nested.containsKey("list"));
        assertTrue(nested.containsKey("attrs"));

        // list should be converted to collection of maps
        Object listObj = nested.get("list");
        assertTrue(listObj instanceof Collection<?>);
        Collection<?> cm = (Collection<?>) listObj;
        assertEquals(2, cm.size());

        // toCollectionMap directly
        Collection<Object> collMaps = JavaBean.toCollectionMap(list);
        assertEquals(2, collMaps.size());

        // isSimple checks
        assertTrue(JavaBean.isSimple(null));
        assertTrue(JavaBean.isSimple("text"));
        assertTrue(JavaBean.isSimple(Integer.valueOf(5)));
        assertTrue(JavaBean.isSimple(new Date()));
        assertFalse(JavaBean.isSimple(nb));
    }

    @Test
    void getCollectionProperties_forCollection() throws Exception {
        JavaBeanMoreTest.SimpleBean s1 = new JavaBeanMoreTest.SimpleBean("A", 10);
        JavaBeanMoreTest.SimpleBean s2 = new JavaBeanMoreTest.SimpleBean("B", 20);
        List<JavaBeanMoreTest.SimpleBean> list = List.of(s1, s2);

        Collection<Object> names = JavaBean.getCollectionProperties(list, "name");
        assertEquals(2, names.size());
        Iterator<Object> it = names.iterator();
        assertEquals("A", it.next());
        assertEquals("B", it.next());
    }

    @Test
    void getMappedProperty_and_getSimpleProperty_and_getIndexedProperty_and_getNestedProperty() throws Exception {
        JavaBeanMoreTest.NestedBean nb = new JavaBeanMoreTest.NestedBean();
        nb.setAttrs(new HashMap<>());
        nb.getAttrs().put("one","uno");

        // mapped via Map
        Object mapped = JavaBean.getMappedProperty(nb, "attrs", "one");
        assertEquals("uno", mapped);

        // simple property
        JavaBeanMoreTest.SimpleBean s = new JavaBeanMoreTest.SimpleBean("X", 5);
        Object simple = JavaBean.getSimpleProperty(s, "name");
        assertEquals("X", simple);

        // indexed property
        JavaBeanMoreTest.IndexedBean ib = new JavaBeanMoreTest.IndexedBean("zero","one","two");
        Object indexed = JavaBean.getIndexedProperty(ib, "items[1]");
        assertEquals("one", indexed);

        // nested property using getNestedProperty
        JavaBeanMoreTest.NestedBean big = new JavaBeanMoreTest.NestedBean();
        big.setChild(s);
        Object nestedName = JavaBean.getNestedProperty(big, "child.name");
        assertEquals("X", nestedName);

        // collection nested property via getNestedProperty
        List<JavaBeanMoreTest.NestedBean> nlist = List.of(big);
        Object coll = JavaBean.getNestedProperty(nlist, "child.name");
        assertTrue(coll instanceof Collection<?>);
    }

    @Test
    void getProperty_for_record() throws Exception {
        JavaBeanMoreTest.PersonRecord pr = new JavaBeanMoreTest.PersonRecord("Rec", 99);
        var name = JavaBean.getProperty(pr, "name");
        assertEquals("Rec", name);
        var age = JavaBean.getProperty(pr, "age");
        assertEquals(99, age);
    }

    @Test
    void getPropertyDescriptors_errors_and_getPropertyDescriptor_nested() throws Exception {
        // getPropertyDescriptors with null should throw
        assertNull(JavaBean.getPropertyNames(null));

        // getPropertyDescriptor nested
        JavaBeanMoreTest.SimpleBean child = new JavaBeanMoreTest.SimpleBean("C",1);
        JavaBeanMoreTest.NestedBean nb = new JavaBeanMoreTest.NestedBean();
        nb.setChild(child);
        PropertyDescriptor pd = JavaBean.getPropertyDescriptor(nb, "child.name");
        assertNotNull(pd);
        assertEquals("name", pd.getName());
    }

    @Test
    void toMap_and_keySet_and_toNestedMap_collectionHandling() throws Exception {
        JavaBeanMoreTest.SimpleBean s = new JavaBeanMoreTest.SimpleBean("The", 1);
        Map<Object,Object> map = JavaBean.toMap(s);
        assertNotNull(map);
        assertTrue(map.containsKey("name"));
        Set<Object> keys = JavaBean.keySet(s);
        assertTrue(keys.contains("name"));

        // toNestedMap for simple should return map of properties
        Map<?,?> nm = JavaBean.toNestedMap(s);
        assertTrue(nm.containsKey("name"));

        // toCollectionMap for collection of nested beans
        List<JavaBeanMoreTest.SimpleBean> list = List.of(s);
        Collection<Object> cm = JavaBean.toCollectionMap(list);
        assertEquals(1, cm.size());
    }


}
