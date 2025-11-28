package nyla.solutions.core.util.text;

import nyla.solutions.core.util.Text;

import java.util.Calendar;
import java.util.Random;

/**
 * @author Gregory Green
 */
public class TextGenerator {

    private final Random random = new Random(Calendar.getInstance().getTime().getTime());

    public String fixedLength(int number, int length) {
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

    public String fixedLength(String text, int length) {
        StringBuilder sb = new StringBuilder(length);

        sb.append(text);
        sb.setLength(length);
        return sb.toString();

    }

    public String fixedLength(String text, int length, char fillChar) {

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
     * Based on <a href="https://www.baeldung.com/java-random-string">baeldung.com</a>
     *
     * @param targetStringLength the text lenght
     * @return the generated id
     */
    public String generateAlphabeticId(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    /**
     * Generate a unique ID
     * @return the generated ID
     */
    public String generateId() {
        var date = Calendar.getInstance().getTime();

        var dateId = Text.format().formatDate("yyyyMMddHHmmssSS", date);

        return dateId + random.nextInt();

    }

    private final String[] fixedNumberPrefixLookup =
            {
                    "0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000"
            };
}
