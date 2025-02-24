package nyla.solutions.core.patterns.expression;

/**
 * @author Gregory Green
 */
public class NotExpression<T> implements BooleanExpression<T>
{
    private final BooleanExpression<T> booleanExpression;

    public NotExpression(BooleanExpression<T> booleanExpression)
    {
        this.booleanExpression = booleanExpression;
    }

    /**
     * Apply a not rule
     * @param value the input
     * @return !this.booleanExpression.apply(value)
     */
    @Override
    public Boolean apply(T value)
    {
        return !this.booleanExpression.apply(value);
    }
}
