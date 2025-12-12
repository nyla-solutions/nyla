package nyla.solutions.core.data.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * List that has max capacity
 * @author Gregory Green
 */
public class CapacityList<T> extends ArrayList<T> {
    private final int maxCapacity;
    private int lastIndex = -1;

    /**
     * Constructor for CapacityList
     * @param capacity the max capacity
     */
    public CapacityList(int capacity)
    {
        super(capacity);
        this.maxCapacity = capacity;
    }

    @Override
    public boolean add(T t) {

        if (this.size() >= maxCapacity) {
            if(lastIndex <= 0)
                lastIndex = size();

            // remove oldest element to make room
            this.remove(--lastIndex);
        }

        return super.add(t);
    }

    /**
     * Adds all items in the collection
     * @param collection the collection to add
     * @return true if all items were added
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean foundOneNotAdded = false;
        for (T t : collection) {
            if(!this.add(t))
                foundOneNotAdded = true;
        }

        return !foundOneNotAdded;
    }

    /**
     * Adds all items in the collection at the index
     * @param index the index
     * @param collection the collection
     * @return true if all items were added
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        AtomicInteger count = new AtomicInteger();
        if(collection.size() > maxCapacity)
            collection = collection.stream()
                    .limit(maxCapacity)
                    .collect(Collectors.toSet());

        return super.addAll(index, collection);
    }
}
