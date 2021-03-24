package nyla.solutions.core.patterns.expression;


/**
 * @author Gregory Green
 */
public class IsPhoneNumberExpression implements BooleanExpression<String>
{

    @Override
    public Boolean apply(String phoneNumber)
    {
        if(phoneNumber == null || phoneNumber.length() == 0)
            return false;

        return phoneNumber.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
    }
}
