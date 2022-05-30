package nyla.solutions.core.patterns.creational.generator;

/**
 * @author Gregory Green
 */
public class FirstNameCreator extends AbstractNameCreator implements CreatorTextable
{

    @Override
    protected String getPresenterPropertyName()
    {
        return "firstNames";
    }
}
