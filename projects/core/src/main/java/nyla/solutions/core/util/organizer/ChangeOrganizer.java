package nyla.solutions.core.util.organizer;

import nyla.solutions.core.data.*;
import nyla.solutions.core.util.Text;

import java.util.*;

/**
 * @author Gregory Green
 */
public class ChangeOrganizer {

    /**
     *
     * @param params the params to convert
     * @param <K> the key
     * @param <V> the value
     * @return The map of the params key/value
     */
    public <K, V> Map<K, V> toMap(Object... params)
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

    public <T> ArrayList<T> toArrayList(List<T> list)
    {
        if(list ==null || list.isEmpty())
            return null;

        ArrayList<T> arrayList = new ArrayList<>(list.size());
        arrayList.addAll(list);
        return arrayList;
    }

    public <T> String[] toArrayString(Collection<T> collection)
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


    /**
     * @param numberedProperties the number properties
     * @return Map with (Integer)NumberedProperty.getNumber as the key
     */
    public Map<Integer, NumberedProperty> constructNumberedPropertyMap(
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
    public Map<String, Property> constructPropertyMap(Collection<Property> properties)
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
    public <K, V> Map<K, V> toMap(Collection<Mappable<K, V>> mapCollection)
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

    public <K, V> Map<K, V> toMap(Mappable<K, V>[] maps)
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
    public Object[] toArray(Object obj)
    {
        if (obj instanceof Object[])
            return (Object[]) obj;
        else {
            Object[] returnArray =
                    {obj};
            return returnArray;
        }
    }


    @SuppressWarnings(
            {"unchecked"})
    public <T> List<T> toList(T... args)
    {
        if (args == null || args.length == 0)
            return null;

        ArrayList<T> list = new ArrayList<>(args.length);
        Collections.addAll(list, args);

        return list;
    }

    @SuppressWarnings("unchecked")
    public <T> void fill(Collection<T> collection, T... args)
    {
        if (collection == null || args == null)
            return;

        Collections.addAll(collection, args);
    }

    @SuppressWarnings(
            {"unchecked"})
    public <T> Set<T> toSet(T... args)
    {
        if (args == null || args.length == 0)
            return null;

        HashSet<T> set = new HashSet<>(args.length);

        fill(set, args);
        return set;
    }

    /**
     * @param objects the objects to convert
     * @return the array of the integers
     */
    public Integer[] toIntegers(Object[] objects)
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

    public double[] toDoubles(List<Double> objects)
    {
        if (objects == null || objects.isEmpty())
            return null;

        double[] rets = new double[objects.size()];

        for (int i = 0; i < rets.length; i++) {
            rets[i] = objects.get(i);
        }
        return rets;

    }



    public <T> List<Collection<T>> toPages(Collection<T> collection, int pageSize)
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
    public <K, V> List<Collection<K>> toKeyPages(Collection<Map.Entry<K, V>> mapEntries, int pageSize)
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
     * Copy data from object to object
     *
     * @param <K>   the key
     * @param aFrom the object to copy from
     * @param aTo   the object to copy to
     */
    public <K> void makeCopies(Map<K, Copier> aFrom, Map<K, Copier> aTo)
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
    public void makeCopies(Collection<Copier> aFrom, Collection<Copier> aTo)
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
     * construct map for collection of criteria object where the key is
     * Criteria.getId
     *
     * @param collection the list of criteria
     * @return the map Criteria is the value and Criteria.getId is the key
     */
    public Map<String, Criteria> constructCriteriaMap(Collection<Criteria> collection)
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
    public Map<Integer, PrimaryKey> constructPrimaryKeyMap(Collection<PrimaryKey> aPrimaryKeys)
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
     * Copy value form one map to another
     *
     * @param aAuditable the auditable to copy
     * @param aFormMap   the input map of copiers
     * @param <K>        the key name
     * @param aToMap     the output map of copiers
     */
    public <K> void makeAuditableCopies(Map<K, Copier> aFormMap, Map<K, Copier> aToMap,
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
}
