package nyla.solutions.core.patterns.conversion.numbers;

import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.conversion.Converter;

import static java.lang.String.valueOf;

public class NumberToStringConverter<NumberType extends Number> implements Converter<NumberType,String> {

    private final Class<NumberType> longClass;

    public NumberToStringConverter(Class<NumberType> longClass) {
        this.longClass = longClass;
    }

    public static String from(Number number) {
        if(number == null)
            return "";

        return new NumberToStringConverter(number.getClass()).convert(number);
    }

    @Override
    public String convert(NumberType sourceObject) {
        if(sourceObject == null)
            return valueOf(ClassPath.newInstance(longClass));

        return sourceObject.toString();
    }
}
