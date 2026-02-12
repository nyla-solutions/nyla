package nyla.solutions.core.util.text;

import nyla.solutions.core.exception.IoException;
import nyla.solutions.core.exception.SystemException;
import nyla.solutions.core.util.Debugger;
import nyla.solutions.core.util.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;


/**
 * Text parser utility
 * @author Gregory Green
 */
public class TextParser {


    /**
     *
     */
    private int indexOf(String aContent, String aSearchText, int aFromIndex) {
        if (Text.SPECIAL_START.equals(aSearchText)) {
            return aFromIndex;
        } else if (Text.SPECIAL_END.equals(aSearchText)) {
            return aContent.length();
        }


        return aContent.indexOf(aSearchText, aFromIndex);
    }

    /**
     * @param content the text content
     * @param searchText the search text
     * @return indexOf(aContent, aSearchText, 0)
     */
    private int indexOf(String content, String searchText) {
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
    public String parseText(String aContent, String aStart, String aEnd) {
        Collection<String> results = parse(aContent, aStart, aEnd);

        if (results == null || results.isEmpty()) {
            return "";
        }

        return results.iterator().next().toString();
    }

    public String parseText(Reader aContent, String aStart, String aEnd) {
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
    public Collection<String> parse(String aContent, String aStart, String aEnd) {
        if ((aStart == null || aStart.isEmpty()) && (aEnd == null || aEnd.isEmpty()))
            return asList(aContent);

        return parse(aContent, aStart, aEnd, false);
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
    public String parseRE(String text, String startRE, String endRE) {
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
     * Parse all string surrounded by the aStart and aEnd tag
     * aIgnoreCase = false
     *
     * @param aContent the content to parse
     * @param aStart   the content start tag
     * @param aEnd     the content end tag
     * @return the collection of parsed objects
     * @throws IOException when an IO error occurs
     */
    public Collection<Object> parse(Reader aContent, String aStart, String aEnd)
            {
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
    public Collection<String> parse(String content, String start, String end, boolean ignoreCase) {
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
        if (Text.SPECIAL_START.equals(start)) {
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

    public Collection<Object> parse(Reader reader, String start, String end, boolean ignoreCase)
             {
        BufferedReader bufferedReader;

        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }

        String content;
        String compareContent;
        ArrayList<Object> results = new ArrayList<>();
        try {
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
                if (Text.SPECIAL_START.equals(start)) {
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
        catch(IOException e)
        {
            throw new IoException(e);
        }
    }

}
