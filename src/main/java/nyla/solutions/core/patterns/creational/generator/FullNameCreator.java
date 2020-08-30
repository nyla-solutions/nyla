package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;

/**
 * Aggregates first and last name
 * @author Gregory Green
 */
public class FullNameCreator implements Creator<String>
{
    private final FirstNameCreator firstNameCreator;
    private final LastNameCreator lastNameCreator;
    private final static char separator = ' ';

    public FullNameCreator()
    {
        firstNameCreator = new FirstNameCreator();
        lastNameCreator = new LastNameCreator();

    }

    @Override
    public String create()
    {
        return new StringBuilder(firstNameCreator.create()).append(separator)
                .append(lastNameCreator.create()).toString();
    }
}
