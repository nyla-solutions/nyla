package nyla.solutions.core.patterns.creational.generator;

public class CityCreator extends AbstractNameCreator implements CreatorTextable{
    @Override
    protected String getPresenterPropertyName()
    {
        return "cities";
    }
}
