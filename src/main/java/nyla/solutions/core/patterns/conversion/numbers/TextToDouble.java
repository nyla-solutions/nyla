package nyla.solutions.core.patterns.conversion.numbers;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.util.Text;

/**
 * Conversion from text to double
 * @author gregory green
 */
public class TextToDouble implements Converter<String,Double> {

    private final Double defaultValue;

    public TextToDouble(Double defaultValue) {
        this.defaultValue = defaultValue;
    }


    @Override
    public Double convert(String text) {
        if(text == null || text.isEmpty())
            return defaultValue;

        return Double.valueOf(text);
    }

    /**
     *
     * @param objectToString the object to convert
     * @param defaultValue the default double if object is null
     * @return the double value
     */
    public static double fromObject(Object objectToString, Double defaultValue) {
        return new TextToDouble(defaultValue).convert(Text.toString(objectToString));
    }
    public static double fromObject(Object objectToString, double defaultValue) {
        return new TextToDouble(defaultValue).convert(Text.toString(objectToString));
    }
}
