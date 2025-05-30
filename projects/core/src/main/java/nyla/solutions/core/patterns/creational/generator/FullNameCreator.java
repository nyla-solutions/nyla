package nyla.solutions.core.patterns.creational.generator;

/**
 * Aggregates first and last name
 * @author Gregory Green
 */
public class FullNameCreator   implements CreatorTextable
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
