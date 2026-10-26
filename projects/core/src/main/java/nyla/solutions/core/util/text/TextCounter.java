package nyla.solutions.core.util.text;

public class TextCounter {

    /**
     * @param val the input string
     * @return the number of digits in a string
     */
    public  int digitCount(String val) {
        if (val == null)
            return 0;

        int c = 0;

        for (int i = 0; i < val.length(); i++) {
            if (Character.isDigit(val.charAt(i))) {
                c++;
            }
        }

        return c;
    }


    /**
     * Get a count of a given character in a text
     *
     * @param character the char to count
     * @param text      the source text
     * @return the number of characters in the given text
     */
    public  int characterCount(char character, String text) {
        if (text == null || text.isEmpty())
            return 0;

        int c = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == character) {
                c++;
            }
        }

        return c;
    }
}
