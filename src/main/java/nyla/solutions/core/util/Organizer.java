package nyla.solutions.core.util;

import nyla.solutions.core.data.*;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

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
 *  }//--------------------------------------------
 *
 *
 * </pre>
 *
 * @author Gregory Green
 * @version 2.0
 */
public final class Organizer
{

    private static Log logger = Debugger.getLog(Organizer.class);

    /**
     * Constructor for Organizer initializes internal
     */
    private Organizer()
    {
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
    }//------------------------------------------------

    /**
     * @param <T>   the type class
     * @param input the item to add
     * @param array the array where item is added
     * @return the update array
     */
    public static <T> T[] add(T input, T[] array)
    {
        ArrayList<T> list = new ArrayList<T>(array.length + 1);
        list.addAll(Arrays.asList(array));
        list.add(input);

        return (T[]) list.toArray(array);
    }// --------------------------------------------------------

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
     * @param <T>           the flatten output type
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

    }// --------------------------------------------------------

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that
     * @param <T>                the type class
     * @return the flatten collections into a single collection
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

    }// --------------------------------------------------------

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that need to be flatten
     * @param sorter             optional comparable for sorting
     * @param filter             optional filter, if filter.
     * @param <T>                the type class
     * @return the flatten collections into a single collection
     */
    public static <T> Paging<T> flattenPaging(Collection<Paging<T>> collectionOfPaging,
                                              Comparator<T> sorter, BooleanExpression<T> filter)
    {
        if (collectionOfPaging == null || collectionOfPaging.isEmpty())
            return null;

        PageCriteria pageCriteria = collectionOfPaging.iterator().next().getPageCriteria();
        return flattenPaging(collectionOfPaging, pageCriteria, sorter, filter);
    }// --------------------------------------------------------

    /**
     * Aggregates multiple collections into a single paging collection
     *
     * @param collectionOfPaging the collection paging that need to be flatten
     * @param sorter             optional comparable for sorting
     * @param filter             optional filter, if filter.
     * @param pageCriteria       the page criteria
     * @param <T>                the collection page object type
     * @return the flatten collections into a single collection
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
            TreeSet<T> treeSet = new TreeSet<T>(sorter);
            pagingResults = new PagingCollection<T>(treeSet, pageCriteria);
        }

        // Add all to an aggregated collection
        Paging<T> paging = null;
        for (Object item : collectionOfPaging) {
            if (item instanceof Paging) {
                paging = (Paging<T>) item;

                if (pagingResults != null)
                    addAll(pagingResults, paging, filter);
                else
                    pagingResults = paging;
            } else if (item != null) {
                // initialize paging results if needed
                if (pagingResults == null) {
                    if (sorter != null) {
                        // Create tree set based paging
                        TreeSet<T> treeSet = new TreeSet<T>(sorter);
                        pagingResults = new PagingCollection<T>(treeSet, pageCriteria);
                    } else
                        pagingResults = new PagingCollection<T>(new ArrayList<T>(), pageCriteria);

                }

                pagingResults.add((T) item);
            }
        }

        return pagingResults;
    }// --------------------------------------------------------

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
            for (T obj : paging) {
                pagingResults.add(obj);
            }
        }
    }// --------------------------------------------------------

    @SuppressWarnings(
            {"unchecked"})
    public static <T> List<T> toList(T... args)
    {
        if (args == null || args.length == 0)
            return null;

        ArrayList<T> list = new ArrayList<>(args.length);
        for (T t : args) {
            list.add(t);
        }

        return list;
    }//------------------------------------------------

    @SuppressWarnings(
            {"unchecked"})
    public static <T> Set<T> toSet(T... args)
    {
        if (args == null || args.length == 0)
            return null;

        HashSet<T> set = new HashSet<>(args.length);

        fill(set, args);
        return set;
    }//------------------------------------------------

    @SuppressWarnings("unchecked")
    public static <T> void fill(Collection<T> collection, T... args)
    {
        if (collection == null || args == null)
            return;

        for (T t : args) {
            collection.add(t);
        }
    }//------------------------------------------------

    @SuppressWarnings(
            {"unchecked", "rawtypes"})
    public static <T> List<Collection<T>> toPages(Collection<T> collection, int pageSize)
    {
        if (collection == null || collection.isEmpty())
            return null;

        int collectionSize = collection.size();

        if (pageSize <= 0 || collectionSize <= pageSize)
            return (List<Collection<T>>) Collections.singletonList(collection);

        int initialSize = collectionSize / pageSize;

        ArrayList<Collection<T>> list = new ArrayList(initialSize);

        ArrayList<Object> current = new ArrayList<Object>();
        for (Object object : collection) {
            current.add(object);

            if (current.size() >= pageSize) {
                current.trimToSize();

                list.add((Collection<T>) current);
                current = new ArrayList<Object>();
            }
        }

        if (!current.isEmpty())
            list.add((Collection<T>) current);

        return (List<Collection<T>>) list;

    }// ------------------------------------------------

    @SuppressWarnings(
            {"unchecked", "rawtypes"})
    public static <K, V> List<Collection<K>> toKeyPages(Collection<Map.Entry<K, V>> mapEntries, int pageSize)
    {
        if (mapEntries == null || mapEntries.isEmpty())
            return null;

        int collectionSize = mapEntries.size();

        if (pageSize <= 0 || collectionSize <= pageSize) {
            ArrayList<K> list = new ArrayList<K>(mapEntries.size());
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

        ArrayList<Collection<K>> list = new ArrayList(initialSize);

        ArrayList<K> current = new ArrayList<K>();
        for (Map.Entry<K, V> entry : mapEntries) {
            current.add(entry.getKey());

            if (current.size() >= pageSize) {
                current.trimToSize();

                list.add((Collection<K>) current);
                current = new ArrayList<K>();
            }
        }

        if (!current.isEmpty())
            list.add((Collection<K>) current);

        return (List<Collection<K>>) list;

    }// ------------------------------------------------

    /**
     * Find the value with a given key in the map.
     *
     * @param key          the map key
     * @param map          the map with key/value pairs
     * @param defaultValue this is returned if the value is now found
     * @return the single value found
     */
    public static Object findMapValueByKey(Object key, Map<?, ?> map,
                                           Object defaultValue)
    {
        if (key == null || map == null)
            return defaultValue;

        Object value = map.get(key);

        if (value == null)
            return defaultValue;

        return value;
    }// --------------------------------------------

    /**
     * @param text the text to search for
     * @param list the list of strings
     * @return true if aText in aList
     */
    public static boolean isStringIn(String text, String[] list)
    {
        if (text == null)
            return false;

        for (int i = 0; i < list.length; i++) {
            if (text.equals(list[i]))
                return true;
        }

        return false;
    }// --------------------------------------------

    public static Object findByTextIgnoreCase(Collection<?> aCollection,
                                              String aText)
    {
        if (aText == null)
            throw new RequiredException("aName in Organizer.findIgnoreCase");

        if (aCollection == null)
            throw new RequiredException(
                    "aCollection in Organizer.findIgnoreCase");

        Object element = null;
        for (Iterator<?> i = aCollection.iterator(); i.hasNext(); ) {
            element = i.next();

            if (element == null)
                continue;

            if (aText.equalsIgnoreCase(element.toString()))
                return element;
        }

        throw new SystemException("Text=" + aText + " in collection  "
                + aCollection);
    }// --------------------------------------------

    /**
     * Add object to a list
     *
     * @param list    where objects will be added
     * @param objects the object add
     */
    public static void addAll(Collection<Object> list, Object[] objects)
    {
        list.addAll(Arrays.asList(objects));
    }// --------------------------------------------

    /**
     * @param objects
     * @param aData
     * @return true if the object's property contains data in test Map
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    static <K, V> boolean doesListContainData(Object[] objects, Map<K, V> aData)
            throws Exception
    {
        if (objects == null || objects.length == 0)
            return false;

        Map<K, V> objectMap = null;
        for (int i = 0; i < objects.length; i++) {
            // get properties for first object
            objectMap = (Map<K, V>) JavaBean.toMap(objects[i]);

            if (doesMapContainData((Map<Object, Object>) objectMap, (Map<Object, Object>) aData))
                return true;
        }

        return false;

    }// --------------------------------------------

    /**
     * @param aMap  the map
     * @param aData the key/values to check
     * @return true if all data in aData exist in aMap
     */
    public static boolean doesMapContainData(Map<Object, Object> aMap, Map<Object, Object> aData)
    {
        // compare with testMap
        Object testMapKey = null;
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
    }// --------------------------------------------

    public static <T> T[] copy(Object[] objs)
    {
        if (objs == null)
            return null;

        Object[] results = new Object[objs.length];
        System.arraycopy(objs, 0, results, 0, results.length);

        return (T[]) results;
    }// --------------------------------------------------------

    /**
     * Copy collection objects to a given array
     *
     * @param collection the collection source
     * @param objects    array destination
     */
    public static void copyToArray(Collection<Object> collection, Object[] objects)
    {
        System.arraycopy(collection.toArray(), 0, objects, 0, objects.length);
    }// --------------------------------------------

    /**
     * Add mappable to map
     *
     * @param <K>        the key
     * @param <V>        the value
     * @param aMappables the collection of Mappables that must implement the Copier
     *                   interface
     * @param aMap       the map to add to
     */
    public static <K, V> void addMappableCopiesToMap(Collection<Mappable<K, V>> aMappables, Map<K, V> aMap)
    {
        if (aMappables == null || aMap == null)
            return;

        Mappable<K, V> mappable = null;
        Copier previous = null;
        for (Iterator<Mappable<K, V>> i = aMappables.iterator(); i.hasNext(); ) {
            mappable = i.next();

            previous = (Copier) aMap.get(mappable.getKey());

            if (previous != null) {
                // copy data
                previous.copy((Copier) mappable);
            } else {
                // add to map
                aMap.put((K) mappable.getKey(), (V) mappable.getValue());
            }
        }
    }// --------------------------------------------

    /**
     * Find values in map that match a given key
     *
     * @param <K>   the key type
     * @param <T>   the map value type
     * @param aKeys the keys
     * @param aMap  the map containing the data
     * @return Collection of values
     */
    public static <T, K> Collection<T> findMapValuesByKey(Collection<K> aKeys,
                                                          Map<K, T> aMap)
    {
        if (aKeys == null || aMap == null)
            return null;

        Object key = null;
        ArrayList<T> results = new ArrayList<T>(aMap.size());
        for (Iterator<K> i = aKeys.iterator(); i.hasNext(); ) {
            key = i.next();
            results.add(aMap.get(key));
        }

        results.trimToSize();

        return results;
    }// --------------------------------------------

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

        T object = null;
        for (Iterator<T> i = aFrom.iterator(); i.hasNext(); ) {
            object = i.next();
            if (object != null) {
                aTo.add(object);
            }
        }
    }// --------------------------------------------

    /**
     * @param aCollection the collection of objects
     * @return aCollection == null || aCollection.isEmpty()
     */
    public static boolean isEmpty(Collection<?> aCollection)
    {
        return aCollection == null || aCollection.isEmpty();
    }// --------------------------------------------

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

        for (int i = 0; i < aIntegers.length; i++) {
            if (aInt.equals(aIntegers[i]))
                return true;
        }

        return false;
    }// --------------------------------------------

    /**
     * construct map for collection of criteria object wher the key is
     * Criteria.getId
     *
     * @param aCriterias
     * @return the map Criteria is the value and Criteria.getId is the key
     */
    public static Map<String, Criteria> constructCriteriaMap(Collection<Criteria> aCriterias)
    {
        if (aCriterias == null)
            return null;

        Map<String, Criteria> map = new HashMap<String, Criteria>(aCriterias.size());
        Criteria criteria = null;
        for (Iterator<Criteria> i = aCriterias.iterator(); i.hasNext(); ) {
            criteria = (Criteria) i.next();
            map.put(criteria.getId(), criteria);
        }
        return map;
    }// --------------------------------------------

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

        Map<Integer, PrimaryKey> map = new HashMap<Integer, PrimaryKey>(aPrimaryKeys.size());
        PrimaryKey primaryKey = null;
        for (Iterator<PrimaryKey> i = aPrimaryKeys.iterator(); i.hasNext(); ) {
            primaryKey = (PrimaryKey) i.next();
            map.put(Integer.valueOf(primaryKey.getPrimaryKey()), primaryKey);
        }
        return map;
    }// --------------------------------------------

    /**
     * @param aName       the property name
     * @param aProperties
     * @return null if not found, else return matching propertu
     */
    public static Property findPropertyByName(String aName,
                                              Collection<Property> aProperties)
    {
        if (aName == null)
            throw new IllegalArgumentException(
                    "aName required in Organizer.findPropertyByName");

        if (aProperties == null)
            throw new IllegalArgumentException(
                    "aProperties required in Organizer.findPropertyByName");

        Property property = null;
        for (Iterator<Property> i = aProperties.iterator(); i.hasNext(); ) {
            property = i.next();
            if (aName.equals(property.getName()))
                return property;
        }
        return null;
    }// --------------------------------------------
    // makeAuditableCopies

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
    }// --------------------------------------------

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

        List<Copier> fromList = new ArrayList<Copier>(aFrom);
        List<Copier> toList = new ArrayList<Copier>(aTo);
        Collections.sort((List) fromList);
        Collections.sort((List) toList);

        Copier from = null;
        Copier to = null;
        Iterator<Copier> toIter = toList.iterator();
        for (Iterator<Copier> i = fromList.iterator(); i.hasNext() && toIter.hasNext(); ) {
            from = (Copier) i.next();
            to = (Copier) toIter.next();

            // copy data
            to.copy(from);
        }
    }// --------------------------------------------

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

        // for thru froms
        K fromKey = null;
        Copier to = null;
        Copier from = null;
        for (Map.Entry<K, Copier> entry : aFormMap.entrySet()) {
            fromKey = entry.getKey();

            if (aToMap.keySet().contains(fromKey)) {
                // copy existening data
                to = (Copier) aToMap.get(fromKey);
                to.copy((Copier) entry.getValue());

                // copy auditing info
                if (aAuditable != null && to instanceof Auditable) {
                    AbstractAuditable.copy(aAuditable, (Auditable) to);
                }
            } else {
                from = (Copier) aFormMap.get(fromKey);

                // copy auditing info
                if (aAuditable != null && from instanceof Auditable) {
                    AbstractAuditable.copy(aAuditable, (Auditable) from);
                }

                // add to
                aToMap.put(fromKey, from);
            }
        }
    }// --------------------------------------------

    /**
     * Sort collection of object by a given property name
     *
     * @param aProperyName the property name
     * @param aCollection  the collection of object to sort
     * @param <T>          the type class
     * @return the collection of sorted values
     */
    public static <T> Collection<T> sortByJavaBeanProperty(String aProperyName,
                                                           Collection<T> aCollection)
    {
        return sortByJavaBeanProperty(aProperyName, aCollection, false);
    }// --------------------------------------------

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
     * @param aProperyName the property name
     * @param aDescending  boolean if sorting descending or not
     * @param aCollection  the collection of object to sort
     * @return the collection of sorted collection of the property
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> sortByJavaBeanProperty(String aProperyName,
                                                           Collection<T> aCollection, boolean aDescending)
    {
        if (aCollection == null)
            return (Collection<T>) new ArrayList<T>();

        if (aProperyName == null)
            throw new IllegalArgumentException(
                    "aProperyName required in Organizer");

        BeanComparator bc = new BeanComparator(aProperyName, aDescending);

        return (Collection<T>) bc.sort(aCollection);

    }// --------------------------------------------

    /**
     * @param aPropertyName the property name
     * @param aCollection   the collection to construct set from (this object) must have
     *                      an javaBean property that matches aPropertyName
     * @param <T>           the type class
     * @return set of bean properties (HashSet)
     *
     * @throws Exception when an unknown error occurs
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> constructSortedSetForProperty(Collection<T> aCollection,
                                                           String aPropertyName)
            throws Exception
    {
        if (aCollection == null || aCollection.isEmpty())
            return null;

        Set<T> set = new TreeSet<T>();
        Object bean = null;
        for (Iterator<T> i = aCollection.iterator(); i.hasNext(); ) {
            bean = (Object) i.next();
            set.add((T) JavaBean.getProperty(bean, aPropertyName));
        }

        return set;
    }// --------------------------------------------

    /**
     * @param aList        the list to filter
     * @param propertyName the property name to based the filters
     * @param aValue       the value to compare
     * @return the filtered list
     */
    public static Collection<Object> filterByJavaBeanProperty(
            List<Object> aList, String propertyName, Comparable<Object> aValue)
    {

        logger.debug("In Organizer filtering: " + propertyName
                + " for value: " + aValue);
        try {
            if (aList == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<Object>(aList.size());

            Object bean = null;
            Object beanPropertyValue = null;
            for (Iterator<Object> i = aList.iterator(); i.hasNext(); ) {
                bean = i.next();
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
    }// -----------------------------------------

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

            ArrayList<Object> filteredList = new ArrayList<Object>(list.size());

            Object bean = null;
            Object beanPropertyValue = null;
            for (Iterator<Object> i = list.iterator(); i.hasNext(); ) {
                try {
                    bean = i.next();
                    beanPropertyValue = JavaBean.getProperty(bean,
                            propertyName);
                    // logger.debug("Got propertyValue: " + beanPropertyValue+
                    // " for propertyName: " + aPropertyName);

                    // DateFormat localFormat = DateFormat.getDateInstance();
                    DateFormat format = new SimpleDateFormat(
                            Config.getProperty("document.date.format"));
                    Date propDate = format.parse(beanPropertyValue.toString());
                    Date aDate = format.parse(startComparable.toString());
                    Date bDate = format.parse(endComparable.toString());

                    if (propDate.after(aDate) && propDate.before(bDate)) {
                        filteredList.add(bean);
                    }
                }
                catch (Exception e) {
                    logger.debug("error occured : " + e);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }// -----------------------------------------

    public static Collection<Object> filterByJavaBeanPageProperty(ArrayList<Object> aList,
                                                                  String aPropertyName, int fromIndex, int toIndex)
    {

        logger.debug("In Organizer filtering: " + aPropertyName);
        try {
            if (aList == null)
                throw new IllegalArgumentException(
                        "aCollection required in filterByJavaBeanProperty");

            ArrayList<Object> filteredList = new ArrayList<Object>(aList.size());

            Object bean = null;
            Object beanPropertyValue = null;
            for (Iterator<Object> i = aList.iterator(); i.hasNext(); ) {
                try {
                    bean = i.next();
                    beanPropertyValue = JavaBean.getProperty(bean,
                            aPropertyName);
                    int beanPropIntVal = Integer.parseInt(
                            beanPropertyValue.toString());
                    // logger.debug("Got propertyValue: " + beanPropertyValue +
                    // " for propertyName: " + beanPropIntVal);
                    if (
                            (fromIndex <= beanPropIntVal)
                                    && (beanPropIntVal <= toIndex)) {
                        filteredList.add(bean);
                        // logger.debug("Organizer added bean");
                    }
                }
                catch (Exception e) {
                    logger.debug("error occured : " + e);
                }
            }
            filteredList.trimToSize();
            return filteredList;
        }
        catch (Exception e) {
            throw new SystemException(e);
        }
    }// -----------------------------------------

    /**
     * @param aNumberedProperties
     * @return Map with (Integer)NumberedProperty.getNumber as the key
     */
    public static Map<Integer, NumberedProperty> constructNumberedPropertyMap(
            Collection<NumberedProperty> aNumberedProperties)
    {
        if (aNumberedProperties == null)
            throw new IllegalArgumentException(
                    "aNumberedProperties required in Organizer");

        Map<Integer, NumberedProperty> map = new HashMap<Integer, NumberedProperty>(aNumberedProperties.size());
        NumberedProperty numberedProperty = null;
        for (Iterator<NumberedProperty> i = aNumberedProperties.iterator(); i.hasNext(); ) {
            numberedProperty = (NumberedProperty) i.next();
            map.put(Integer.valueOf(numberedProperty.getNumber()), numberedProperty);
        }

        return map;
    }// --------------------------------------------

    /**
     * @param aProperties collection of Property objects
     * @return Map with (Integer)Property.getName as the key
     */
    public static Map<String, Property> constructPropertyMap(Collection<Property> aProperties)
    {
        if (aProperties == null)
            throw new IllegalArgumentException(
                    "aProperties required in Organizer");

        Map<String, Property> map = new HashMap<String, Property>(aProperties.size());
        Property property = null;
        for (Iterator<Property> i = aProperties.iterator(); i.hasNext(); ) {
            property = (Property) i.next();
            map.put(property.getName(), property);
        }

        return map;
    }// --------------------------------------------

    /**
     * key=Mappable.getKey() value=Mappable.getValue()
     *
     * @param aMappables the collection of mappable to convert
     * @param <K>        the key
     * @param <V>        the value
     * @return the mapped
     */
    public static <K, V> Map<K, V> toMap(Collection<Mappable<K, V>> aMappables)
    {
        if (aMappables == null)
            throw new IllegalArgumentException(
                    "aMappables required in Organizer");

        Map<K, V> map = new HashMap<K, V>(aMappables.size());
        Mappable<K, V> mappable = null;
        for (Iterator<Mappable<K, V>> i = aMappables.iterator(); i.hasNext(); ) {
            mappable = i.next();
            map.put((K) mappable.getKey(), (V) mappable.getValue());
        }

        return map;
    }// --------------------------------------------

    public static <K, V> Map<K, V> toMap(Mappable<K, V>[] aMappables)
    {
        if (aMappables == null)
            throw new IllegalArgumentException(
                    "aMappables required in Organizer");

        Map<K, V> map = new HashMap<K, V>(aMappables.length);
        Mappable<K, V> mappable = null;
        for (int i = 0; i < aMappables.length; i++) {
            mappable = aMappables[i];
            map.put((K) mappable.getKey(), (V) mappable.getValue());
        }

        return map;
    }// --------------------------------------------

    /**
     * Cast into an array of objects or create a array with a single entry
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
    }// ------------------------------------------------

    /**
     * @param aObjects
     * @return the array of the integers
     */
    public static Integer[] toIntegers(Object[] aObjects)
    {
        if (aObjects == null)
            throw new IllegalArgumentException(
                    "aObjects required in Organizer.toIntegers");

        if (aObjects.length < 1)
            throw new IllegalArgumentException("aObjects.length < 1 ");

        Integer[] ints = new Integer[aObjects.length];

        System.arraycopy(aObjects, 0, ints, 0, ints.length);
        return ints;

    }// --------------------------------------------

    public static double[] toDoubles(List<Double> objects)
    {
        if (objects == null || objects.isEmpty())
            return null;

        double[] rets = new double[objects.size()];

        for (int i = 0; i < rets.length; i++) {
            rets[i] = objects.get(i);
        }
        return rets;

    }// --------------------------------------------

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

        ArrayList<T> list = new ArrayList<T>(count);
        for (int i = 0; i < count; i++) {
            list.add(value);
        }

        return list;
    }//------------------------------------------------


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
        for (T obj : list)
        {
            arrayList.add(obj);
        }
        return arrayList;
    }

    public static <T> String[] toArrayString(Collection<T> collection)
    {
        if(collection ==null || collection.isEmpty())
            return null;

        String[]  outut = new String[collection.size()];
        int i = 0;
        for (T obj: collection) {
            outut[i] = Text.toString(obj);
            i++;
        }

        return outut;
    }

    public static<T> Queue<T> toQueue(T... args)
    {
        if(args == null || args.length == 0)
            return null;

        LinkedBlockingQueue<T> queue = new LinkedBlockingQueue(args.length);
        queue.addAll(Arrays.asList(args));
        return queue;
    }

    public static <T> T getByIndex(List<T> list, int index) {
            if(list == null)
                return null;

            if(index>= list.size() )
                return null;

            return list.get(index);
    }
}
