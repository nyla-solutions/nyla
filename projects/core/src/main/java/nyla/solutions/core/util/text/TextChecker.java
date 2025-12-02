package nyla.solutions.core.util.text;

/**
 * Text checker utility
 * @author Gregory Green
 */
public class TextChecker {

    /**
     * @param val the input string
     * @return true the inputted string is an integer
     */
    public boolean isInteger(String val) {
        if (val == null || val.isEmpty())
            return false;

        val = val.trim();

        for (int i = 0; i < val.length(); i++) {
            if (!Character.isDigit(val.charAt(i))) {
                return false;
            }
        }

        return true;
    }



    /**
     * Determines whether the provided str is equal to null
     * <p>
     * or the length is equal to zero
     *
     * @param str the text to text if is null
     * @return true if the string is em
     */

    public boolean isNull(String str) {
        return str == null || str.trim().isEmpty() ||
                "null".equals(str.trim());
    }


    /**
     * Determine whether the given object is an number (double, number) or can be
     * converted to an number.
     *
     * @param aObject the object to test
     * @return true is an instance of Number or a PARSE-ABLE number
     */
    public boolean isNumber(Object aObject) {
        if (aObject instanceof Number) {
            return true;
        }

        if (aObject == null)
            return false;


        String text = aObject.toString().trim();

        if (text.isEmpty())
            return false;

        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
