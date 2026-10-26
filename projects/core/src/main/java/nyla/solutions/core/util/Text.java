package nyla.solutions.core.util;

import nyla.solutions.core.exception.SetupException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.patterns.decorator.BasicTextStyles;
import nyla.solutions.core.patterns.decorator.TextStyles;
import nyla.solutions.core.util.text.*;

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
    private static final TextSplitter textSplitter = new TextSplitter();
    private static final TextGrep textGrep = new TextGrep();

    private static final TextCounter textCounter = new TextCounter();

    /**
     * TEMPLATE_DIR_PROP_NM = Text.class.getName()+".template.dir"
     */
    public static final String TEMPLATE_DIR_PROP_NM = Text.class.getName() + ".template.dir";


    /**
     * TEMPLATE_EXTENSION_PROP_NM = Text.class.getName()+".template.extension"
     */
    public static final String TEMPLATE_EXTENSION_PROP_NM = Text.class.getName() + ".template.extension";


    /**
     *
     * @return the Text grep instance
     */
    public static TextGrep textGrep()
    {
        return textGrep;
    }
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


    public static TextCounter count(){
        return textCounter;
    }

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

    public static TextChecker check() {
        return textChecker;
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

    public static TextSplitter split() {
        return textSplitter;
    }
}
