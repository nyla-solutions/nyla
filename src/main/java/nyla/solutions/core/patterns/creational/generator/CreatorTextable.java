package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.patterns.creational.Creator;

/**
 * @author Gregory Green
 */
public interface CreatorTextable extends Creator<String>, Textable
{
    default String getText()
    {
        return create();
    }
}
