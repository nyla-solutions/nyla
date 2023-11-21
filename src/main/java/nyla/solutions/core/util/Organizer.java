package nyla.solutions.core.util;

import nyla.solutions.core.data.*;
import nyla.solutions.core.exception.NoDataFoundException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.operations.logging.Log;
import nyla.solutions.core.patterns.expression.BooleanExpression;
import nyla.solutions.core.patterns.iteration.PageCriteria;
import nyla.solutions.core.patterns.iteration.Paging;
import nyla.solutions.core.patterns.iteration.PagingCollection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static nyla.solutions.core.util.Config.settings;

/**
 * <pre>
 *
 *  Organizer provides a set of functions to sort, filter and find elements in container objects
 *  such as collections, arrays and maps.
 *
 *  This object has methods to sort collections of object. It can compare a particular
 *  bean property value. Note that this class uses the JavaBean object that is
 *  also located in the same util directory.
 *
 *  The following is a sample web action that uses the Organizer to sort a list of sites Java bean objects.
 *
 *   ...
 *
 *  public Collection getSitesDetailList()
 *  {
 *  //     Here SiteAction Must be Called Like SiteAction.getSiteDetails
 *  this.sitesDetailList = MockUtils.getSomeSiteDetails();
 *
 *  String sortProperty = JSF.getRequestParameter(&quot;sortProperty&quot;);
 *  String filterNameProperty = JSF.getRequestParameter(&quot;filterPropertyName&quot;);
 *  Comparable filterValueProperty =
 *  (Comparable)JSF.getRequestParameter(&quot;filterValueProperty&quot;);
 *
 *  if (!Text.isNull(sortProperty))
 *  {
 *  sitesDetailList = Organizer.sortByJavaBeanProperty(sortProperty,
 *  sitesDetailList);
 *  }
 *  else if(!Text.isNull(filterNameProperty))
 *  {
 *  sitesDetailList = Organizer.filterByJavaBeanProperty((ArrayList)sitesDetailList,filterNameProperty,filterValueProperty);
 *  }
 *
 *  return sitesDetailList;
 *  }
 *
 *
 * </pre>
 *
 * @author Gregory Green
 * @version 2.0
 */
public final class Organizer
{
    private static final Log logger = Debugger.getLog(Organizer.class);

    /**
     * Constructor for Organizer initializes internal
     */
    private Organizer()
    {
    }

    public static class Arranger{
        final List<?> collection;

        public Arranger(List<?> collection) {
            if(collection == null)
                this.collection = Collections.emptyList();
            else
                this.collection = collection;
        }

        public <T> T getByIndex(int index) {
            if(index < 0 || index > collection.size())
                return null;

            return (T) collection.get(index);
        }

        public <T> Queue<T> toQueue() {
            if(collection.isEmpty())
                return null;

            LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>(collection.size());
            queue.addAll((List<T>)collection);
            return queue;
        }

        public int size() {
            return collection.size();
        }

        /**
         * @param <T>   the type class
         * @param array the array where item is added
         * @return the update array
         */
        public <T> T[] add(T[] array)
        {
            ArrayList<T> list = new ArrayList<>(array.length + 1);
            list.addAll(Arrays.asList(array));
            list.addAll((List)this.collection);

            return list.toArray(array);
        }
    }

    /**
     * @param <T>   the array of this type
     * @param array the array to select from
     * @return the first element
     */
    public static <T> T first(T[] array)
    {
        if (array == null || array.length < 1)
            return null;

        return array[0];
    }



    /**
     * <pre>
     * Flatten collection of collections into a collection of objects
     *
     * Usage
     * Collection<Serializable> flattedCollection = null;
     *
     * switch(returnType)
     * {
     * case tree: flattedCollection = new TreeSet<Serializable>();break;
     * case set: flattedCollection = new HashSet<Serializable>(results.size());break;
     * default: flattedCollection = new HashSet<Serializable>(results.size());
     * }
     *
     * Organizer.flatten((Collection)results, flattedCollection);
     * </pre>
     *
     * @param input         the input collection
     * @param flattenOutput the collection output
     * @param <T>           to flatten output type
     */
    @SuppressWarnings("unchecked")
    public static <T> void flatten(Collection<?> input,
                                   Collection<T> flattenOutput)
    {
        if (input == null || input.isEmpty() || flattenOutput == null)
            return;

        for (Object inputObj : input) {
            if (inputObj == null)
                continue;

            if (inputObj instanceof Collection)
                flatten((Collection<Object>) inputObj, flattenOutput);
            else
                flattenOutput.add((T) inputObj);

        }

    }

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that
     * @param <T> the paging the type class
     * @return to flatten collections into a single collection
     */
    public static <T> Paging<T> flattenPaging(Collection<Paging<T>> collectionOfPaging)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        PageCriteria pageCriteria = null;
        // get first page criteria
        Paging<T> firstPaging = collectionOfPaging.iterator().next();

        if (firstPaging != null)
            pageCriteria = firstPaging.getPageCriteria();

        return flattenPaging(collectionOfPaging, pageCriteria, null, null);

    }

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that need to be flattened
     * @param sorter             optional comparable for sorting
     * @param filter             optional filter, if filter.
     * @param <T>                the type class
     * @return to flatten collections into a single collection
     */
    public static <T> Paging<T> flattenPaging(Collection<Paging<T>> collectionOfPaging,
                                              Comparator<T> sorter, BooleanExpression<T> filter)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        PageCriteria pageCriteria = collectionOfPaging.iterator().next().getPageCriteria();
        return flattenPaging(collectionOfPaging, pageCriteria, sorter, filter);
    }

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that need to be flattened
     * @param sorter             optional comparable for sorting
     * @param filter             optional filter, if filter.
     * @param pageCriteria       the page criteria
     * @param <T>                the collection page object type
     * @return to flatten collections into a single collection
     */
    @SuppressWarnings("unchecked")
    public static <T> Paging<T> flattenPaging(Collection<?> collectionOfPaging, PageCriteria pageCriteria,
                                              Comparator<T> sorter, BooleanExpression<T> filter)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        Paging<T> pagingResults = null;

        if (sorter != null) {
            // Create tree set based paging
            TreeSet<T> treeSet = new TreeSet<>(sorter);
            pagingResults = new PagingCollection<>(treeSet, pageCriteria);
        }

        // Add all to an aggregated collection
        Paging<T> paging;
        for (Object item : collectionOfPaging) {
            if (item instanceof Paging) {
                paging = (Paging<T>) item;

                if (pagingResults != null)
                    addAll(pagingResults, paging, filter);
                else {
                    pagingResults = paging;
                }
            } else if (item != null) {
                // initialize paging results if needed
                if (pagingResults == null) {
                    if (sorter != null) {
                        // Create tree set based paging
                        TreeSet<T> treeSet = new TreeSet<>(sorter);
                        pagingResults = new PagingCollection<>(treeSet, pageCriteria);
                    } else
                        pagingResults = new PagingCollection<>(new ArrayList<>(), pageCriteria);

                }

                pagingResults.add((T) item);
            }
        }

        return pagingResults;
    }

    /**
     * Add all collections
     *
     * @param <T>           the type class
     * @param paging        the paging output
     * @param pagingResults the results to add to
     * @param filter        remove object where filter.getBoolean() == true
     */
    public static <T> void addAll(Collection<T> pagingResults, Collection<T> paging, BooleanExpression<T> filter)
    {
        if (pagingResults == null || paging == null)
            return;

        if (filter != null) {
            for (T obj : paging) {
                if (filter.apply(obj))
                    pagingResults.add(obj);
            }
        } else {
            // add independent of a filter
            pagingResults.addAll(paging);
        }
    }

    @SuppressWarnings(
            {"unchecked"})
    public static <T> List<T> toList(T... args)
    {
        if (args == null || args.length == 0)
            return null;

        ArrayList<T> list = new ArrayList<>(args.length);
        Collections.addAll(list, args);

        return list;
    }

    @SuppressWarnings(
            {"unchecked"})
    public static <T> Set<T> toSet(T... args)
    {
        if (args == null || args.length == 0)
            return null;

        HashSet<T> set = new HashSet<>(args.length);

        fill(set, args);
        return set;
    }

    @SuppressWarnings("unchecked")
    public static <T> void fill(Collection<T> collection, T... args)
    {
        if (collection == null || args == null)
            return;

        Collections.addAll(collection, args);
    }

    public static <T> List<Collection<T>> toPages(Collection<T> collection, int pageSize)
    {
        if (collection == null || collection.isEmpty())
            return null;

        int collectionSize = collection.size();

        if (pageSize <= 0 || collectionSize <= pageSize)
            return Collections.singletonList(collection);

        int initialSize = collectionSize / pageSize;

        ArrayList<Collection<T>> list = new ArrayList<>(initialSize);

        ArrayList<Object> current = new ArrayList<>();
        for (Object object : collection) {
            current.add(object);

            if (current.size() >= pageSize) {
                current.trimToSize();

                list.add((Collection<T>) current);
                current = new ArrayList<>();
            }
        }

        if (!current.isEmpty())
            list.add((Collection<T>) current);

        return list;

    }

    @SuppressWarnings(
            {"unchecked", "rawtypes"})
    public static <K, V> List<Collection<K>> toKeyPages(Collection<Map.Entry<K, V>> mapEntries, int pageSize)
    {
        if (mapEntries == null || mapEntries.isEmpty())
            return null;

        int collectionSize = mapEntries.size();

        if (pageSize <= 0 || collectionSize <= pageSize) {
            ArrayList<K> list = new ArrayList<>(mapEntries.size());
            for (Map.Entry<K, V> entry : mapEntries) {
                if (entry == null)
                    continue;

                list.add(entry.getKey());
            }

            if (list.isEmpty())
                return null;

            return Collections.singletonList(list);
        }

        int initialSize = collectionSize / pageSize;

        ArrayList<Collection<K>> list = new ArrayList<>(initialSize);

        ArrayList<K> current = new ArrayList<>();
        for (Map.Entry<K, V> entry : mapEntries) {
            current.add(entry.getKey());

            if (current.size() >= pageSize) {
                current.trimToSize();

                list.add(current);
                current = new ArrayList<>();
            }
        }

        if (!current.isEmpty())
            list.add(current);

        return list;

    }

    /**
     * Find the value with a given key in the map.
     *
     * @param map          the map with key/value pairs
     * @param key          the map key
     * @param defaultValue this is returned if the value is now found
     * @return the single value found
     */
    public static <T> T findValueByKeyWithDefault(Map<?, ?> map, Object key,
                                           T defaultValue)
    {
        if (key == null || map == null)
            return defaultValue;

        Object value = map.get(key);

        if (value == null)
            return defaultValue;

        return (T)value;
    }

    /**
     * @param text the text to search for
     * @param list the list of strings
     * @return true if aText in aList
     */
    public static boolean isStringIn(String text, String... list)
    {
        if (text == null)
            return false;

        for (String s : list) {
            if (text.equals(s))
                return true;
        }

        return false;
    }

    /**
     *
     * @param collection the collection
     * @param text text to find
     * @return
     */
    public static <T> T findByTextIgnoreCase(Collection<T> collection,
                                              String text)
    {
        if (text == null)
            return null;

        if (collection == null)
            throw new RequiredException(
                    "aCollection in Organizer.findIgnoreCase");

        for (T element : collection) {

            if (text.equalsIgnoreCase(element.toString()))
                return element;
        }

        return null;
    }

    /**
     * Add object to a list
     *
     * @param <T> the input type
     * @param list    where objects will be added
     * @param objects the object add
     */
    public static <T> void addAll(Collection<T> list, T[] objects)
    {
        list.addAll(Arrays.asList(objects));
    }

    /**
     * @param objects the object to check
     * @param dataMap the data map
     * @return true if the object's property contains data in test Map
     */
    @SuppressWarnings("unchecked")
    static <K, V> boolean doesListContainData(Object[] objects, Map<K, V> dataMap)
    {
        if (objects == null || objects.length == 0)
            return false;

        Map<K, V> objectMap;
        for (Object object : objects) {
            // get properties for first object
            objectMap = (Map<K, V>) JavaBean.toMap(object);

            if (doesMapContainData((Map<Object, Object>) objectMap, (Map<Object, Object>) dataMap))
                return true;
        }

        return false;

    }

    /**
     * @param aMap  the map
     * @param aData the key/values to check
     * @return true if all data in aData exist in aMap
     */
    public static boolean doesMapContainData(Map<Object, Object> aMap, Map<Object, Object> aData)
    {
        // compare with testMap
        Object testMapKey;
        for (Map.Entry<Object, Object> entry : aData.entrySet()) {
            // get testMap Key
            testMapKey = entry.getKey();

            // test if values are equals

            if (
                    !String.valueOf(aMap.get(testMapKey)).equals(
                            String.valueOf(entry.getValue()))
            ) {
                // not equal continue skip
                return false;
            }
        }
        return true;
    }

    public static <T> T[] copy(Object[] objects)
    {
        if (objects == null)
            return null;

        Object[] results = new Object[objects.length];
        System.arraycopy(objects, 0, results, 0, results.length);

        return (T[]) results;
    }

    /**
     * Copy collection objects to a given array
     *
     * @param collection the collection source
     * @param objects    array destination
     */
    public static void copyToArray(Collection<Object> collection, Object[] objects)
    {
        System.arraycopy(collection.toArray(), 0, objects, 0, objects.length);
    }

    /**
     * Add mappable to map
     *
     * @param <K>        the key
     * @param <V>        the value
     * @param maps the collection of Mappable that must implement the Copier
     *                   interface
     * @param aMap       the map to add to
     */
    public static <K, V> void addMappableCopiesToMap(Collection<Mappable<K, V>> maps, Map<K, V> aMap)
    {
        if (maps == null || aMap == null)
            return;

        Mappable<K, V> mappable;
        Copier previous;
        for (Mappable<K, V> aMappable : maps) {
            mappable = aMappable;

            previous = (Copier) aMap.get(mappable.getKey());

            if (previous != null) {
                // copy data
                previous.copy((Copier) mappable);
            } else {
                // add to map
                aMap.put(mappable.getKey(), mappable.getValue());
            }
        }
    }

    /**
     * Find values in map that match a given key
     *
     * @param <K>   the key type
     * @param <T>   the map value type
     * @param keys the keys
     * @param map  the map containing the data
     * @return Collection of values
     */
    public static <T, K> Collection<T> findMapValuesByKey(Collection<K> keys,
                                                          Map<K, T> map)
    {
        if (keys == null || map == null)
            return null;

        T value;

        ArrayList<T> results = new ArrayList<>(map.size());
        for (K key : keys) {

            value = map.get(key);

            if(value == null)
                continue;

            results.add(value);
        }

        results.trimToSize();

        return results;
    }

    /**
     * All object to a given collection
     *
     * @param <T>   the type class
     * @param aFrom the collection to add from
     * @param aTo   the collection to add to.
     */
    public static <T> void addAll(Collection<T> aFrom, Collection<T> aTo)
    {
        if (aFrom == null || aTo == null)
            return; // do nothing

        T object;
        for (T t : aFrom) {
            object = t;
            if (object != null) {
                aTo.add(object);
            }
        }
    }

    /**
     * @param aCollection the collection of objects
     * @return aCollection == null || aCollection.isEmpty()
     */
    public static boolean isEmpty(Collection<?> aCollection)
    {
        return aCollection == null || aCollection.isEmpty();
    }

    public static boolean isEmpty(Object[] objects)
    {
        return objects == null || objects.length == 0;
    }// --------------------------------------------

    /**
     * @param aInt      the search criteria
     * @param aIntegers the list of integers
     * @return true is aInts exist in aIntegers list
     */
    public static boolean isIntegerIn(Integer aInt, Integer[] aIntegers)
    {
        if (aIntegers == null || aInt == null)
            return false;

        for (Integer aInteger : aIntegers) {
            if (aInt.equals(aInteger))
                return true;
        }

        return false;
    }

    /**
     * construct map for collection of criteria object where the key is
     * Criteria.getId
     *
     * @param collection the list of criteria
     * @return the map Criteria is the value and Criteria.getId is the key
     */
    public static Map<String, Criteria> constructCriteriaMap(Collection<Criteria> collection)
    {
        if (collection == null)
            return null;

        Map<String, Criteria> map = new HashMap<>(collection.size());
        Criteria criteria;
        for (Criteria value : collection) {
            criteria = value;
            map.put(criteria.getId(), criteria);
        }
        return map;
    }

    /**
     * construct map for collection of criteria object where the key is
     * Criteria.getId
     *
     * @param aPrimaryKeys the primary keys
     * @return the map Criteria is the value and Criteria.getId is the key
     */
    public static Map<Integer, PrimaryKey> constructPrimaryKeyMap(Collection<PrimaryKey> aPrimaryKeys)
    {
        if (aPrimaryKeys == null)
            return null;

        Map<Integer, PrimaryKey> map = new HashMap<>(aPrimaryKeys.size());
        for (PrimaryKey primaryKey : aPrimaryKeys) {
            map.put(primaryKey.getPrimaryKey(), primaryKey);
        }
        return map;
    }

    /**
     * @param aName       the property name
     * @param properties the collection of properties
     * @return null if not found, else return matching property
     */
    public static Property findPropertyByName(String aName,
                                              Collection<Property> properties)
    {
        if (aName == null)
            throw new IllegalArgumentException(
                    "aName required in Organizer.findPropertyByName");

        if (properties == null)
            throw new IllegalArgumentException(
                    "aProperties required in Organizer.findPropertyByName");

        Property property;
        for (Property value : properties) {
            property = value;
            if (aName.equals(property.getName()))
                return property;
        }
        return null;
    }

    /**
     * Copy data from object to object
     *
     * @param <K>   the key
     * @param aFrom the object to copy from
     * @param aTo   the object to copy to
     */
    public static <K> void makeCopies(Map<K, Copier> aFrom, Map<K, Copier> aTo)
    {
        makeAuditableCopies(aFrom, aTo, null);
    }

    /**
     * Copy data from object to object
     *
     * @param aFrom the object to copy from
     * @param aTo   the object to copy to
     */
    @SuppressWarnings(
            {"unchecked", "rawtypes"})
    public static void makeCopies(Collection<Copier> aFrom, Collection<Copier> aTo)
    {
        if (aFrom == null || aTo == null)
            return;

        List<Copier> fromList = new ArrayList<>(aFrom);
        List<Copier> toList = new ArrayList<>(aTo);
        Collections.sort((List) fromList);
        Collections.sort((List) toList);

        Copier from;
        Copier to;
        Iterator<Copier> iterator = toList.iterator();
        for (Iterator<Copier> i = fromList.iterator(); i.hasNext() && iterator.hasNext(); ) {
            from = i.next();
            to = iterator.next();

            // copy data
            to.copy(from);
        }
    }

    /**
     * Copy value form one map to another
     *
     * @param aAuditable the auditable to copy
     * @param aFormMap   the input map of copiers
     * @param <K>        the key name
     * @param aToMap     the output map of copiers
     */
    public static <K> void makeAuditableCopies(Map<K, Copier> aFormMap, Map<K, Copier> aToMap,
                                               Auditable aAuditable)

    {
        if (aFormMap == null || aToMap == null)
            return;

        // for through from
        K fromKey;
        Copier to;
        Copier from;
        for (Map.Entry<K, Copier> entry : aFormMap.entrySet()) {
            fromKey = entry.getKey();

            if (aToMap.containsKey(fromKey)) {
                // copy existing data
                to = aToMap.get(fromKey);
                to.copy(entry.getValue());

                // copy auditing info
                if (aAuditable != null && to instanceof Auditable) {
                    AbstractAuditable.copy(aAuditable, (Auditable) to);
                }
            } else {
                from = aFormMap.get(fromKey);

                // copy auditing info
                if (aAuditable != null && from instanceof Auditable) {
                    AbstractAuditable.copy(aAuditable, (Auditable) from);
                }

                // add to
                aToMap.put(fromKey, from);
            }
        }
    }

    /**
     * Sort collection of object by a given property name
     *
     * @param propertyName the property name
     * @param aCollection  the collection of object to sort
     * @param <T>          the type class
     * @return the collection of sorted values
     */
    public static <T> Collection<T> sortByJavaBeanProperty(String propertyName,
                                                           Collection<T> aCollection)
    {
        return sortByJavaBeanProperty(propertyName, aCollection, false);
    }

    public static <K, V> Map<K, V> sortByValue(Map<K, V> map, BeanComparator beanComparator)
    {
        return map.entrySet()
                  .stream()
                  .sorted(Map.Entry.comparingByValue(beanComparator))
                  .collect(Collectors.toMap(
                          Map.Entry::getKey,
                          Map.Entry::getValue,
                          (e1, e2) -> e1,
                          LinkedHashMap::new));
    }

    /**
     * Sort collection of object by a given property name
     *
     * @param <T>          the type class name
     * @param propertyName the property name
     * @param descending  boolean if sorting descending or not
     * @param aCollection  the collection of object to sort
     * @return the collection of sorted collection of the property
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> sortByJavaBeanProperty(String propertyName,
                                                           Collection<T> aCollection, boolean descending)
    {
        if (aCollection == null)
            return new ArrayList<>();

        if (propertyName == null)
            throw new IllegalArgumentException(
                    "propertyName required in Organizer");

        BeanComparator bc = new BeanComparator(propertyName, descending);

        return (Collection<T>) bc.sort(aCollection);

    }

    /**
     * @param aPropertyName the property name
     * @param aCollection   the collection to construct set from (this object) must have
     *                      an javaBean property that matches aPropertyName
     * @param <T>           the type class
     * @return set of bean properties (HashSet)
     *
     */
    public static <T> Set<T> constructSortedSetForProperty(Collection<T> aCollection,
                                                           String aPropertyName)
    {
        if (aCollection == null || aCollection.isEmpty())
            return null;

        Set<T> set = new TreeSet<>();
        Object bean;
        for (T t : aCollection) {
            bean = t;
            set.add(JavaBean.getProperty(bean, aPropertyName));
        }

        return set;
    }

    /**
     * @param list        the list to filter
     * @param propertyName the property name to base the filters
     * @param aValue       the value to compare
     * @return the filtered list
     */
    public static Collection<Object> filterByJavaBeanProperty(
            List<Object> list, String propertyName, Comparable<Object> aValue)
    {

        logger.debug("In Organizer filtering: " + propertyName
                + " for value: " + aValue);
        try {
            if (list == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<>(list.size());

            Object bean;
            Object beanPropertyValue;
            for (Object object : list) {
                bean = object;
                beanPropertyValue = JavaBean.getProperty(bean, propertyName);
                logger.debug("Got propertyValue: " + beanPropertyValue
                        + " for propertyName: " + propertyName);
                if (aValue.compareTo(beanPropertyValue) == 0) {
                    // only add equal this bean
                    filteredList.add(bean);
                    logger.debug("Organizer added bean");
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * @param list            the list to filter
     * @param propertyName    the property name to filter
     * @param startComparable the formatted start date
     * @param endComparable   the formatted end date
     * @return the filter results
     */
    public static Collection<Object> filterByJavaBeanDateProperty(List<Object> list,
                                                                  String propertyName,
                                                                  Comparable<Object> startComparable,
                                                                  Comparable<Object> endComparable)
    {

        logger.debug("In Organizer filtering: " + propertyName
                + " for date value between : " + startComparable + " and " + endComparable);
        try {
            if (list == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<>(list.size());

            Object bean;
            Object beanPropertyValue;
            for (Iterator<Object> i = list.iterator(); i.hasNext(); ) {
                try {
                    bean = i.next();
                    beanPropertyValue = JavaBean.getProperty(bean,
                            propertyName);

                    DateFormat format = new SimpleDateFormat(
                            settings().getProperty("document.date.format"));
                    Date propDate = format.parse(beanPropertyValue.toString());
                    Date aDate = format.parse(startComparable.toString());
                    Date bDate = format.parse(endComparable.toString());

                    if (propDate.after(aDate) && propDate.before(bDate)) {
                        filteredList.add(bean);
                    }
                }
                catch (Exception e) {
                    logger.debug("error occurred : " + e);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public static Collection<Object> filterByJavaBeanPageProperty(ArrayList<Object> aList,
                                                                  String aPropertyName, int fromIndex, int toIndex)
    {

        logger.debug("In Organizer filtering: " + aPropertyName);
        try {
            if (aList == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<>(aList.size());

            Object bean;
            Object beanPropertyValue;
            for (Iterator<Object> i = aList.iterator(); i.hasNext(); ) {
                try {
                    bean = i.next();
                    beanPropertyValue = JavaBean.getProperty(bean,
                            aPropertyName);
                    int beanPropIntVal = Integer.parseInt(
                            beanPropertyValue.toString());

                    if ((fromIndex <= beanPropIntVal)
                                    && (beanPropIntVal <= toIndex)) {
                        filteredList.add(bean);
                    }
                }
                catch (Exception e) {
                    logger.debug("error occurred : " + e);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * @param numberedProperties the number properties
     * @return Map with (Integer)NumberedProperty.getNumber as the key
     */
    public static Map<Integer, NumberedProperty> constructNumberedPropertyMap(
            Collection<NumberedProperty> numberedProperties)
    {
        if (numberedProperties == null)
            throw new IllegalArgumentException(
                    "aNumberedProperties required in Organizer");

        Map<Integer, NumberedProperty> map = new HashMap<>(numberedProperties.size());
        NumberedProperty numberedProperty;
        for (NumberedProperty property : numberedProperties) {
            numberedProperty = property;
            map.put(numberedProperty.getNumber(), numberedProperty);
        }

        return map;
    }

    /**
     * @param properties collection of Property objects
     * @return Map with (Integer)Property.getName as the key
     */
    public static Map<String, Property> constructPropertyMap(Collection<Property> properties)
    {
        if (properties == null)
            throw new IllegalArgumentException(
                    "properties required in Organizer");

        Map<String, Property> map = new HashMap<>(properties.size());
        for (Property property : properties) {
            map.put(property.getName(), property);
        }

        return map;
    }

    /**
     * key=Mappable.getKey() value=Mappable.getValue()
     *
     * @param mapCollection the collection of mappable to convert
     * @param <K>        the key
     * @param <V>        the value
     * @return the mapped
     */
    public static <K, V> Map<K, V> toMap(Collection<Mappable<K, V>> mapCollection)
    {
        if (mapCollection == null)
            throw new IllegalArgumentException(
                    "mapCollection required in Organizer");

        Map<K, V> map = new HashMap<>(mapCollection.size());
        for (Mappable<K, V> mappable : mapCollection) {
            map.put(mappable.getKey(), mappable.getValue());
        }

        return map;
    }

    public static <K, V> Map<K, V> toMap(Mappable<K, V>[] maps)
    {
        if (maps == null)
            throw new IllegalArgumentException(
                    "maps required in Organizer");

        Map<K, V> map = new HashMap<>(maps.length);
        for (Mappable<K, V> mappable : maps) {
            map.put(mappable.getKey(), mappable.getValue());
        }

        return map;
    }

    /**
     * Cast into an array of objects or create an array with a single entry
     *
     * @param obj the Object[] or single object
     * @return converted Object[]
     */
    public static Object[] toArray(Object obj)
    {
        if (obj instanceof Object[])
            return (Object[]) obj;
        else {
            Object[] returnArray =
                    {obj};
            return returnArray;
        }
    }

    /**
     * @param objects the objects to convert
     * @return the array of the integers
     */
    public static Integer[] toIntegers(Object[] objects)
    {
        if (objects == null)
            throw new IllegalArgumentException(
                    "aObjects required in Organizer.toIntegers");

        if (objects.length < 1)
            throw new IllegalArgumentException("aObjects.length < 1 ");

        Integer[] ints = new Integer[objects.length];

        System.arraycopy(objects, 0, ints, 0, ints.length);
        return ints;

    }

    public static double[] toDoubles(List<Double> objects)
    {
        if (objects == null || objects.isEmpty())
            return null;

        double[] rets = new double[objects.size()];

        for (int i = 0; i < rets.length; i++) {
            rets[i] = objects.get(i);
        }
        return rets;

    }

    /**
     * @param <T>   the type for the list
     * @param count the number of copies
     * @param value the value to
     * @return the collection with that number of copies
     */
    public static <T> List<T> fill(int count, T value)
    {
        if (value == null || count < 1)
            return null;

        ArrayList<T> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(value);
        }

        return list;
    }

    /**
     * Get element by index
     *
     * @param <T>  the input type
     * @param i    the index
     * @param args the input argument
     * @return the object at the index
     */
    public static <T> T at(int i, T[] args)
    {
        if (args == null || i < 0)
            return null;

        if (i >= args.length)
            return null;

        return args[i];
    }

    /**
     *
     * @param params the params to convert
     * @param <K> the key
     * @param <V> the value
     * @return The map of the params key/value
     */
    public static <K, V> Map<K, V> toMap(Object... params)
    {
        if (params == null || params.length == 0)
            return null;

        Map<Object, Object> map = new HashMap<>();

        Object key = null;
        for (int i = 0; i < params.length; i++) {
            if (i % 2 == 0) {
                key = params[i];
            } else {
                map.put(key, params[i]);
            }
        }

        if(params.length %2 == 1)
            map.put(params[params.length -1],null);

        return (Map<K, V>) map;
    }

    public static <T> ArrayList<T> toArrayList(List<T> list)
    {
        if(list ==null || list.isEmpty())
            return null;

        ArrayList<T> arrayList = new ArrayList<>(list.size());
        arrayList.addAll(list);
        return arrayList;
    }

    public static <T> String[] toArrayString(Collection<T> collection)
    {
        if(collection ==null || collection.isEmpty())
            return null;

        String[]  outputs = new String[collection.size()];
        int i = 0;
        for (T obj: collection) {
            outputs[i] = Text.toString(obj);
            i++;
        }

        return outputs;
    }


    public static<T> Arranger organize(T... list) {
        return organizeList(Arrays.asList(list));
    }

    public static<T> Arranger organizeList(List<T> list) {
        return new Arranger(list);
    }
}
