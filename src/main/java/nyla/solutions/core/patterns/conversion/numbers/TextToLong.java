package nyla.solutions.core.patterns.conversion.numbers;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.util.Text;

/**
 * @author gregory green
 */
public class TextToLong implements Converter<String,Long> {
    private final static TextToLong instance = new TextToLong();

    /**
     * Convert from a given value
     * @param longText the long text
     * @return the convert long
     */
    public static Long from(String longText) {
        return instance.convert(longText);
    }

    /**
     * Convert from a given value
     * @param longText the long text
     * @param defaultValue the default value
     * @return the convert long
     */
    public static Long from(String longText, long defaultValue) {
        if(longText == null || longText.isEmpty())
            return defaultValue;

        return from(longText);
    }

    /**
     * USe the fromObject toString to convert to long
     * @param fromObject the object
     * @return the long
     * @param <T> the from input type
     */
    public static <T> Long fromObject(T fromObject) {
        return from(Text.toString(fromObject));
    }

    public static <T> Long fromObject(T fromObject, Long defaultValue) {

        return from(Text.toString(fromObject),defaultValue);
    }


    @Override
    public Long convert(String longText) {
        if(longText == null || longText.isEmpty())
            return null;

        return Long.valueOf(longText);
    }
}
