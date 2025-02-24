package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Digits;

public class PickRandomTextCreator implements CreatorTextable{
    private final String[] textOptions;
    private final Digits digits = new Digits();

    public PickRandomTextCreator(String... textOptions) {
        this.textOptions = textOptions;
    }

    public static PickRandomTextCreator options(String... textOptions) {
        return new PickRandomTextCreator(textOptions);
    }


    @Override
    public String create() {
        var i = digits.generateInteger(0,textOptions.length-1);
        return textOptions[i];
    }
}
