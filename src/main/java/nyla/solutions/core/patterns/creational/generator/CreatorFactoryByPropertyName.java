package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.util.Text;

import java.time.format.DateTimeFormatter;

/**
 * @author Gregory Green
 */
public class CreatorFactoryByPropertyName
{
    private final DateTimeFormatter textDateFormat;

    public CreatorFactoryByPropertyName(DateTimeFormatter textDateFormat)
    {
        this.textDateFormat = textDateFormat;
    }


    public <T> Creator<T> forProperty( String property)
    {
        if(property == null)
            return  () -> (T) Text.generateId();


        String lowerCaseProperty = property.toLowerCase();

        final Creator<?> creator;

        if(lowerCaseProperty.contains("email"))
        {
            creator = new EmailCreator();
        }
        else if(lowerCaseProperty.contains("firstname"))
        {
            creator = new FirstNameCreator();
        }
        else if(lowerCaseProperty.contains("lastname"))
        {
            creator = new LastNameCreator();
        }
        else if(lowerCaseProperty.equals("name")
                ||lowerCaseProperty.contains("fullname")
                ||lowerCaseProperty.contains("customer"))
        {
            creator = new FullNameCreator();
        }
        else if(lowerCaseProperty.contains("phone")||
                lowerCaseProperty.contains("mobile")||
                lowerCaseProperty.contains("fax"))
        {
            creator = new PhoneNumberCreator();
        }
        else if(lowerCaseProperty.contains("date"))
        {
            creator = new DateTextCreator(textDateFormat);
        }
        else if(lowerCaseProperty.endsWith("id"))
        {
            creator = new IdCreator();
        }
        else
        {
            creator = () -> Text.generateId();
        }

        return (Creator<T>) creator;
    }
}
