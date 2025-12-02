package nyla.solutions.core.util.text;

import nyla.solutions.core.io.IO;

import java.util.regex.Pattern;

/**
 * Text editor utility
 * @author Gregory Green
 */
public class TextEditor {

    /**
     * Append newline to text
     *
     * @param text the text
     * @return the text with the appended the new line
     */
    public String appendNewLine(String text) {

        String newline = IO.newline();

        if (text == null || text.isEmpty())
            return newline;

        StringBuilder sb = new StringBuilder(text);

        if (!text.endsWith(newline)) {
            sb.append(newline);
        }

        return sb.toString();
    }

    public String removeNewLines(String text) {
        return replace("\r\n", " ", text);
    }


    /**
     * End all line with \r\n
     *
     * @param s string to replace
     * @return the converted newline
     */
    public static String newlineToCrLf(String s) {
        String s1 = s.replaceAll("\\r", "");
        return s1.replaceAll("\\n", "\r\n");
    }



    /**
     * @param aOld   the string to be replaced
     * @param aNew   the new string
     * @param aInput the input string
     * @return the replaced string
     */

    public String replace(String aOld, String aNew, String aInput) {

        if (aOld == null || aNew == null || aInput == null) {
            return aInput;

        }

        int i = aInput.indexOf(aOld);

        if (i < 0) {

            //nothing to replace

            return aInput;

        }

        StringBuilder sb = new StringBuilder();

        int start = 0;

        //String tmp = null;

        do {

            //replace all instances
            if (start != i) {

                sb.append(aInput.substring(start, i));

            }

            sb.append(aNew);

            start = i + aOld.length();

            i = aInput.indexOf(aOld, start);

        }

        while (i >= 0);

        //append rest of string

        if (start < aInput.length())

            sb.append(aInput.substring(start, aInput.length()));

        return sb.toString();

    }

    /**
     * @param aText the text for convert to
     * @return the String with initial caps
     */
    public String initCaps(String aText) {
        if (aText == null || aText.isEmpty())
            return aText;

        //Character character = null;
        char textChar;

        boolean needInitUpper = true;
        boolean first = true;
        StringBuilder results = new StringBuilder();

        for (int i = 0; i < aText.length(); i++) {
            textChar = aText.charAt(i);
            if (!Character.isWhitespace(textChar) && needInitUpper) {
                needInitUpper = false;
                first = false;

                textChar = Character.toUpperCase(textChar);
            } else if (!Character.isWhitespace(textChar) && !needInitUpper) {
                textChar = Character.toLowerCase(textChar);
            } else if (!first && Character.isWhitespace(textChar)) {
                needInitUpper = true;
            }

            results.append(textChar);
        }//end for

        return results.toString();
    }

    /**
     * Replace the last instance of the regular expression with a given token
     *
     * @param text        the source text
     * @param re          the regular expression
     * @param replacement the replacement text
     * @return the text with the last regExpr instance replaced
     */
    public String replaceFirstRegExpWith(String text, String re, String replacement) {
        if (text == null || text.isEmpty())
            return "";

        //compile pattern
        Pattern pattern = Pattern.compile(re);

        return pattern.matcher(text).replaceFirst(replacement);

    }
    /**
     * Use a regular expression to update a given text
     *
     * @param text        the text to update
     * @param re          the regular expression
     * @param replaceText the replacement text
     * @return the formatted regular expression
     */
    public String replaceForRegExprWith(final String text, final String re, final String replaceText) {
        if (text == null || text.isEmpty())
            return "";

        Pattern pattern = Pattern.compile(re);

        return pattern.matcher(text).replaceAll(replaceText);

    }

    public String trim(String text, char character) {
        if (text == null)
            return "";

        int beginIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (character != text.charAt(i)) {
                beginIndex = i;
                break;
            }
        }

        int endIndex = -1;
        for (int i = text.length() - 1; i >= 0; i--) {
            if (character != text.charAt(i)) {
                endIndex = i + 1;
                break;
            }
        }

        if (beginIndex < 0) {
            beginIndex = 0;
        }

        if (endIndex < 0)
            endIndex = text.length() - 1;


        return text.substring(beginIndex, endIndex);
    }

}
