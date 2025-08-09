package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Digits;

/**
 * @author Gregory Green
 */
public class PhoneNumberCreator  implements CreatorTextable
{
    private final Digits digits = new Digits();
    @Override
    public String create()
    {
        return new StringBuilder()
                .append("555-01")
                .append(digits.generateInteger(0,9))
                .append("-")
                .append(digits.generateInteger(1000,9999))
                .toString();
    }
}
