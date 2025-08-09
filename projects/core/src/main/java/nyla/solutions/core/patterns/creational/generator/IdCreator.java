package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Digits;

/**
 * @author Gregory Green
 */
public class IdCreator implements CreatorTextable
{
    private Digits digits = new Digits();

    @Override
    public String create()
    {
        return String.valueOf(digits.generateInteger());
    }
}
