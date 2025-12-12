package nyla.solutions.core.util.organizer;

import nyla.solutions.core.data.Copier;
import nyla.solutions.core.data.Mappable;

import java.util.Collection;
import java.util.Map;

/**
 * @author Gregory Green
 */
public class CopyOrganizer {


    /**
     * Add mappable to map
     *
     * @param <K>        the key
     * @param <V>        the value
     * @param maps the collection of Mappable that must implement the Copier
     *                   interface
     * @param aMap       the map to add to
     */
    public <K, V> void addMappableCopiesToMap(Collection<Mappable<K, V>> maps, Map<K, V> aMap)
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

}
