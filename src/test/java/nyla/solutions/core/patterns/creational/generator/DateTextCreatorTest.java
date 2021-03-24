package nyla.solutions.core.patterns.creational.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DateTextCreatorTest
{
    @Test
    public void test_create_generates_date_text()
    {
        DateTextCreator subject = new DateTextCreator(DateTimeFormatter.ISO_DATE);
        String actual = subject.create();
        assertNotNull(LocalDate.parse(actual,DateTimeFormatter.ISO_DATE));
    }

}