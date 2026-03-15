package nyla.solutions.core.util;

import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JavaBeanMoreTest {

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
        private SimpleBean child;
        private Collection<SimpleBean> list;
        private Map<String,String> attrs;
        public NestedBean() {}
        public SimpleBean getChild() { return child; }
        public void setChild(SimpleBean child) { this.child = child; }
        public Collection<SimpleBean> getList() { return list; }
        public void setList(Collection<SimpleBean> list) { this.list = list; }
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
        SimpleBean s1 = new SimpleBean("A", 10);
        SimpleBean s2 = new SimpleBean("B", 20);

        List<SimpleBean> list = List.of(s1, s2);

        NestedBean nb = new NestedBean();
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
        SimpleBean s1 = new SimpleBean("A", 10);
        SimpleBean s2 = new SimpleBean("B", 20);
        List<SimpleBean> list = List.of(s1, s2);

        Collection<Object> names = JavaBean.getCollectionProperties(list, "name");
        assertEquals(2, names.size());
        Iterator<Object> it = names.iterator();
        assertEquals("A", it.next());
        assertEquals("B", it.next());
    }

    @Test
    void getMappedProperty_and_getSimpleProperty_and_getIndexedProperty_and_getNestedProperty() throws Exception {
        NestedBean nb = new NestedBean();
        nb.setAttrs(new HashMap<>());
        nb.getAttrs().put("one","uno");

        // mapped via Map
        Object mapped = JavaBean.getMappedProperty(nb, "attrs", "one");
        assertEquals("uno", mapped);

        // simple property
        SimpleBean s = new SimpleBean("X", 5);
        Object simple = JavaBean.getSimpleProperty(s, "name");
        assertEquals("X", simple);

        // indexed property
        IndexedBean ib = new IndexedBean("zero","one","two");
        Object indexed = JavaBean.getIndexedProperty(ib, "items[1]");
        assertEquals("one", indexed);

        // nested property using getNestedProperty
        NestedBean big = new NestedBean();
        big.setChild(s);
        Object nestedName = JavaBean.getNestedProperty(big, "child.name");
        assertEquals("X", nestedName);

        // collection nested property via getNestedProperty
        List<NestedBean> nlist = List.of(big);
        Object coll = JavaBean.getNestedProperty(nlist, "child.name");
        assertTrue(coll instanceof Collection<?>);
    }

    @Test
    void getProperty_for_record() throws Exception {
        PersonRecord pr = new PersonRecord("Rec", 99);
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
        SimpleBean child = new SimpleBean("C",1);
        NestedBean nb = new NestedBean();
        nb.setChild(child);
        PropertyDescriptor pd = JavaBean.getPropertyDescriptor(nb, "child.name");
        assertNotNull(pd);
        assertEquals("name", pd.getName());
    }

    @Test
    void toMap_and_keySet_and_toNestedMap_collectionHandling() throws Exception {
        SimpleBean s = new SimpleBean("The", 1);
        Map<Object,Object> map = JavaBean.toMap(s);
        assertNotNull(map);
        assertTrue(map.containsKey("name"));
        Set<Object> keys = JavaBean.keySet(s);
        assertTrue(keys.contains("name"));

        // toNestedMap for simple should return map of properties
        Map<?,?> nm = JavaBean.toNestedMap(s);
        assertTrue(nm.containsKey("name"));

        // toCollectionMap for collection of nested beans
        List<SimpleBean> list = List.of(s);
        Collection<Object> cm = JavaBean.toCollectionMap(list);
        assertEquals(1, cm.size());
    }
}

