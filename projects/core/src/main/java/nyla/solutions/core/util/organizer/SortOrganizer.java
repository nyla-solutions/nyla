package nyla.solutions.core.util.organizer;

import nyla.solutions.core.util.BeanComparator;
import nyla.solutions.core.util.JavaBean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Gregory Green
 */
public class SortOrganizer {


    /**
     * Sort collection of object by a given property name
     *
     * @param propertyName the property name
     * @param aCollection  the collection of object to sort
     * @param <T>          the type class
     * @return the collection of sorted values
     */
    public <T> Collection<T> sortByJavaBeanProperty(String propertyName,
                                                           Collection<T> aCollection)
    {
        return sortByJavaBeanProperty(propertyName, aCollection, false);
    }

    public <K, V> Map<K, V> sortByValue(Map<K, V> map, BeanComparator beanComparator)
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
     * @param collection  the collection of object to sort
     * @return the collection of sorted collection of the property
     */
    @SuppressWarnings("unchecked")
    public <T> Collection<T> sortByJavaBeanProperty(String propertyName,
                                                           Collection<T> collection, boolean descending)
    {
        if (collection == null)
            return new ArrayList<>();

        if (propertyName == null)
            throw new IllegalArgumentException(
                    "propertyName required in Organizer");

        BeanComparator bc = new BeanComparator(propertyName, descending);

        return (Collection<T>) bc.sort(collection);

    }

    /**
     * @param aPropertyName the property name
     * @param aCollection   the collection to construct set from (this object) must have
     *                      an javaBean property that matches aPropertyName
     * @param <T>           the type class
     * @return set of bean properties (HashSet)
     *
     */
    public <T> Set<T> constructSortedSetForProperty(Collection<T> aCollection,
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
}
