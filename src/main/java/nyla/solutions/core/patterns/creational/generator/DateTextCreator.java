package nyla.solutions.core.patterns.creational.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * Generates a text with the date in the given format
 *
 * @author Gregory Green
 */
public class DateTextCreator implements CreatorTextable
{
    private final DateTimeFormatter dateTimeFormatter;
    public DateTextCreator(DateTimeFormatter dateTimeFormatter)
    {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public DateTextCreator()
    {
        this(DateTimeFormatter.ISO_DATE);
    }

    @Override
    public String create()
    {
        return LocalDate.now().format(dateTimeFormatter);
    }

    public DateTimeFormatter getDateTimeFormatter()
    {
        return dateTimeFormatter;
    }
}
