package nyla.solutions.core.patterns.creational.generator;

public class StateCreator extends AbstractNameCreator implements CreatorTextable{
    @Override
    protected String getPresenterPropertyName()
    {
        return "states";
    }
}
