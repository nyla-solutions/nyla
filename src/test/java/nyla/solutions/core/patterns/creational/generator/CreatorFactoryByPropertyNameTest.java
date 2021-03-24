package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatorFactoryByPropertyNameTest
{
    private CreatorFactoryByPropertyName subject = new CreatorFactoryByPropertyName(DateTimeFormatter.ISO_DATE_TIME);

    @Test
    void forProperty_date()
    {
        String expectedPropertyName = "date";
        Class<?> expectedClass = DateTextCreator.class;
        verify(expectedPropertyName, expectedClass);
    }

    @Test
    void forProperty_email()
    {
        String expectedPropertyName = "email";
        Class<?> expectedClass = EmailCreator.class;
        verify(expectedPropertyName, expectedClass);
    }

    @Test
    void forProperty_firstname()
    {
        String expectedPropertyName = "firstname";
        Class<?> expectedClass = FirstNameCreator.class;
        verify(expectedPropertyName, expectedClass);
    }

    @Test
    void forProperty_lastname()
    {
        String expectedPropertyName = "lastname";
        Class<?> expectedClass = LastNameCreator.class;
        verify(expectedPropertyName, expectedClass);
    }

    @Test
    void forProperty_id()
    {
        String expectedPropertyName = "id";
        Class<?> expectedClass = IdCreator.class;
        verify(expectedPropertyName, expectedClass);
    }

    @Test
    void forProperty_phone()
    {
        Class<?> expectedClass = PhoneNumberCreator.class;
        String expectedPropertyName = "phone";
        verify(expectedPropertyName, expectedClass);
        expectedPropertyName = "mobile";
        verify(expectedPropertyName, expectedClass);
        expectedPropertyName = "fax";
        verify(expectedPropertyName, expectedClass);
    }

    private void verify(String expectedPropertyName, Class<?> expectedClass)
    {
        assertNotNull(subject.forProperty(""));
        assertNotNull(subject.forProperty(null));
        assertTrue(expectedClass.isInstance(subject.forProperty(expectedPropertyName)));
        expectedPropertyName = expectedPropertyName.toUpperCase();
        assertTrue(expectedClass.isInstance(subject.forProperty(expectedPropertyName)));
        expectedPropertyName = Text.toProperCase(expectedPropertyName);
        assertTrue(expectedClass.isInstance(subject.forProperty(expectedPropertyName)));
    }
}