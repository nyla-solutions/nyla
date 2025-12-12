package nyla.solutions.core.util.organizer;

import nyla.solutions.core.data.Property;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.util.JavaBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Gregory Green
 */
public class SearchOrganizer {

    /**
     * Find the value with a given key in the map.
     *
     * @param map          the map with key/value pairs
     * @param key          the map key
     * @param defaultValue this is returned if the value is now found
     * @return the single value found
     */
    public <T> T findValueByKeyWithDefault(Map<?, ?> map, Object key,
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
     *
     * @param collection the collection
     * @param text text to find
     * @return
     */
    public <T> T findByTextIgnoreCase(Collection<T> collection,
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
     * @param aInt      the search criteria
     * @param aIntegers the list of integers
     * @return true is aInts exist in aIntegers list
     */
    public boolean isIntegerIn(Integer aInt, Integer[] aIntegers)
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
     * @param text the text to search for
     * @param list the list of strings
     * @return true if aText in aList
     */
    public boolean isStringIn(String text, String... list)
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
     * @param objects the object to check
     * @param dataMap the data map
     * @return true if the object's property contains data in test Map
     */
    @SuppressWarnings("unchecked")
    public <K, V> boolean doesListContainData(Object[] objects, Map<K, V> dataMap)
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
    public boolean doesMapContainData(Map<Object, Object> aMap, Map<Object, Object> aData)
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

    /**
     * @param aName       the property name
     * @param properties the collection of properties
     * @return null if not found, else return matching property
     */
    public Property findPropertyByName(String aName,
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
     * Find values in map that match a given key
     *
     * @param <K>   the key type
     * @param <T>   the map value type
     * @param keys the keys
     * @param map  the map containing the data
     * @return Collection of values
     */
    public <T, K> Collection<T> findMapValuesByKey(Collection<K> keys,
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


}
