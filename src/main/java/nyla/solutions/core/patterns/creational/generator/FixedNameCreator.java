package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.patterns.creational.Creator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Uses provided names to provide a fixed number of responses.
 * @author Gregory Green
 */
public class FixedNameCreator implements Creator<String>
{
    private final String[] names;
    private AtomicInteger i = new AtomicInteger(0);

    public FixedNameCreator(String... names)
    {
        if(names == null || names.length == 0)
            throw new RequiredException("At least one name must be provided");

        this.names = names;
    }

    /**
     * @return one of the fixed provided names
     */
    @Override
    public String create()
    {
         int index = i.get();
         if(index >= names.length)
         {
             index = 0;
             i.set(index);
         }

         String results = names[index];
         i.set(++index);
        return results;
    }
}
