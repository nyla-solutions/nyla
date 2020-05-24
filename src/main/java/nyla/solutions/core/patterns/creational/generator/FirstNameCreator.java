package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.Presenter;

/**
 * @author Gregory Green
 */
public class FirstNameCreator extends AbstractNameCreator
{

    @Override
    protected String getPresenterPropertyName()
    {
        return "firstNames";
    }
}
