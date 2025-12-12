package nyla.solutions.core.data.collections;

import nyla.solutions.core.exception.CapacityException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CapacityQueue<T> implements Queue<T>{

    private final Queue<T> queue;
    private final int maxLength;

    public CapacityQueue(Queue<T> queue, int maxLength) {
        this.queue = queue;
        this.maxLength = maxLength;
    }

    @Override
    public boolean add(T t) {
        if(size() >= maxLength)
            return false;

        return queue.add(t);
    }

    @Override
    public boolean offer(T t) {
        if(size() >= maxLength)
            return false;

        return queue.offer(t);
    }

    @Override
    public T remove() {
        return queue.remove();
    }

    @Override
    public T poll() {
        return queue.poll();
    }

    @Override
    public T element() {
        return queue.element();
    }

    @Override
    public T peek() {
        return queue.peek();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return queue.toArray(a);
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return queue.toArray(generator);
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return queue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return queue.removeAll(c);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return queue.removeIf(filter);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean equals(Object o) {
        return queue.equals(o);
    }

    @Override
    public int hashCode() {
        return queue.hashCode();
    }

    @Override
    public Spliterator<T> spliterator() {
        return queue.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return queue.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return queue.parallelStream();
    }

}
