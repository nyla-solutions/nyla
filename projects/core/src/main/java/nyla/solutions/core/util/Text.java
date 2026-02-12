package nyla.solutions.core.util;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.decorator.BasicTextStyles;
import nyla.solutions.core.patterns.decorator.TextStyles;
import nyla.solutions.core.util.text.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static nyla.solutions.core.util.Config.settings;

/**
 * <pre>
 * <b>Text</b> is geared toward string based processing. It includes template engine support
 * like Free Marker that builds composite strings/values dynamically at runtime
 *  (see <a href="http://freemarker.sourceforge.net/">http://freemarker.sourceforge.net/</a>). There are also methods to support complex
 *  regular expressions with Boolean AND, OR and NOT logic, numerous string conversions,
 *  general text manipulation and parsing methods.
 * </pre>
 *
 * @author Gregory Green
 */

public class Text {

    private static final TextEditor textEditor = new TextEditor();
    private static TextStyles textStyles = null;
    private static final TextFormatter textFormatter = new TextFormatter();
    private static  final TextGenerator textGenerator = new TextGenerator();

    /**
     * TEMPLATE_DIR_PROP_NM = Text.class.getName()+".template.dir"
     */
    public static final String TEMPLATE_DIR_PROP_NM = Text.class.getName() + ".template.dir";


    /**
     * TEMPLATE_EXTENSION_PROP_NM = Text.class.getName()+".template.extension"
     */
    public static final String TEMPLATE_EXTENSION_PROP_NM = Text.class.getName() + ".template.extension";


    /**
     * TEMPLATE_EXTENSION = settings().getProperty(TEMPLATE_EXTENSION_PROP_NM,".txt")
     */
    public static final String TEMPLATE_EXTENSION = settings().getProperty(TEMPLATE_EXTENSION_PROP_NM, ".txt");

    /**
     * TEMPLATE_CLASSPATH_ROOT = settings().getProperty(Text.class,"TEMPLATE_CLASSPATH_ROOT","templates")
     */
    public static final String TEMPLATE_CLASSPATH_ROOT = settings().getProperty(Text.class, "TEMPLATE_CLASSPATH_ROOT",
            "templates");

    /**
     * TEMPLATE_LOCALE_COUNTRY = Text.class.getName()+".TEMPLATE_LOCALE_COUNTRY"
     */
    public static final String TEMPLATE_LOCALE_COUNTRY = Text.class.getName() + ".TEMPLATE_LOCALE_COUNTRY";
    /**
     * TEMPLATE_LOCALE_LANGUAGE = Text.class.getName()+".TEMPLATE_LOCALE_LANGUAGE"
     */
    public static final String TEMPLATE_LOCALE_LANGUAGE = Text.class.getName() + ".TEMPLATE_LOCALE_LANGUAGE";
    /**
     * SPECIAL_START = "${START}" used for the parse method (start tag)
     */
    public static final String SPECIAL_START = "${START}";
    /**
     * SPECIAL_END = "${END} used for the parse method (end tag)
     */
    public static final String SPECIAL_END = "${END}";
    /**
     * dateFormat = settings().getProperty(Text.class,"dateFormat", "MM/dd/yyyy")
     */
    public static final String DATETIME_FORMAT = settings().getProperty(Text.class, "dateFormat", "M/dd/yyyy hh:mm:ss:SS " +
            "a");
    public static final String DATE_FORMAT = settings().getProperty(Text.class, "dateFormat", "M/dd/yyyy");



    private static final TextChecker textChecker = new TextChecker();
    private static final TextMatcher textMatcher = new TextMatcher();
    private static final TextDates textDates = new TextDates();
    private static final TextParser textParser = new TextParser();


    /**
     * Inject information with the property solutions.global.util.Text.textStyles=className
     *
     * @return implementation of  text styles
     */
    public static TextStyles styles() {
        if (textStyles == null) {
            String className = settings().getProperty(Text.class, "textStyles", BasicTextStyles.class.getName());

            try {
                textStyles = ClassPath.newInstance(className);
            } catch (Exception e) {
                throw new SetupException("Unable to create TextStyles instance:" + className, e);
            }
        }

        return textStyles;

    }




    /**
     * Split a single text into many determined by the separation character
     *
     * @param text the text to split
     * @param re    the regular expression separation character
     * @return the array of the split strings
     */
    public static String[] splitRE(String text, String re) {
        if (text == null || text.isEmpty())
            return null;

        if (re == null)
            throw new RequiredException("regular expression");

        return text.split(re);
    }

    /**
     * Split a single text into many determined by the separation character
     *
     * @param aText the text to split
     * @return the array of the split strings
     */
    public static String[] split(String aText) {
        if (aText == null || aText.isEmpty())
            return null;

        StringTokenizer toks = new StringTokenizer(aText, " \t\n\r");


        return format().toStrings(toks);
    }

    /**
     * Split a single text into many determined by the separation character
     *
     * @param text  the text to split
     * @param token the separation character
     * @return the array of the split strings
     */
    public static String[] split(String text, String token) {
        if (text == null)
            return null;

        if (token == null) {
            return new String[]{text};
        }

        int i = text.indexOf(token);

        if (i < 0) {
            //nothing to replace

            String[] results = {text};
            return results;

        }

        ArrayList<String> resultList = new ArrayList<String>(16);
        int start = 0;


        do {

            //split all instances
            if (start != i) {
                resultList.add(text.substring(start, i));
            }

            start = i + token.length();
            i = text.indexOf(token, start);

        }

        while (i >= 0);

        //append rest of string

        if (start < text.length())

            resultList.add(text.substring(start, text.length()));

        if (resultList.isEmpty()) {
            throw new IllegalArgumentException("Unexpected empty list");
        }

        resultList.trimToSize();

        String[] results = new String[resultList.size()];

        resultList.toArray(results);

        return results;
    }

    /**
     * Split Regular expressions
     *
     * @param <T>    the class type
     * @param line   the line to convert
     * @param re     the regular express
     * @param class1 the class to convert to
     * @return the array of the converted class types
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] splitRE(String line, String re, Class<T> class1) {
        String[] text = splitRE(line, re);


        if (text == null || text.length == 0)
            return null;

        T[] results = (T[]) Array.newInstance(class1, text.length);
        for (int i = 0; i < text.length; i++) {
            results[i] = ClassPath.newInstance(class1, text[i]);
        }
        return results;
    }


    /**
     * IndexOf function based on regular expressions
     *
     * @param text   the Text to start
     * @param regExp the regular expression
     * @return the start index of the match regular expression
     */
    public static int indexOfRegExp(String text, String regExp) {
        Pattern amountsPattern = Pattern.compile(regExp);
        Matcher matcher = amountsPattern.matcher(text);

        if (matcher.find())
            return matcher.start();
	   
	   /* while (matcher.find()) {
		   System.out.println("Starting & ending index of " + matcher.group()+ ":=" +
		  "start=" + matcher.start() + " end = " + matcher.end());
		   }
	   */
        return -1;

    }

    /**
     * Summary of regular-expression constructs
     * Construct Matches
     * <p>
     * Characters
     * x The character x
     * \\ The backslash character
     * \0n The character with octal value 0n (0 &lt;= n  &amp;lt;= 7)
     * \0nn The character with octal value 0nn (0  &amp;lt;= n  &amp;lt;= 7)
     * \0mnn The character with octal value 0mnn (0  &amp;lt;= m  &amp;lt;= 3, 0  &amp;lt;= n  &amp;lt;= 7)
     * \xhh The character with hexadecimal value 0xhh
     * <p>
     * \t The tab character ('\u0009')
     * \n The newline (line feed) character ('\u000A')
     * \r The carriage-return character ('\u000D')
     * \f The form-feed character ('\u000C')
     * \a The alert (bell) character ('\u0007')
     * \e The escape character ('\u001B')
     * \cx The control character corresponding to x
     * <p>
     * Character classes
     * [abc] a, b, or c (simple class)
     * [^abc] Any character except a, b, or c (negation)
     * [a-zA-Z] a through z or A through Z, inclusive (range)
     * [a-d[m-p]] a through d, or m through p: [a-dm-p] (union)
     * [a-z &amp; &amp;[def]] d, e, or f (intersection)
     * [a-z &amp; &amp;[^bc]] a through z, except for b and c: [ad-z] (subtraction)
     * [a-z &amp; &amp;[^m-p]] a through z, and not m through p: [a-lq-z](subtraction)
     * <p>
     * Predefined character classes
     * . Any character (may or may not match line terminators)
     * \d A digit: [0-9]
     * \D A non-digit: [^0-9]
     * \s A whitespace character: [ \t\n\x0B\f\r]
     * \S A non-whitespace character: [^\s]
     * \w A word character: [a-zA-Z_0-9]
     * \W A non-word character: [^\w]
     * <p>
     * POSIX character classes (US-ASCII only)
     * \p{Lower} A lower-case alphabetic character: [a-z]
     * \p{Upper} An upper-case alphabetic character:[A-Z]
     * \p{ASCII} All ASCII:[\x00-\x7F]
     * \p{Alpha} An alphabetic character:[\p{Lower}\p{Upper}]
     * \p{Digit} A decimal digit: [0-9]
     * \p{Alnum} An alphanumeric character:[\p{Alpha}\p{Digit}]
     * \p{Punct} Punctuation: One of !"#$% &amp;'()*+,-./:;&lt;=&gt;?@[\]^_`{|}~
     * \p{Graph} A visible character: [\p{Alnum}\p{Punct}]
     * \p{Print} A printable character: [\p{Graph}]
     * \p{Blank} A space or a tab: [ \t]
     * \p{Cntrl} A control character: [\x00-\x1F\x7F]
     * \p{XDigit} A hexadecimal digit: [0-9a-fA-F]
     * \p{Space} A whitespace character: [ \t\n\x0B\f\r]
     * <p>
     * Classes for Unicode blocks and categories
     * \p{InGreek} A character in the Greek block (simple block)
     * \p{Lu} An uppercase letter (simple category)
     * \p{Sc} A currency symbol
     * \P{InGreek} Any character except one in the Greek block (negation)
     * [\p{L} &amp; &amp;[^\p{Lu}]]  Any letter except an uppercase letter (subtraction)
     * <p>
     * Boundary matchers
     * ^ The beginning of a line
     * $ The end of a line
     * \b A word boundary
     * \B A non-word boundary
     * \A The beginning of the input
     * \G The end of the previous match
     * \Z The end of the input but for the final terminator, if any
     * \z The end of the input
     * <p>
     * Greedy quantifiers
     * X? X, once or not at all
     * X* X, zero or more times
     * X+ X, one or more times
     * X{n} X, exactly n times
     * X{n,} X, at least n times
     * X{n,m} X, at least n but not more than m times
     * <p>
     * Reluctant quantifiers
     * X?? X, once or not at all
     * X*? X, zero or more times
     * X+? X, one or more times
     * X{n}? X, exactly n times
     * X{n,}? X, at least n times
     * X{n,m}? X, at least n but not more than m times
     * <p>
     * Possessive quantifiers
     * X?+ X, once or not at all
     * X*+ X, zero or more times
     * X++ X, one or more times
     * X{n}+ X, exactly n times
     * X{n,}+ X, at least n times
     * X{n,m}+ X, at least n but not more than m times
     * <p>
     * Logical operators
     * XY X followed by Y
     * X|Y Either X or Y
     * (X) X, as a capturing group
     * <p>
     * Back references
     * \n Whatever the nth capturing group matched
     * <p>
     * Quotation
     * \ Nothing, but quotes the following character
     * \Q Nothing, but quotes all characters until \E
     * \E Nothing, but ends quoting started by \Q
     * <p>
     * Special constructs (non-capturing)
     * (?:X) X, as a non-capturing group
     * (?idmsux-idmsux)  Nothing, but turns match flags on - off
     * (?idmsux-idmsux:X)   X, as a non-capturing group with the given flags on - off
     * (?=X) X, via zero-width positive lookahead
     * (?!X) X, via zero-width negative lookahead
     * (?&lt;=X) X, via zero-width positive lookbehind
     * (?&lt;!X) X, via zero-width negative lookbehind
     * (?&gt;X) X, as an independent, non-capturing group
     *
     * @param matchRegExp the match regular expression
     * @param text the text
     * @return the patching results
     */
    public static String grepText(String matchRegExp, String text) {
        if (text == null || matchRegExp == null)
            return "";

        var tok = new StringTokenizer(text, "\r\n");

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(matchRegExp);

        String line;
        while (tok.hasMoreTokens()) {
            line = tok.nextToken();

            if (p.matcher(line).find()) {
                return line;
            }
        }

        return "";

    }

    public static Collection<String> grepAllTexts(String matchRegExp, String text) {
        if (text == null || matchRegExp == null)
            return null;

        String[] words = text.split("[ \t\n]");

        if (words == null || words.length == 0)
            return null;

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(matchRegExp);

        List<String> wordsList = asList(words);
        List<String> results = wordsList.parallelStream().filter(
                word -> p.matcher(word.trim()).find()).collect(Collectors.toList());

        if (results == null || results.isEmpty())
            return null;

        return results;
    }

    /**
     * Determines whether the provided str is equal to null
     * <p>
     * or the length is equal to zero
     *
     * @param str the text to text if is null
     * @return true if the string is em
     */

    public static boolean isNull(String str) {
        return check().isNull(str);
    }

    public static TextChecker check() {
        return textChecker;
    }


    /**
     * Compares two strings. Similar to using the String.equals()
     * method, but avoids NullPointerExceptions by having to check
     * for either String being null
     *
     * @param str1 One of 2 strings to be compared
     * @param str2 Other of 2 strings to be compared
     * @return the boolean
     */

    public static boolean strCompare(String str1, String str2) {

        boolean isEqual = false;

        if (str1 == null && str2 == null) {

            isEqual = true;

        } else if (str1 != null && str2 == null) {

            isEqual = false;

        } else if (str1 == null && str2 != null) {

            isEqual = false;

        } else {

            if (str1 == null)
                return false;

            isEqual = str1.equals(str2);

        }

        return isEqual;

    }

    /**
     * @param val the input string
     * @return the number of digits in a string
     */
    public static int digitCount(String val) {
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
    public static int characterCount(char character, String text) {
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



    /**
     * @param aObject the object to convert
     * @return to string version of object
     */
    public static String toString(Object aObject) {
        if (aObject == null)
            return "";

        if (aObject instanceof Date) {
            return Text.format().formatDate((Date) aObject);
        } else if (aObject instanceof Calendar) {
            return Text.format().formatDate((Calendar) aObject);
        } else if (aObject instanceof Object[] objects) {

            StringBuilder text = new StringBuilder();
            for (int i = 0; i < objects.length; i++) {
                if (i != 0)
                    text.append(",");

                text.append(objects[i]);
            }
            return text.toString();
        } else if (aObject instanceof Textable) {
            return ((Textable) aObject).getText();
        } else if (aObject instanceof byte[]) {

            return new String((byte[]) aObject, IO.CHARSET);
        } else if (aObject instanceof InputStream) {
                return IO.reader().readText((InputStream) aObject);
        }


        return aObject.toString();
    }

    public static <T> String mergeArray(String separator, T[] merging) {
        if (merging == null || merging.length == 0)
            return null;

        StringBuilder text = new StringBuilder();

        if (separator == null)
            separator = "";

        for (Object merge : merging) {
            if (!text.isEmpty())
                text.append(separator);

            text.append(merge);
        }

        return text.toString();
    }

    /**
     * Example: assertEquals("1,2",Text.merge(",",1,2));
     *
     * @param separator the merge separator
     * @param merging   the array of object string to merge
     * @return the merged text
     */
    public static String merge(String separator, Object... merging) {
        return mergeArray(separator, merging);
    }

    public static String build(String... texts) {
        if (texts.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();
        for (String text : texts) {
            builder.append(text);
        }
        return builder.toString();
    }

    /**
     *
     * @param text the text
     * @param defaultValue
     * @return
     * @param <T>
     */
    public static <T> String valueOf(T text, T defaultValue) {
        if(text == null)
            return String.valueOf(defaultValue);

        var textString = text.toString();
        if(textString.isEmpty() || "null".equals(textString))
            return String.valueOf(defaultValue);

        return textString;
    }

    public static TextEditor editor() {
        return textEditor;
    }

    public static TextFormatter format() {
        return textFormatter;
    }

    public static TextGenerator generator() {
        return textGenerator;
    }

    public static TextMatcher match() {
        return textMatcher;
    }

    public static boolean matches(String sourceValue, String complexRegularExpression) {
        return match().matches(sourceValue,complexRegularExpression);
    }

    public static TextDates dates() {
        return textDates;
    }

    public static TextParser parser() {
        return textParser;
    }
}
