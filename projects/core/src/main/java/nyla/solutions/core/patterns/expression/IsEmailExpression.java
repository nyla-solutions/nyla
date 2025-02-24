package nyla.solutions.core.patterns.expression;

/**
 * @author Gregory Green
 */
public class IsEmailExpression implements BooleanExpression<String>
{
    @Override
    public Boolean apply(String text)
    {
        if(text == null || text.length() == 0)
            return false;

        return text.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z0-9]+)$");
    }
}
