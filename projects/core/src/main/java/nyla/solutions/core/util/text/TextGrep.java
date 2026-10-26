package nyla.solutions.core.util.text;

import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class TextGrep {



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
    public String grepText(String matchRegExp, String text) {
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

    public Collection<String> grepAllTexts(String matchRegExp, String text) {
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
}
