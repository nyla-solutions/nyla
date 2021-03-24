package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Digits;

/**
 * @author Gregory Green
 */
public class PhoneNumberCreator implements Creator<String>
{
    private Digits digits = new Digits();
    @Override
    public String create()
    {
        return new StringBuilder()
                .append(digits.generateInteger(200,999))
                .append("-")
                .append(digits.generateInteger(200,999))
                .append("-")
                .append(digits.generateInteger(1000,9999))
                .toString();
    }
}
