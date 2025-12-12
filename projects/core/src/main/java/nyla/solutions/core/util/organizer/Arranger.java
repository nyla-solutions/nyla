package nyla.solutions.core.util.organizer;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Gregory Green
 */
public class Arranger {
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
