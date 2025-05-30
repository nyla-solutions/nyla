package nyla.solutions.core.patterns.expression;

/**
 * @author Gregory Green
 */
public class AndExpression<T> implements BooleanExpression<T>
{
    private final BooleanExpression<T>[] expressions;

    public AndExpression(BooleanExpression<T>... expressions)
    {
        this.expressions = expressions;
    }

    @Override
    public Boolean apply(T value)
    {
        for (BooleanExpression<T> exp:expressions) {

            if(!exp.apply(value))
                return false;
        }

        return true;
    }


}
