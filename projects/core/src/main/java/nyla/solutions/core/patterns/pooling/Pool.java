package nyla.solutions.core.patterns.pooling;

import nyla.solutions.core.patterns.creational.Creator;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Generic implementation of a resource pool
 * @param <T> the pool object
 */
public class Pool<T> {
    private final int size;
    private final Queue<T> queue;
    private final Creator<T> creator;

    public Pool(int size,  Creator<T> creator) {
        this.size = size;
        this.queue = new ConcurrentLinkedQueue<>();;
        this.creator = creator;
        
        constructPool();
    }

    private synchronized void constructPool() {
        while (queue.size() < size) {
            queue.add(creator.create());
        }
    }

    public synchronized T acquire() {
        T item = null;
        if (!queue.isEmpty()) {
            item = queue.poll();
        }
        return item;
    }

    public synchronized void release(T item) {
        queue.add(item);
    }
}
