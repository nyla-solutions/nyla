package nyla.solutions.core.util;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.decorator.BasicTextStyles;
import nyla.solutions.core.patterns.decorator.TextStyles;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Function;
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
     * Complex matches ${NOT}
     */
    public static final String NOT = "${NOT}";
    /**
     * Complex matches ${OR}
     */
    public static final String OR = "${OR}";
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
    //private static final String encoding = settings().getProperty(Text.class,"encoding","ISO-8859-1");
    private static final Random random = new Random(Calendar.getInstance().getTime().getTime());
    private static final String[] fixedNumberPrefixLookup =
            {
                    "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000"
            };
    /**
     * Complex matches and
     */
    private static final String AND = "${AND}";
    private static TextStyles textStyles = null;

    /**
     * Append newline to text
     *
     * @param text the text
     * @return the text with the appended the new line
     */
    public static String appendNewLine(String text) {

        String newline = IO.newline();

        if (text == null || text.isEmpty())
            return newline;

        StringBuilder sb = new StringBuilder(text);

        if (!text.endsWith(newline)) {
            sb.append(newline);
        }

        return sb.toString();
    }

    /**
     * Inject an with the property solutions.global.util.Text.textStyles=className
     *
     * @return implementation of  text styles
     */
    public static TextStyles getTextStyles() {
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

    public static String fixedLength(int number, int length) {
        String numberText = String.valueOf(number);
        int numberLength = numberText.length();

        StringBuilder sb = new StringBuilder();

        if (length > 16) {
            sb.ensureCapacity(length);
        }

        sb.append(numberText);

        //prefix 0
        if (length == numberLength)
            return sb.toString();


        if (numberLength > length) {
            sb.setLength(length);
            return sb.toString();
        }

        int diff = length - numberLength;

        if (diff < fixedNumberPrefixLookup.length) {
            sb.insert(0, fixedNumberPrefixLookup[diff - 1]);
            return sb.toString();
        }

        //prefix 0(s) =
        //length = 2 and value = 1 = 01

        for (int i = 0; i < length - numberLength; i++) {
            sb.insert(0, "0");
        }

        return sb.toString();
        
    }

    public static String fixedLength(String text, int length) {
        StringBuilder sb = new StringBuilder(length);

        sb.append(text);
        sb.setLength(length);
        return sb.toString();

    }

    public static String fixedLength(String text, int length, char fillChar) {

        StringBuilder sb = new StringBuilder(length);
        sb.append(text);
        sb.setLength(length);

        //Take original
        if (fillChar != '\0') {
            //replace with prefix
            int originalLength = text.length();

            if (originalLength < sb.length()) {
                String newEnd = sb.substring(originalLength, sb.length()).replace('\0', fillChar);
                sb = sb.replace(originalLength, sb.length(), newEnd);
            }

        }

        return sb.toString();

    }

    /**
     * Loop thrus map to formats any needed value with data from other properties.
     * <p>
     * See Text.format(String,Map)
     *
     * @param map the map to format
     * @throws FormatException when format error occurs
     */
    public static void formatMap(Map<Object, Object> map)
            throws FormatException {
        if (map == null || map.isEmpty())
            return;// nothing to process

        //format properties
        Object key, value;
        String text;

        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            if (!(value instanceof String))
                continue; //skip

            text = (String) value;

            //overwrite value with formatted version
            if (text.contains("${"))
                map.put(key, Text.format(text, map));
        }

    }

    /**
     * Generate a unique ID
     *
     * @return the generated ID
     */
    public static String generateId() {
        var date = Calendar.getInstance().getTime();

        var dateId = formatDate("yyyyMMddHHmmssSS", date);

        return dateId + random.nextInt();

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
     * @param lines tokens separated by \t\n\r
     * @return tokens separated by \n (newline)
     */
    public static Set<String> toSet(String[] lines) {
        return new TreeSet<String>(asList(lines));
    }

    public static String removeNewLines(String text) {
        return replace("\r\n", " ", text);
    }

    /**
     * @param aValue the value
     * @param aType  the type of the value
     * @return value cast to the object type instance
     */
    public static Object toObject(String aValue, String aType) {
        if ("Date".equalsIgnoreCase(aType)) {
            return toDate(aValue);
        } else if ("Boolean".equalsIgnoreCase(aType)) {
            return Boolean.valueOf(aValue);
        } else if ("Int".equalsIgnoreCase(aType) ||
                "Integer".equalsIgnoreCase(aType)) {
            return Integer.valueOf(aValue);
        } else if ("Double".equalsIgnoreCase(aType)) {
            return Double.valueOf(aValue);
        } else {
            return aValue;
        }

    }

    /**
     * Use a regular expression to update a given text
     *
     * @param text        the text to update
     * @param re          the regular expression
     * @param replaceText the replacement text
     * @return the formatted regular expression
     */
    public static String replaceForRegExprWith(final String text, final String re, final String replaceText) {
        if (text == null || text.isEmpty())
            return "";

        Pattern pattern = Pattern.compile(re);

        return pattern.matcher(text).replaceAll(replaceText);

    }

    /**
     * Parse text based on regular expressions
     * TODO: add multiple results
     *
     * @param startRE the start regular express
     * @param endRE   the end regular expression
     * @param text    the text to parse
     * @return the parsed output
     */
    public static String parseRE(String text, String startRE, String endRE) {
        try {
            if (text == null || text.isEmpty())
                return text;

            if (startRE == null || endRE == null)
                return text;

            Pattern startPattern = Pattern.compile(startRE);
            Pattern endPattern = Pattern.compile(endRE);

            Matcher startMatcher = startPattern.matcher(text);

            if (!startMatcher.find())
                return text;

            int startIndex = startMatcher.end();

            String endText = text.substring(startIndex);

            Matcher endMatcher = endPattern.matcher(endText);
            if (!endMatcher.find())
                return text.substring(startIndex);

            int endIndex = endMatcher.start();

            if (endIndex < 0)
                endIndex = endText.length() - 1;

            return text.substring(startIndex, startIndex + endIndex);
        } catch (IllegalStateException e) {
            throw new SystemException("startRE=" + startRE + " endRE=" + endRE + " text=" + text + " " + Debugger.stackTrace(e));
        }
    }

    /**
     * Replace the last instance of the regular expression with a given token
     *
     * @param text        the source text
     * @param re          the regular expression
     * @param replacement the replacement text
     * @return the text with the last regExpr instance replaced
     */
    public static String replaceFirstRegExpWith(String text, String re, String replacement) {
        if (text == null || text.isEmpty())
            return "";

        //compile pattern
        Pattern pattern = Pattern.compile(re);

        return pattern.matcher(text).replaceFirst(replacement);

    }

    /**
     * @param object to convert collection of String to array
     * @return the array of strings
     */
    @SuppressWarnings("unchecked")
    public static String[] toStrings(Object object) {
        if (object == null)
            return null;


        if (object instanceof Collection) {
            return toStrings((Collection<Object>) object);
        } else if (object instanceof Object[]) {
            return toStrings(asList((Object[]) object));
        } else {
            //return single array value

            return new String[]{object.toString()};
        }


    }


    /**
     * @param collection convert collection of String to array
     * @return the array of strings
     */
    public static String[] toStrings(Collection<Object> collection) {
        if (collection == null || collection.isEmpty())
            return null;

        String[] texts = new String[collection.size()];

        int i =0;
        for (Object obj : collection){
            texts[i] = String.valueOf(obj);
            i++;
        }

        return texts;

    }

    public static Collection<String> toUpperCase(Collection<String> collection) {
        Function<String, String> function = text -> text != null ? text.toUpperCase() : text;

        return transform(function,collection);

    }

    public static Collection<String> transformTexts(Function<String, String> function, String... collection) {
        return transform(function,asList(collection));
    }
    public static Collection<String> transform(Function<String, String> function, Collection<String> collection) {
        if (collection == null)
            return null;

        if (collection.isEmpty())
            return collection;

        var list = new ArrayList<String>(collection.size());

        for (String string : collection) {
            list.add(function.apply(string));
        }

        return list;
    }

    public static <T> String toText(Collection<T> collection, String separator) {
        if (collection == null || collection.isEmpty())
            return "";

        if (separator == null)
            separator = "";

        StringBuilder sb = new StringBuilder();

        Object object = null;
        for (T t : collection) {
            object = t;

            if (object == null)
                continue;  //skip
            if (!sb.isEmpty()) {
                sb.append(separator);
            }

            sb.append(object);
        }

        return sb.toString();
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


        return toStrings(toks);
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
     * @param aTokenizer the string token object
     * @return the array of tokens
     */
    public static String[] toStrings(StringTokenizer aTokenizer) {
        String[] texts = new String[aTokenizer.countTokens()];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = aTokenizer.nextToken();
            if (texts[i] != null)
                texts[i] = texts[i].trim();
        }
        return texts;
    }

    /**
     * Convert a text string to a date instance
     *
     * @param text the text to convert
     * @return date version of text
     */
    public static Date toDate(String text) {
        return toDate(text, DATE_FORMAT);

    }

    /**
     * Convert a text string to a date instance
     *
     * @param aText      the text to convert
     * @param dateFormat the date format
     * @return date version of text
     */
    public static Date toDate(String aText, String dateFormat) {
        try {
            if (aText == null || aText.isEmpty())
                throw new IllegalArgumentException("aText required in toDate");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            return simpleDateFormat.parse(aText);
        } catch (ParseException e) {
            Debugger.printWarn(e);
            return null;
        }
    }

    /**
     *
     */
    private static int indexOf(String aContent, String aSearchText, int aFromIndex) {
        if (SPECIAL_START.equals(aSearchText)) {
            return aFromIndex;
        } else if (SPECIAL_END.equals(aSearchText)) {
            return aContent.length();
        }


        return aContent.indexOf(aSearchText, aFromIndex);
    }

    /**
     * @param content the text content
     * @param searchText the search text
     * @return indexOf(aContent, aSearchText, 0)
     */
    private static int indexOf(String content, String searchText) {
        return indexOf(content, searchText, 0);
    }

    /**
     * First occurrence of text
     *
     * @param aContent the content to parse
     * @param aStart   the start tag
     * @param aEnd     the end tag
     * @return the collection of parsed output
     */
    public static String parseText(String aContent, String aStart, String aEnd) {
        Collection<String> results = parse(aContent, aStart, aEnd);

        if (results == null || results.isEmpty()) {
            return "";
        }

        return results.iterator().next().toString();
    }

    public static String parseText(Reader aContent, String aStart, String aEnd)
            throws IOException {
        //TODO: parse single
        Collection<Object> results = parse(aContent, aStart, aEnd);

        if (results == null || results.isEmpty()) {
            return "";
        }

        return results.iterator().next().toString();
    }

    /**
     * Parse all string surrounded by the aStart and aEnd tag
     * aIgnoreCase = false
     *
     * @param aContent the content to parse
     * @param aStart   the content start tag
     * @param aEnd     the content end tag
     * @return the collection of parsed string
     */
    public static Collection<String> parse(String aContent, String aStart, String aEnd) {
        if ((aStart == null || aStart.isEmpty()) && (aEnd == null || aEnd.isEmpty()))
            return asList(aContent);

        return parse(aContent, aStart, aEnd, false);
    }

    /**
     * Parse all string surrounded by the aStart and aEnd tag
     * aIgnoreCase = false
     *
     * @param aContent the content to parse
     * @param aStart   the content start tag
     * @param aEnd     the content end tag
     * @return the collection of parsed objects
     * @throws IOException when an IO error occurs
     */
    public static Collection<Object> parse(Reader aContent, String aStart, String aEnd)
            throws IOException {
        return parse(aContent, aStart, aEnd, false);
    }

    /**
     * Parse all string surrounded by the aStart and aEnd tag
     *
     * @param content    the content to parse
     * @param start      the content start tag
     * @param end        the content end tag
     * @param ignoreCase determine whether to ignore case
     * @return the collection of parsed output
     */
    public static Collection<String> parse(String content, String start, String end, boolean ignoreCase) {
        String compareContent;
        if (ignoreCase) {
            compareContent = content.toUpperCase();
            start = start.toUpperCase();
            end = end.toUpperCase();
        } else {
            compareContent = content;
        }

        ArrayList<String> results = new ArrayList<String>(10);
        int indexOfHref = indexOf(compareContent, start);

        int startText = indexOfHref + start.length();
        if (SPECIAL_START.equals(start)) {
            startText = 0;
        }


        int endText = indexOf(content, end, startText);

        if (endText == content.length() && startText == 0) {
            results.add(content);
            return results;
        }

        String txt = null;
        while (startText > -1 && endText >= startText) {
            txt = content.substring(startText, endText);
            results.add(txt);

            indexOfHref = indexOf(compareContent, start, endText);

            if (indexOfHref < 0)
                break;

            startText = indexOfHref + start.length();
            endText = indexOf(content, end, startText);
        }

        return results;
    }

    /**
     * Parse all string surrounded by the aStart and aEnd tag
     *
     * @param reader     the reader content to parse
     * @param start      the content start tag
     * @param end        the content end tag
     * @param ignoreCase determine whether to ignore the case
     * @return collection of parse strings
     * @throws IOException when an IO error occurs
     */

    public static Collection<Object> parse(Reader reader, String start, String end, boolean ignoreCase)
            throws IOException {
        BufferedReader bufferedReader;

        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }

        String content;
        String compareContent;
        ArrayList<Object> results = new ArrayList<>();
        while ((content = bufferedReader.readLine()) != null) {

            if (ignoreCase) {
                compareContent = content.toUpperCase();
                start = start.toUpperCase();
                end = end.toUpperCase();
            } else {
                compareContent = content;
            }

            int indexOfHref = indexOf(compareContent, start);

            int startText = indexOfHref + start.length();
            if (SPECIAL_START.equals(start)) {
                startText = 0;
            }


            int endText = indexOf(content, end, startText);

            if (endText == content.length() && startText == 0) {
                results.add(content);
                return results;
            }

            String txt;
            while (startText > -1 && endText >= startText) {
                txt = content.substring(startText, endText);
                results.add(txt);

                indexOfHref = indexOf(compareContent, start, endText);

                if (indexOfHref < 0)
                    break;

                startText = indexOfHref + start.length();
                endText = indexOf(content, end, startText);
            }

        }

        return results;
    }

    /**
     * Not complex regular expression match
     *
     * @param re    the regular expression
     * @param aValue the value to test
     * @return true if the regular expression matches the given value
     */
    protected static boolean matchesRE(String re, Object aValue) {
        if (aValue == null)
            return false;

        if (re.contains("(")) {
            if (re.indexOf(")") < 0) {
                //correct unbalanced
                re = new StringBuilder().append(re).append(")").toString();
            }
        } else {
            //no start
            if (re.indexOf(")") > -1) {
                //correct unbalanced
                re = new StringBuilder("(").append(re).toString();
            }
        }

        Pattern pattern = Pattern.compile(re, Pattern.DOTALL);
        java.util.regex.Matcher matcher = pattern.matcher(Text.toString(aValue));
        return matcher.matches();

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
        return str == null || str.trim().isEmpty() ||
                "null".equals(str.trim());
    }

    /**
     * @param aText the text for convert to
     * @return the String with initial caps
     */
    public static String initCaps(String aText) {
        if (isNull(aText))
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

    public static String toByteText(byte[] aByte) {

        if (aByte == null)

            return "";

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < aByte.length; i++) {

            text.append(Byte.toString(aByte[i]));

            if (i + 1 < aByte.length)

                text.append(" ");

        }

        return text.toString();

    }

    public static byte[] toBytesFromByteText(String aByteText) {

        if (aByteText == null)

            return new byte[0];

        StringTokenizer tok = new StringTokenizer(aByteText);

        byte[] bytes = new byte[tok.countTokens()];

        int i = 0;

        while (tok.hasMoreTokens()) {

            bytes[i] = Byte.valueOf(tok.nextToken());

            i++;

        }

        return bytes;

    }

    /**
     * @param doubleText the double text
     * @return formatted currency
     */
    public static String formatCurrency(String doubleText) {
        if (doubleText == null || doubleText.isEmpty())
            return "";

        return formatCurrency(Double.valueOf(doubleText));
    }

    /**
     * Format a number as a currency
     *
     * @param number a money amount
     * @return formatted currency
     */
    public static String formatCurrency(double number) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        return numberFormat.format(number);
    }//---------------------------------------

    /**
     * Used the"#,##0.0###" formatting for a given number
     *
     * @param aNumber the number to format
     * @return formatted number
     */
    public static String formatDouble(String aNumber) {
        return formatDouble(aNumber, "#,##0.0###");
    }

    /**
     * @param number the number to format
     * @param format the format
     * @return formatted number
     */
    public static String formatDouble(String number, String format) {
        if (number == null || number.isEmpty())
            return "";

        return formatDouble(Double.valueOf(number), format);

    }

    /**
     * @param number the double number to format
     * @return formatted number
     */
    public static String formatDouble(double number) {
        return formatDouble(number, "#,##0.0###");
    }

    /**
     * @param number the number to format
     * @param format the format
     * @return formatted number
     */
    public static String formatDouble(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);

    }

    /**
     * @param aNumber the number to format
     * @return formatted number
     */
    public static String formatNumber(String aNumber) {

        return formatDouble(aNumber);
    }

    /**
     * @param number the double number
     * @return formatted number
     */
    public static String formatNumber(double number) {
        return formatDouble(number);
    }

    /**
     * @param number the number to format
     * @param format the format of the number
     * @return formatted number
     */
    public static String formatNumber(double number, String format) {

        return formatDouble(number, format);
    }

    /**
     * @param number the percentage number to format
     * @return formatted number
     */
    public static String formatPercent(String number) {
        if (number == null || number.isEmpty())
            return "";

        return formatPercent(Double.valueOf(number).doubleValue());
    }

    /**
     * @param number
     * @return the formatted percent
     */
    public static String formatPercent(double number) {
        return formatNumber(number, "###.##'%'");
    }//---------------------------------------

    /**
     * <pre>
     * Regular expressions do not have an easy way to chain expressions together using AND/NOT/OR logic.
     * The RE operation combines regular expressions
     * with a special syntax to support AND/NOT/OR logic.
     *
     * <b>AND Operation</b>
     * The RE operation can support chaining expressions together with �AND� logic. This is accomplished by chaining
     * expressions together with �${AND}�. The string �${AND}� can be used to separate two regular expressions.
     * If any of the regular expressions return false then the entire regular expression is false. In the following
     * example, the regular expression �.*USA.*${AND}.*Greece.*�, only returns true if the text contains both
     * �USA� and �Greece�.
     *
     * <b>NOT Operation</b>
     * The RE operation can supports negative logic (NOT) for expressions. This is accomplished by prefixing the
     * expressions with  ${NOT}. In the following example, the regular expression �.*USA.*� only returns true
     * if the text does not contain the word �USA�. Note that multiple �${NOT}�(s) can be chained together with
     * �${AND}�(s) (see table below).
     *
     * <b>OR Operation</b>
     * Regular express ${OR} returns true if the expressions to the right or left of the ${OR} are true
     * .*Chicken.*${OR}.*Egg.* return true for the string "Chickens run" and "Egg are layed"
     *
     *
     * Use complex boolean logic regular expression by adding ${AND}, ${OR} and ${NOT} tags
     * </pre>
     *
     * @param sourceValue              the source value to test
     * @param complexRegularExpression the complex regular expression (used ${AND} and ${NOT} to chain expressions
     *                                 together}
     * @return true if source value matches complex regular
     */
    public static boolean matches(String sourceValue, String complexRegularExpression) {
        //check for null
        if (sourceValue == null) {
            if (complexRegularExpression == null)
                return true;
            else
                return false;
        }

        try {
            //test source contains AND
            int startOrIndex = complexRegularExpression.indexOf(OR);

            if (startOrIndex > -1) {
                if (startOrIndex == 0) {
                    if (!matches(sourceValue, complexRegularExpression.substring(OR.length(),
                            complexRegularExpression.length())))
                        return false;
                }

                //split AND first only
                int endIndex = startOrIndex + OR.length();


                String leftExpr = complexRegularExpression.substring(0, startOrIndex);
                String rightExpr = complexRegularExpression.substring(endIndex, complexRegularExpression.length());

                return matches(sourceValue, leftExpr) || matches(sourceValue, rightExpr);

            } else {
                //test source contains AND
                int startAndIndex = complexRegularExpression.indexOf(AND);

                if (startAndIndex > -1) {
                    if (startAndIndex == 0) {
                        if (!matches(sourceValue, complexRegularExpression.substring(AND.length(),
                                complexRegularExpression.length())))
                            return false;
                    }

                    //split AND first only
                    int endIndex = startAndIndex + AND.length();


                    String leftExpr = complexRegularExpression.substring(0, startAndIndex);
                    String rightExpr = complexRegularExpression.substring(endIndex, complexRegularExpression.length());

                    return matches(sourceValue, leftExpr) && matches(sourceValue, rightExpr);

                } else {
                    int notIndex = complexRegularExpression.indexOf(NOT);

                    if (notIndex > -1) {
                        String notRegExp = complexRegularExpression.substring(notIndex + NOT.length(),
                                complexRegularExpression.length());

                        //return not match
                        return !matches(sourceValue, notRegExp);
                    }
                }//end else not
            }//end else and or not
        } catch (RuntimeException e) {
            throw new SystemException("complexRegularExpression=" + complexRegularExpression + " sourceValue=" + sourceValue + " " + Debugger.stackTrace(e));
        }


        return matchesRE(complexRegularExpression, sourceValue);


    }


    /**
     * @param aOld   the string to be replaced
     * @param aNew   the new string
     * @param aInput the input string
     * @return the replaced string
     */

    public static String replace(String aOld, String aNew, String aInput) {

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
     * This method is used to convert an integer to roman numeral.
     * <p>
     * If the return value is " " then the integer was not valid. (1-4999)
     *
     * @param input An integer to convert to Roman numeral.
     * @return String (Roman numeral)
     */

    public static String toRomanNumber(int input) {

        //Determines if the integer is in the range for 1-39999 to be able to
        // convert.

        if (input > 0 && input < 40000) {

            //An initialization of an empty string to store the roman numerals.

            StringBuilder strValue = new StringBuilder();

            //An array of strings stating the unique Roman sequences.

            //An array of integers corresponding to the Roman numeral sequences
            // respectively.

            String roNumStr[] = {
                    "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M",
                    "ME", "E", "MG", "G"};

            int roNumInt[] = {
                    1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000, 4000, 5000,
                    9000, 10000};

            //A counted loop going through the arrays from the highest element to
            // the lowest.

            for (int i = roNumStr.length - 1; i >= 0; i--) {

                //This compares the contents in the element number of the loop to
                // the input value (or what is left of the input value)

                while (input >= roNumInt[i]) {

                    //The number that was compared is subtracted from the input
                    // value and add the string that is respected to the compared
                    // integer is added to the final string.

                    input -= roNumInt[i];

                    strValue.append(roNumStr[i]);

                }

            }

            return strValue.toString(); // Returns the roman numeral string value.

        } else
            return " "; // Returns " " because of the invalid input.

    }

    /**
     * This method is used to convert Roman numerals to an integer.
     *
     * @param input An integer to convert to Roman numerals.
     * @param valid A boolean to determine that states if the integer is valid or
     *              invalid.
     * @return Integer value of the Roman numeral.
     */

    public static int toIntegerFromRoman(String input, boolean valid) {

        if (input == null) {

            return 0;

        }

        //Checks to see if the input was valid.

        if (valid) {

            //Initializing the resultant integer.

            int intValue = 0;

            //An array of strings stating the roman numerals.

            //An array of integers corresponding to the roman numerals
            // respectively.

            char roNumStr[] = {
                    'I', 'V', 'X', 'L', 'C', 'D', 'M', 'E', 'G'};

            int roNumInt[] = {
                    1, 5, 10, 50, 100, 500, 1000, 5000, 10000};

            //A nested counted loop comparing the character of the string from
            // right to

            //left to the roman numerals.

            for (int i = input.length() - 1; i >= 0; i--) {

                for (int j = roNumStr.length - 1; j >= 0; j--) {

                    if (input.charAt(i) == roNumStr[j]) {

                        //Adds the value to the total.

                        intValue += roNumInt[j];

                        /* 4 or 9 conditions */

                        //Checks if the character is I, X, C, or M because they are
                        // they only Roman numerals to determine if part of the value
                        // will be 4*10^x or 9*10^x.
                        //Checks to see if the character is not in the last position
                        // because the last position doesn't determine 4*10^x or
                        // 9*10^x.
                        //Checks the character to the right to see if that character
                        // is higher by 5*10^x or 1*10^(x+1) assuming x is the
                        // character's position in an integer number from right to
                        // left minus one.
                        //If it is, then it needs to be subtracted and since the
                        // value has been already added, the value needs to be
                        // subtracted twice.
                        if (i < input.length() - 1 && j < roNumStr.length - 1
                                && j % 2 == 0) {

                            if (input.charAt(i + 1) == roNumStr[j + 1]
                                    || input.charAt(i + 1) == roNumStr[j + 2])
                                intValue -= roNumInt[j] * 2;

                        }

                        break; //Breaks because the letter has been chosen.

                    }

                }

            }

            return intValue; // Returns the roman numeral integer value if the
            // value is still valid.

        } else
            return 0; // Returns zero to indicate that the number is invalid.

    }

    /**
     * @param aInput the input string
     * @return the input stream
     */
    public static InputStream toInputStream(String aInput) {
        if (aInput == null) {
            return null;
        }

        return new BufferedInputStream(
                new ByteArrayInputStream(aInput.getBytes(IO.CHARSET)));

    }

    /**
     * Inserts values from the object template into the Bind Str
     * <p>
     * For example if bindStr "Today is ${DAY}" and bindPara DAY="Monday"
     * <p>
     * results in a string "Today is Monday"
     * <p>
     * Default dateFormat MM/dd/yyyy
     * <p>
     * Functions
     * &lt;#if x = 1&gt;
     * x is 1
     * &lt;#elseif x = 2&gt;
     * x is 2
     * &lt;#elseif x = 3&gt;
     * <p>
     * x is 3
     * &lt;#elseif x = 4&gt;
     * x is 4
     * &lt;#else&gt;
     * x is not 1 nor 2 nor 3 nor 4
     * &lt;/#if&gt;
     * <p>
     * &lt;#if Tithes_1?exists&gt;
     * OK
     * &lt;#else&gt;
     * &lt;#assign Tithes_1= 0&gt;
     * &lt;/#if&gt;
     * <p>
     * Date formating ${lastUpdated?string("yyyy-MM-dd HH:mm:ss zzzz")}
     *
     * @param aBindText the bind text
     * @param aBindMap  values to insert into the bind text
     * @return the text the format
     * @throws FormatException the format exception
     */
    public static String format(String aBindText, Object aBindMap)
            throws FormatException {
        return Text.format(aBindText, aBindMap, DATE_FORMAT);

    }

    /**
     * Format a given array
     *
     * @param bindText ex: Hello # ${0}
     * @param inputs   {"Green", "Red"}
     * @return format response ex: Hello # Green
     * @throws FormatException when a format exception
     */
    public static String formatArray(String bindText, Object[] inputs)
            throws FormatException {
        if (inputs == null)
            return bindText;


        HashMap<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < inputs.length; i++) {
            map.put(String.valueOf(i), inputs[i]);
        }

        return format(bindText, map);

    }

    public static String formatDate(LocalDate date) {
        return formatDate(date, Text.DATE_FORMAT);
    }

    /**
     * The formatted date
     *
     * @param date the date to format
     * @return Formatted date based on default data format
     */
    public static String formatDate(LocalDateTime date) {
        return formatDate(date, Text.DATE_FORMAT);
    }

    public static String formatDate(TemporalAccessor date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

        return formatter.format(date);
    }

    /**
     * Formats a java.util.Date object in the standard format
     * for the application.
     *
     * @param date the date to format
     * @return the format date
     */
    public static String formatDate(Date date) {

        return formatDate(DATE_FORMAT, date);

    }

    public static String formatDate(Calendar date) {

        if (date == null)
            return "";

        return formatDate(DATE_FORMAT, date.getTime());
    }

    public static String formatDate(String format, LocalDate date) {
        return formatDate(format, Scheduler.toLocalDateTime(date));
    }

    /**
     * Formats a java.util.Date object into the provided format
     * <p>
     * Letter  Date or Time Component  Presentation  Examples
     * G  Era designator  Text  AD
     * y  Year  Year  1996; 96
     * M  Month in year  Month  July; Jul; 07
     * w  Week in year  Number  27
     * W  Week in month  Number  2
     * D  Day in year  Number  189
     * d  Day in month  Number  10
     * F  Day of week in month  Number  2
     * E  Day in week  Text  Tuesday; Tue
     * a  Am/pm marker  Text  PM
     * H  Hour in day (0-23)  Number  0
     * k  Hour in day (1-24)  Number  24
     * K  Hour in am/pm (0-11)  Number  0
     * h  Hour in am/pm (1-12)  Number  12
     * m  Minute in hour  Number  30
     * s  Second in minute  Number  55
     * S  Millisecond  Number  978
     * z  Time zone  General time zone  Pacific Standard Time; PST; GMT-08:00
     * Z  Time zone  RFC 822 time zone  -0800
     *
     * @param format the data format
     * @param date   the date to format
     * @return formatted
     */
    public static String formatDate(String format, Date date) {
        if (date == null)
            return "";

        if (format == null || format.isEmpty()) {
            return date.toString();

        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    public static String formatDate(String text) {
        return formatDate(DATE_FORMAT, toDate(text));

    }

    public static String formatFromTemplate(String aTemplateName,
                                            Map<Object, Object> aBindMap, Locale aLocale)
            throws IOException {

        return format(Text.loadTemplate(aTemplateName, aLocale), aBindMap);

    }


    /**
     * Inserts values from the object template into the Bind Str
     * <p>
     * For example if bindStr "Today is ${DAY}" and bindPara DAY="Monday"
     * <p>
     * results in a string "Today is Monday"
     *
     * @param bindText   the bind text ex: Today is ${DAY}" and bindPara DAY="Monday
     * @param bindObj    the map of values to insert into the bind text
     * @param dateFormat i.e. MM/dd/yyyy
     * @return the formatted text
     * @throws FormatException when a format error occurs
     */

    public static String format(String bindText, Object bindObj,
                                String dateFormat)
            throws FormatException {
        return getTextStyles().format(bindText, bindObj, dateFormat);
    }

    /**
     * Format text replacing place-holders prefixed with ${ and suffixed by }
     * with the corresponding values in the map.
     *
     * @param bindText the text to format
     * @param map      the key/values
     * @return the formatted text
     * @throws FormatException when  formatting error occurs
     */
    public static String formatMap(String bindText, Map<String, String> map)
            throws FormatException {
        return format(bindText, map);
    }

    public static String formatMap(String bindText, Object... keyValues)
            throws FormatException {
        return format(bindText, Organizer.toMap(keyValues));
    }

    /**
     * write formatted output to the writer
     *
     * @param aBindMap the placeholder key/value pairs
     * @param text     the text to format
     * @param writer   the writer to send formatted output
     * @throws IOException     when IO error occurs
     * @throws FormatException a format error occurs
     */
    public static void formatWriter(String text, Object aBindMap, Writer writer)
            throws IOException, FormatException {
        formatWriter(text, aBindMap, null, writer);
    }

    /**
     * write formatted output to the writer
     *
     * @param aBindMap   the placeholder name/value map
     * @param text       the text to format
     * @param dateFormat the date format
     * @param writer     the writer to write the formatted output
     * @throws IOException     when IO error occurs
     * @throws FormatException when formatting exception occurs
     */
    public static void formatWriter(String text, Object aBindMap, String dateFormat, Writer writer)
            throws IOException, FormatException {
        getTextStyles().formatWriter(text, aBindMap, dateFormat, writer);
    }

    public static void formatWriterFromTemplateName(String templateName, Object aBindMap, Writer writer)
            throws IOException, FormatException {

        getTextStyles().formatWriter(new File(settings().getProperty(TEMPLATE_DIR_PROP_NM)), templateName, aBindMap, null
                , writer);
    }

    public static void formatWriter(File templateDir, String templateName, Object aBindMap, String dateFormat,
                                    Writer writer)
            throws IOException, FormatException {
        getTextStyles().formatWriter(templateDir, templateName, aBindMap, dateFormat, writer);
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
     * @param templateName the template name
     * @return the loaded template
     * @throws IOException when an IO
     */
    public static String loadTemplate(String templateName)
            throws IOException {
        return loadTemplate(templateName, null);
    }

    /**
     * @param templateName the template
     * @param locale     the locale
     * @return @throws
     * @throws IOException then the template cannot be loaded
     */
    public static String loadTemplate(String templateName, Locale locale)
            throws IOException {

        if (templateName == null)

            throw new IllegalArgumentException(
                    "aTemplateNM, aLocale required in Text");

        if (locale == null)

            locale = Locale.getDefault();

        String lang = locale.getLanguage();
        String country = locale.getCountry();

        if (Text.isNull(country))
            country = settings().getProperty(TEMPLATE_LOCALE_COUNTRY, Locale.getDefault().getCountry());
        if (Text.isNull(lang))
            lang = settings().getProperty(TEMPLATE_LOCALE_LANGUAGE, Locale.getDefault().getLanguage());

        var templateDir = settings().getProperty(TEMPLATE_DIR_PROP_NM, "");

        if (!templateDir.isEmpty()) {
            StringBuilder templatePath = new StringBuilder(templateDir).append("/").append(templateName);

            //Append locale information
            File file = new File(templatePath.toString());
            if (!file.exists()) {
                templatePath.append("_").append(country.toLowerCase())
                        .append("_").append(lang.toLowerCase())
                        .append(settings().getProperty(TEMPLATE_EXTENSION_PROP_NM, ".txt"));
            }

            return IO.reader().readTextFile(templatePath.toString());
        } else {
            //use class path
            String path = new StringBuilder(TEMPLATE_CLASSPATH_ROOT)
                    .append("/").append(templateName).append(TEMPLATE_EXTENSION).toString();

            return IO.reader().readClassPath(path);
        }


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
     * @param val the input string
     * @return true the inputted string is an integer
     */
    public static boolean isInteger(String val) {
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
     * @param aObject the object to convert
     * @return to string version of object
     */
    public static String toString(Object aObject) {
        if (aObject == null)
            return "";

        if (aObject instanceof Date) {
            return Text.formatDate((Date) aObject);
        } else if (aObject instanceof Calendar) {
            return Text.formatDate((Calendar) aObject);
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
            try {
                return IO.reader().readText((InputStream) aObject);
            } catch (IOException e) {
                throw new SystemException(Debugger.stackTrace(e));
            }

        }


        return aObject.toString();
    }

    /**
     * Determine whether the given object is an number (double, number) or can be
     * converted to an number.
     *
     * @param aObject the object to test
     * @return true is an instance of Number or a PARSE-ABLE number
     */
    public static boolean isNumber(Object aObject) {
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

    public static <T> String mergeArray(String separator, T[] merging) {
        if (merging == null || merging.length == 0)
            return null;

        StringBuilder text = new StringBuilder();

        if (separator == null)
            separator = "";

        for (Object merge : merging) {
            if (!text.isEmpty() && separator != null)
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

    /**
     * Example usage:
     * <p>
     * String path = "templates/test.txt";
     * String results = Text.formatTextFromClassPath(path,map);
     *
     * @param path the class path (ex: /templates/test.txt)
     * @param map  the bind variable map
     * @return the formatted template
     * @throws IOException when the template path cannot be read
     */
    public static String formatTextFromClassPath(String path, Map<?, ?> map)
            throws IOException {
        String template = IO.reader().readClassPath(path);
        return format(template, map);
    }

    public static LocalDateTime toLocalDateTime(String text) {
        return toLocalDateTime(text, DATETIME_FORMAT);
    }

    public static LocalDateTime toLocalDateTime(String text, String format) {
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(text, df);
        } catch (DateTimeParseException e) {
            throw new FormatException(e.getMessage() + " FORMAT:" + format, e);
        }
    }

    /**
     * @param text   the data text
     * @param format the data Format ex: M/dd/yyy
     * @return the locate date
     */
    public static LocalDate toLocalDate(String text, String format) {
        if (text == null || text.isEmpty())
            return null;

        try {
            if (format == null || format.isEmpty())
                format = DATE_FORMAT;

            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(text, df);
        } catch (DateTimeParseException e) {
            throw new FormatException(e.getMessage() + " FORMAT:" + format, e);
        }
    }


    /**
     * Based on <a href="https://www.baeldung.com/java-random-string">baeldung.com</a>
     *
     * @param targetStringLength the text lenght
     * @return the generated id
     */
    public static String generateAlphabeticId(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
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

    public static String toProperCase(String text) {
        if (text == null)
            return null;

        if (text.length() == 1)
            return text.toUpperCase();

        return new StringBuilder(text.length())
                .append(
                        Character.toString(text.charAt(0)).toUpperCase()
                ).append(text.substring(1).toLowerCase()).toString();
    }

    public static String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(IO.CHARSET));
    }

    public static String trim(String text, char character) {
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
}
