package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.Presenter;

/**
 * @author Gregory Green
 */
public abstract class AbstractNameCreator implements Creator<String>
{
    private final Presenter presenter;
    private final String separator = ", *";
    public AbstractNameCreator()
    {
        presenter = Presenter.getPresenter(getClass());
    }


    @Override
    public String create()
    {

        String[] texts = presenter.getTexts(getPresenterPropertyName(), separator);

        int index = new Digits().generateInteger(0,texts.length-1);

        return texts[index];
    }

    protected abstract String getPresenterPropertyName();
}
