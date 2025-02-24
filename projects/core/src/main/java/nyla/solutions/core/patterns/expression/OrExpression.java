package nyla.solutions.core.patterns.expression;

/**
 * @author Gregory Green
 */
public class OrExpression<T> implements BooleanExpression<T>
{
    private final BooleanExpression<T>[] expressions;

    public OrExpression(BooleanExpression<T>... expressions)
    {
        this.expressions = expressions;
    }

    @Override
    public Boolean apply(T value)
    {
        for (BooleanExpression<T> exp:expressions) {

            if(exp.apply(value))
                return true;
        }

        return false;
    }
}
