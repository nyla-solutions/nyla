package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Digits;

public class ZipCreator implements Creator<String> {

    private final Digits digits = new Digits();

    @Override
    public String create() {
        return new StringBuilder()
                .append("55")
                .append(digits.generateInteger(0,9))
                .append(digits.generateInteger(0,9))
                .append(digits.generateInteger(0,9)).toString();
    }
}
