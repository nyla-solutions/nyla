package nyla.solutions.core.util.text;

import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.util.regex.Pattern;

/**
 *
 * @author Gregory Green
 */
public class TextMatcher {

    /**
     * Complex matches and
     */
    private static final String AND = "${AND}";

    /**
     * Complex matches ${NOT}
     */
    public static final String NOT = "${NOT}";
    /**
     * Complex matches ${OR}
     */
    public static final String OR = "${OR}";

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
    public boolean matches(String sourceValue, String complexRegularExpression) {
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
}
