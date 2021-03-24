package nyla.solutions.core.patterns.creational.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Gregory Green
 */
public class DateTextCreator implements nyla.solutions.core.patterns.creational.Creator<String>
{
    private final DateTimeFormatter dateTimeFormatter;
    public DateTextCreator(DateTimeFormatter dateTimeFormatter)
    {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public String create()
    {
        return LocalDate.now().format(dateTimeFormatter);
    }
}
