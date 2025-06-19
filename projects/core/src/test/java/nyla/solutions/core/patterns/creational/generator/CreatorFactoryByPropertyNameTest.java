package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Text;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatorFactoryByPropertyNameTest
{
    private final CreatorFactoryByPropertyName subject = new CreatorFactoryByPropertyName(DateTimeFormatter.ISO_DATE_TIME);

    @Test
    void forProperty_date()
    {
        verify("date", DateTextCreator.class);
    }

    @Test
    void forProperty_email()
    {
        verify("email", EmailCreator.class);
    }

    @Test
    void forProperty_firstname()
    {
        verify("firstname", FirstNameCreator.class);
    }

    @Test
    void forProperty_lastname()
    {
        verify("lastname", LastNameCreator.class);
    }

    @Test
    void forProperty_city()
    {
        verify("city", CityCreator.class);
        verify("cityName", CityCreator.class);
        verify("province", CityCreator.class);
        verify("town", CityCreator.class);
    }

    @Test
    void forProperty_state()
    {
        verify("state", StateCreator.class);
        verify("stateName", StateCreator.class);
    }

    @Test
    void forProperty_zip()
    {
        verify("zip", ZipCreator.class);
        verify("zipCode", ZipCreator.class);
    }

    @Test
    void forProperty_id()
    {
        verify("id", IdCreator.class);
    }

    @Test
    void forProperty_phone()
    {
        verify("phone", PhoneNumberCreator.class);
        verify("phoneNumber", PhoneNumberCreator.class);
        verify("mobile", PhoneNumberCreator.class);
        verify("fax", PhoneNumberCreator.class);
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