package nyla.solutions.core.patterns.creational.generator;

/**
 * @author Gregory Green
 */
public class LastNameCreator extends AbstractNameCreator  implements CreatorTextable
{

    @Override
    protected String getPresenterPropertyName()
    {
        return "lastNames";
    }
}
