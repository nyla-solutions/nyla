package nyla.solutions.core.patterns.expression;

/**
 * Generic expression for comparing the Comparable objects
 * @author Gregory Green
 */
public class ComparableExpression<T extends Comparable>  implements BooleanExpression<T>
{
    private static final int GREATER_THAN_COMPARE_TO = 1;
    private static final int LESS_THAN_COMPARE_TO = -1;
    private static final int EQUALS_TO_COMPARE_TO = 0;

    private final T value;
    private final int expectedCompareTo;

    /**
     *
     * @param value the value to compare
     * @param expectedCompareTo the expected compare to results
     */
    public ComparableExpression(T value,int expectedCompareTo)
    {
        this.value = value;
        this.expectedCompareTo = expectedCompareTo;
    }

    /**
     * Factory method for greater than
     * @param value the value to compare
     * @param <T> the input type
     * @return  ComparableExpression
     */
    public static <T extends Comparable> ComparableExpression<T> greaterThan(T value)
    {
        return new ComparableExpression(value,GREATER_THAN_COMPARE_TO);
    }

    /**
     * Factory method for less than
     * @param value the value to compare
     * @param <T> the input type
     * @return  ComparableExpression
     */
    public static <T extends Comparable> ComparableExpression<T> lessThan(T value)
    {
        return new ComparableExpression(value,LESS_THAN_COMPARE_TO);
    }

    /**
     * Factory method for equals
     * @param value the value to compare
     * @param <T> the input type
     * @return  ComparableExpression
     */
    public static <T extends Comparable> ComparableExpression<T> equalsTo(T value)
    {
        return new ComparableExpression(value,EQUALS_TO_COMPARE_TO);
    }

    /**
     * Execute the rule to compare To
     * @param t the input value to compare
     * @return true if expected value compareTo matches for the input value
     */
    @Override
    public Boolean apply(T t)
    {
        return expectedCompareTo == t.compareTo(value);
    }
}
