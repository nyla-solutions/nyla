package nyla.solutions.core.util.text;

import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.io.IO;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Organizer;
import nyla.solutions.core.util.Scheduler;
import nyla.solutions.core.util.Text;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static nyla.solutions.core.util.Config.settings;


/**
 *  * <b>TextFormatter</b> includes template engine support
 *  * like Free Marker that builds composite strings/values dynamically at runtime
 *  *  (see <a href="http://freemarker.sourceforge.net/">http://freemarker.sourceforge.net/</a>). There are also methods to support complex
 *  *  regular expressions with Boolean AND, OR and NOT logic, numerous string conversions,
 *  *  general text manipulation and parsing methods.
 *
 * @author Gregory Green
 */
public class TextFormatter {

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
    public String formatTextFromClassPath(String path, Map<?, ?> map)
            throws IOException {
        String template = IO.reader().readClassPath(path);
        return formatTemplate(template, map);
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
     * @param bindTemplate the bind text
     * @param bindMap  values to insert into the bind text
     * @return the text the format
     * @throws FormatException the format exception
     */
    public String formatTemplate(String bindTemplate, Object bindMap)
            throws FormatException {
        return formatTemplate(bindTemplate, bindMap, Text.DATE_FORMAT);

    }

    /**
     * Format a given array
     *
     * @param bindText ex: Hello # ${0}
     * @param inputs   {"Green", "Red"}
     * @return format response ex: Hello # Green
     * @throws FormatException when a format exception
     */
    public String formatArray(String bindText, Object[] inputs)
            throws FormatException {
        if (inputs == null)
            return bindText;


        HashMap<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < inputs.length; i++) {
            map.put(String.valueOf(i), inputs[i]);
        }

        return formatTemplate(bindText, map);

    }

    public String formatDate(LocalDate date) {
        return formatDate(date, Text.DATE_FORMAT);
    }

    /**
     * The formatted date
     *
     * @param date the date to format
     * @return Formatted date based on default data format
     */
    public String formatDate(LocalDateTime date) {
        return formatDate(date, Text.DATE_FORMAT);
    }

    public String formatDate(TemporalAccessor date, String format) {
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
    public String formatDate(Date date) {

        return formatDate(Text.DATE_FORMAT, date);

    }

    public String formatDate(Calendar date) {

        if (date == null)
            return "";

        return formatDate(Text.DATE_FORMAT, date.getTime());
    }

    public String formatDate(String format, LocalDate date) {
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
    public String formatDate(String format, Date date) {
        if (date == null)
            return "";

        if (format == null || format.isEmpty()) {
            return date.toString();

        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }


    /**
     * @param aTokenizer the string token object
     * @return the array of tokens
     */
    public String[] toStrings(StringTokenizer aTokenizer) {
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
    public Date toDate(String text) {
        return toDate(text,Text.DATE_FORMAT);

    }


    public String toProperCase(String text) {
        if (text == null)
            return null;

        if (text.length() == 1)
            return text.toUpperCase();

        return new StringBuilder(text.length())
                .append(
                        Character.toString(text.charAt(0)).toUpperCase()
                ).append(text.substring(1).toLowerCase()).toString();
    }


    /**
     * @param templateName the template name
     * @return the loaded template
     * @throws IOException when an IO
     */
    public String loadTemplate(String templateName)
            throws IOException {
        return loadTemplate(templateName, null);
    }

    public String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(IO.CHARSET));
    }


    /**
     * @param templateName the template
     * @param locale     the locale
     * @return @throws
     * @throws IOException then the template cannot be loaded
     */
    public String loadTemplate(String templateName, Locale locale)
            throws IOException {

        if (templateName == null)

            throw new IllegalArgumentException(
                    "aTemplateNM, aLocale required in Text");

        if (locale == null)

            locale = Locale.getDefault();

        String lang = locale.getLanguage();
        String country = locale.getCountry();

        if (Text.isNull(country))
            country = settings().getProperty(Text.TEMPLATE_LOCALE_COUNTRY, Locale.getDefault().getCountry());
        if (Text.isNull(lang))
            lang = settings().getProperty(Text.TEMPLATE_LOCALE_LANGUAGE, Locale.getDefault().getLanguage());

        var templateDir = settings().getProperty(Text.TEMPLATE_DIR_PROP_NM, "");

        if (!templateDir.isEmpty()) {
            StringBuilder templatePath = new StringBuilder(templateDir).append("/").append(templateName);

            //Append locale information
            File file = new File(templatePath.toString());
            if (!file.exists()) {
                templatePath.append("_").append(country.toLowerCase())
                        .append("_").append(lang.toLowerCase())
                        .append(settings().getProperty(Text.TEMPLATE_EXTENSION_PROP_NM, ".txt"));
            }

            return IO.reader().readTextFile(templatePath.toString());
        } else {
            //use class path
            String path = new StringBuilder(Text.TEMPLATE_CLASSPATH_ROOT)
                    .append("/").append(templateName).append(Text.TEMPLATE_EXTENSION).toString();

            return IO.reader().readClassPath(path);
        }


    }

    /**
     * Convert a text string to a date instance
     *
     * @param aText      the text to convert
     * @param dateFormat the date format
     * @return date version of text
     */
    public Date toDate(String aText, String dateFormat) {
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

    public String formatDate(String text) {
        return formatDate(Text.DATE_FORMAT, toDate(text));

    }

    public String formatFromTemplate(String aTemplateName,
                                            Map<Object, Object> aBindMap, Locale aLocale)
            throws IOException {

        return formatTemplate(loadTemplate(aTemplateName, aLocale), aBindMap);

    }


    /**
     * @param object to convert collection of String to array
     * @return the array of strings
     */
    @SuppressWarnings("unchecked")
    public String[] toStrings(Object object) {
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
    public String[] toStrings(Collection<Object> collection) {
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


    /**
     * This method is used to convert an integer to roman numeral.
     * <p>
     * If the return value is " " then the integer was not valid. (1-4999)
     *
     * @param input An integer to convert to Roman numeral.
     * @return String (Roman numeral)
     */

    public String toRomanNumber(int input) {

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

    public int toIntegerFromRoman(String input, boolean valid) {

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
    public InputStream toInputStream(String aInput) {
        if (aInput == null) {
            return null;
        }

        return new BufferedInputStream(
                new ByteArrayInputStream(aInput.getBytes(IO.CHARSET)));

    }

    public Collection<String> transformTexts(Function<String, String> function, String... collection) {
        return transform(function,asList(collection));
    }
    public Collection<String> transform(Function<String, String> function, Collection<String> collection) {
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


    public Collection<String> toUpperCase(Collection<String> collection) {
        Function<String, String> function = text -> text != null ? text.toUpperCase() : text;

        return transform(function,collection);

    }

    public <T> String toText(Collection<T> collection, String separator) {
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
     * @param lines tokens separated by \t\n\r
     * @return tokens separated by \n (newline)
     */
    public Set<String> toSet(String[] lines) {
        return new TreeSet<String>(asList(lines));
    }


    /**
     * @param aValue the value
     * @param aType  the type of the value
     * @return value cast to the object type instance
     */
    public Object toObject(String aValue, String aType) {
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

    public String formatTemplate(String bindText, Object bindObj,
                                 String dateFormat)
            throws FormatException {
        return Text.styles().format(bindText, bindObj, dateFormat);
    }


    /**
     * Loop thrus map to formats any needed value with data from other properties.
     * <p>
     * See Text.format(String,Map)
     *
     * @param map the map to format
     * @throws FormatException when format error occurs
     */
    public void formatMap(Map<Object, Object> map)
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
                map.put(key, Text.format().formatTemplate(text, map));
        }

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
    public String formatMap(String bindText, Map<String, String> map)
            throws FormatException {
        return formatTemplate(bindText, map);
    }

    public String formatMap(String bindText, Object... keyValues)
            throws FormatException {
        return formatTemplate(bindText, Organizer.toMap(keyValues));
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
    public void formatWriter(String text, Object aBindMap, Writer writer)
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
    public void formatWriter(String text, Object aBindMap, String dateFormat, Writer writer)
            throws IOException, FormatException {
        Text.styles().formatWriter(text, aBindMap, dateFormat, writer);
    }

    public void formatWriterFromTemplateName(String templateName, Object aBindMap, Writer writer)
            throws IOException, FormatException {

        Text.styles().formatWriter(new File(settings().getProperty(Text.TEMPLATE_DIR_PROP_NM)), templateName, aBindMap, null
                , writer);
    }

    public void formatWriter(File templateDir, String templateName, Object aBindMap, String dateFormat,
                                    Writer writer)
            throws IOException, FormatException {
        Text.styles().formatWriter(templateDir, templateName, aBindMap, dateFormat, writer);
    }


    /**
     * @param doubleText the double text
     * @return formatted currency
     */
    public String formatCurrency(String doubleText) {
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
    public String formatCurrency(double number) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        return numberFormat.format(number);
    }

    /**
     * Used the"#,##0.0###" formatting for a given number
     *
     * @param aNumber the number to format
     * @return formatted number
     */
    public String formatDouble(String aNumber) {
        return formatDouble(aNumber, "#,##0.0###");
    }

    /**
     * @param number the number to format
     * @param format the format
     * @return formatted number
     */
    public String formatDouble(String number, String format) {
        if (number == null || number.isEmpty())
            return "";

        return formatDouble(Double.valueOf(number), format);

    }

    /**
     * @param number the double number to format
     * @return formatted number
     */
    public String formatDouble(double number) {
        return formatDouble(number, "#,##0.0###");
    }

    /**
     * @param number the number to format
     * @param format the format
     * @return formatted number
     */
    public String formatDouble(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);

    }

    /**
     * @param aNumber the number to format
     * @return formatted number
     */
    public String formatNumber(String aNumber) {

        return formatDouble(aNumber);
    }

    /**
     * @param number the double number
     * @return formatted number
     */
    public String formatNumber(double number) {
        return formatDouble(number);
    }

    /**
     * @param number the number to format
     * @param format the format of the number
     * @return formatted number
     */
    public String formatNumber(double number, String format) {

        return formatDouble(number, format);
    }

    /**
     * @param number the percentage number to format
     * @return formatted number
     */
    public String formatPercent(String number) {
        if (number == null || number.isEmpty())
            return "";

        return formatPercent(Double.valueOf(number).doubleValue());
    }

    /**
     * @param number
     * @return the formatted percent
     */
    public String formatPercent(double number) {
        return formatNumber(number, "###.##'%'");
    }

}
