package nyla.solutions.core.util.text;

import java.util.StringTokenizer;

/**
 * @author Gregory Green
 */
public class TextBytes {


    public String toByteText(byte[] aByte) {

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

    public byte[] toBytesFromByteText(String aByteText) {

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

}
