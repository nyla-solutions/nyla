package nyla.solutions.core.util.text;

import nyla.solutions.core.exception.FormatException;
import nyla.solutions.core.util.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Text date utility
 * @author Gregory Green
 */
public class TextDates {


    public  LocalDateTime toLocalDateTime(String text) {
        return toLocalDateTime(text, Text.DATETIME_FORMAT);
    }

    public LocalDateTime toLocalDateTime(String text, String format) {
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
    public LocalDate toLocalDate(String text, String format) {
        if (text == null || text.isEmpty())
            return null;

        try {
            if (format == null || format.isEmpty())
                format = Text.DATE_FORMAT;

            DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(text, df);
        } catch (DateTimeParseException e) {
            throw new FormatException(e.getMessage() + " FORMAT:" + format, e);
        }
    }
}
