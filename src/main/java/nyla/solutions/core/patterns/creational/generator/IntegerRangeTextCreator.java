package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.Digits;

/**
 * @author Gregory Green
 */
public class IntegerRangeTextCreator implements CreatorTextable
{
    public static final String MIN_INT_TEXT_PROP = "INT_RANGE_TEXT_CREATOR_MIN";
    public static final String MAX_INT_TEXT_PROP = "INT_RANGE_TEXT_CREATOR_MAX";

    private final Digits digits = new Digits();

    /**
     * @return the create object
     */
    @Override
    public String create()
    {
        int min = Config.getPropertyInteger(MIN_INT_TEXT_PROP);
        int max = Config.getPropertyInteger(MAX_INT_TEXT_PROP);
        return String.valueOf(digits.generateInteger(min,max));
    }
}
