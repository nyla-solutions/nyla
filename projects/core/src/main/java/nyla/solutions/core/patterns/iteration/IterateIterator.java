package nyla.solutions.core.patterns.iteration;

import java.util.Iterator;

public class IterateIterator<T> implements Iterate<T>
{
    private final Iterator<T> iterator;

    public IterateIterator(Iterator<T> iterator)
    {
        this.iterator = iterator;
    }

    public T next()
    {
        if(iterator == null || !iterator.hasNext())
            return null;

        return iterator.next();
    }
}
