package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.util.Config;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GenerateTextWithPropertiesCreatorTest
{


    @Test
    void withTemplate()
    {
        String expected = "hello world";
        String actual = GenerateTextWithPropertiesCreator.withTemplate(expected).create();
        assertNotNull(actual);
        assertEquals(expected,actual);
    }
/*


        ${id} ${email} ${firstName} ${lastName} ${name} ${fullName} ${phone} ${mobile} ${fax} ${date}

     */

    @Test
    void create()
    {

        String template = "Hello ${id} ${email} ${firstName} ${lastName} ${name} ${fullName}, "+
                "your phone is ${phone}, your cell is ${mobile} your fax is ${fax} "+
                "generated on ${date}";

        GenerateTextWithPropertiesCreator subject = new GenerateTextWithPropertiesCreator(template);
        String actual = subject.getText();

        System.out.println(actual);
        assertNotNull(actual);
        assertFalse(actual.contains("${"));

    }

    @Test
    void create_GivenEmail_Then_Replaced()
    {
        String template = "Hello ${email}";
        verify(template,"email");
    }

    @Test
    void create_GivenName_Then_Replaced()
    {
        String template = "Hello ${name}";
        verify(template,"name");
    }

    @Test
    void create_GivenCustomerName_Then_Replaced()
    {
        String template = "Hello ${customerName}";
        verify(template,"customerName");
    }

    @Test
    void create_GivenFullName_Then_Replaced()
    {
        String template = "Hello ${fullName}";
        verify(template,"fullName");
    }

    @Test
    void create_GivenFirstName_Then_Replaced()
    {
        String template = "Hello ${firstName}";
        verify(template,"firstName");
    }

    @Test
    void create_GivenDate_Then_Replaced()
    {
        String template = "Hello ${date}";
        verify(template,"date");
    }

    @Test
    void create_GivenLastName_Then_Replaced()
    {
        String template = "Hello ${lastName}";
        verify(template,"lastName");
    }
    @Test
    void create_GivenFax_Then_Replaced()
    {
        String template = "Call ${fax}";
        verify(template,"fax");
    }

    @Test
    void create_GivenMobile_Then_Replaced()
    {
        String template = "Call ${mobile}";
        verify(template,"mobile");
    }

    @Test
    void create_GivenId_Then_Replaced()
    {
        String template = "Hello ${id}";
        verify(template,"id");
    }

    @Test
    void create_GivenNumber_Then_Replaced()
    {
        String template = "Hello ${number}";
        verify(template,"number");
    }


    @Test
    void create_GivenPhone_Then_Replaced()
    {
        String template = "Hello ${phone}";
        verify(template,"phone");
    }

    @Test
    void create_GivenPhoneNumber_Then_Replaced()
    {
        String template = "Hello ${phoneNumber}";
        verify(template,"phoneNumber");
    }

    @Test
    void create_GivenIntRangeText_Then_Replaced()
    {

        Properties properties = new Properties();
        properties.setProperty(IntegerRangeTextCreator.MIN_INT_TEXT_PROP,"0");
        properties.setProperty(IntegerRangeTextCreator.MAX_INT_TEXT_PROP,"30");
        Config.setProperties(properties);
        String template = "Hello ${intRange}";
        verify(template,"intRange");
    }


    @Test
    void create_GivenNotKeyInTemplate_WhenCreate_Then_DoReplaceAnyThing()
    {

        Properties properties = new Properties();
        properties.setProperty(IntegerRangeTextCreator.MIN_INT_TEXT_PROP,"");
        properties.setProperty(IntegerRangeTextCreator.MAX_INT_TEXT_PROP,"");
        Config.setProperties(properties);
        String expected = "Hello World";
        GenerateTextWithPropertiesCreator subject = new GenerateTextWithPropertiesCreator(expected);
        String actual = subject.getText();

        assertEquals(expected,actual);
    }

    private void verify(String template,String term)
    {
        GenerateTextWithPropertiesCreator subject = new GenerateTextWithPropertiesCreator(template);
        String actual = subject.getText();

        System.out.println(actual);

        assertNotNull(actual);
        assertThat(actual).doesNotContain("${"+term+"}");
    }
}