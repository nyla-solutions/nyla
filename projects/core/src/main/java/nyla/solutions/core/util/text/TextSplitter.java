package nyla.solutions.core.util.text;

import nyla.solutions.core.exception.RequiredException;
import nyla.solutions.core.operations.ClassPath;
import nyla.solutions.core.util.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TextSplitter {

    /**
     * Split a single text into many determined by the separation character
     *
     * @param text the text to split
     * @param re    the regular expression separation character
     * @return the array of the split strings
     */
    public String[] splitRE(String text, String re) {
        if (text == null || text.isEmpty())
            return null;

        if (re == null)
            throw new RequiredException("regular expression");

        return text.split(re);
    }

    public int[] splitToInts(String aText) {
        if (aText == null || aText.isEmpty())
            return null;

        StringTokenizer toks = new StringTokenizer(aText, " \t\n\r");


        int[] ints = new int[toks.countTokens()];

        int i = 0;
        while (toks.hasMoreTokens()) {
            ints[i++] = Integer.parseInt(toks.nextToken());
        }
        return ints;
    }


    /**
     * Split a single text into many determined by the separation character
     *
     * @param text  the text to split
     * @param token the separation character
     * @return the array of the split strings
     */
//    public String[] split(String text, String token) {
//        if (text == null)
//            return null;
//
//        if (token == null) {
//            return new String[]{text};
//        }
//
//        int i = text.indexOf(token);
//
//        if (i < 0) {
//            //nothing to replace
//
//            String[] results = {text};
//            return results;
//
//        }
//
//        ArrayList<String> resultList = new ArrayList<String>(16);
//        int start = 0;
//
//
//        do {
//
//            //split all instances
//            if (start != i) {
//                resultList.add(text.substring(start, i));
//            }
//
//            start = i + token.length();
//            i = text.indexOf(token, start);
//
//        }
//
//        while (i >= 0);
//
//        //append rest of string
//
//        if (start < text.length())
//
//            resultList.add(text.substring(start, text.length()));
//
//        if (resultList.isEmpty()) {
//            throw new IllegalArgumentException("Unexpected empty list");
//        }
//
//        resultList.trimToSize();
//
//        String[] results = new String[resultList.size()];
//
//        resultList.toArray(results);
//
//        return results;
//    }

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
//    public <T> T[] splitRE(String line, String re, Class<T> class1) {
//        String[] text = splitRE(line, re);
//
//
//        if (text == null || text.length == 0)
//            return null;
//
//        T[] results = (T[]) Array.newInstance(class1, text.length);
//        for (int i = 0; i < text.length; i++) {
//            results[i] = ClassPath.newInstance(class1, text[i]);
//        }
//        return results;
//    }



    /**
     * Split a single text into many determined by the separation character
     *
     * @param aText the text to split
     * @return the array of the split strings
     */
//    public String[] split(String aText) {
//        if (aText == null || aText.isEmpty())
//            return null;
//
//        StringTokenizer toks = new StringTokenizer(aText, " \t\n\r");
//
//
//        return Text.format().toStrings(toks);
//    }


    /**
     * Split a single text into many determined by the separation character
     *
     * @param aText the text to split
     * @return the array of the split strings
     */
    public  String[] split(String aText) {
        if (aText == null || aText.isEmpty())
            return null;

        StringTokenizer toks = new StringTokenizer(aText, " \t\n\r");


        return Text.format().toStrings(toks);
    }

    /**
     * Split a single text into many determined by the separation character
     *
     * @param text  the text to split
     * @param token the separation character
     * @return the array of the split strings
     */
    public String[] split(String text, String token) {
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
    public <T> T[] splitRE(String line, String re, Class<T> class1) {
        String[] text = splitRE(line, re);


        if (text == null || text.length == 0)
            return null;

        T[] results = (T[]) Array.newInstance(class1, text.length);
        for (int i = 0; i < text.length; i++) {
            results[i] = ClassPath.newInstance(class1, text[i]);
        }
        return results;
    }
}
