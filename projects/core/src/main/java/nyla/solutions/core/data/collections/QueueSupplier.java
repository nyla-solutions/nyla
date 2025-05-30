package nyla.solutions.core.data.collections;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Gregory Green
 */
public class QueueSupplier <T> implements  Supplier<T>, Queue<T>
{
    private final Queue<T> queue;

    public QueueSupplier(Queue<T> queue)
    {
        if(queue == null)
            throw new NullPointerException("Provided queue is null");
        this.queue = queue;
    }

    public QueueSupplier()
    {
        this(new ConcurrentLinkedQueue<>());
    }

    //@Override
    public boolean add(T t)
    {
        return queue.add(t);
    }

    //@Override
    public boolean offer(T t)
    {
        return queue.offer(t);
    }

    //@Override
    public T remove()
    {
        return queue.remove();
    }

    //@Override
    public T poll()
    {
        return queue.poll();
    }

    //@Override
    public T element()
    {
        return queue.element();
    }

    //@Override
    public T peek()
    {
        return queue.peek();
    }

    //@Override
    public int size()
    {
        return queue.size();
    }

    //@Override
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    //@Override
    public boolean contains(Object o)
    {
        return queue.contains(o);
    }

    //@Override
    public Iterator<T> iterator()
    {
        return queue.iterator();
    }

    //@Override
    public Object[] toArray()
    {
        return queue.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a)
    {
        return queue.toArray(a);
    }

    //@Override
    public boolean remove(Object o)
    {
        return queue.remove(o);
    }

    //@Override
    public boolean containsAll(Collection<?> c)
    {
        return queue.containsAll(c);
    }

    //@Override
    public boolean addAll(Collection<? extends T> c)
    {
        return queue.addAll(c);
    }

    //@Override
    public boolean removeAll(Collection<?> c)
    {
        return queue.removeAll(c);
    }

    //@Override
    public boolean removeIf(Predicate<? super T> filter)
    {
        return queue.removeIf(filter);
    }

    //@Override
    public boolean retainAll(Collection<?> c)
    {
        return queue.retainAll(c);
    }

    //@Override
    public void clear()
    {
        queue.clear();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueueSupplier<?> that = (QueueSupplier<?>) o;
        return Objects.equals(queue, that.queue);
    }

    @Override
    public int hashCode()
    {
        return queue.hashCode();
    }

    //@Override
    public Spliterator<T> spliterator()
    {
        return queue.spliterator();
    }

    //@Override
    public Stream<T> stream()
    {
        return queue.stream();
    }

    //@Override
    public Stream<T> parallelStream()
    {
        return queue.parallelStream();
    }

    //@Override
    public void forEach(Consumer<? super T> action)
    {
        queue.forEach(action);
    }

    /**
     * Supplier implementation
     * @return
     */
    //@Override
    public T get()
    {
        return queue.poll();
    }
}
