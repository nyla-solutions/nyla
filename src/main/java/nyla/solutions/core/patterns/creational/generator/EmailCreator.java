package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.Text;

/**
 * @author Gregory Green
 */
public class EmailCreator implements CreatorTextable
{
    private final Digits digits = new Digits();

    @Override
    public String create()
    {
        return new StringBuilder()
                .append(
                        Text.generateAlphabeticId(
                                digits.generateInteger(1, 15)))
                .append("@")
                .append(Text.generateAlphabeticId(
                        digits.generateInteger(2, 10)))
                .append(".")
                .append(Text.generateAlphabeticId(
                        digits.generateInteger(2, 5)))
                .toString();
    }
}
