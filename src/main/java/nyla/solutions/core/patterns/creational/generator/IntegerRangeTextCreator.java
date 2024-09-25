package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Digits;

import static nyla.solutions.core.util.Config.settings;

/**
 * @author Gregory Green
 */
public class IntegerRangeTextCreator implements CreatorTextable
{
    public static final String MIN_INT_TEXT_PROP = "INT_RANGE_TEXT_CREATOR_MIN";
    public static final String MAX_INT_TEXT_PROP = "INT_RANGE_TEXT_CREATOR_MAX";

    private final Digits digits = new Digits();
    private final int min;
    private final int max;

    public IntegerRangeTextCreator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntegerRangeTextCreator() {
        this(settings().getPropertyInteger(MIN_INT_TEXT_PROP),
                settings().getPropertyInteger(MAX_INT_TEXT_PROP));
    }

    public static IntegerRangeTextCreator range(int min, int max) {
        return new IntegerRangeTextCreator(min,max);
    }

    /**
     * @return the create object
     */
    @Override
    public String create()
    {

        return String.valueOf(digits.generateInteger(min,max));
    }
}
