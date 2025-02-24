package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.data.Textable;
import nyla.solutions.core.patterns.creational.Creator;
import nyla.solutions.core.patterns.decorator.MappedTextFormatDecorator;
import nyla.solutions.core.util.Organizer;

import java.util.Map;

/**
 * This class generator text based on a template
 *
 *
 * Example Template
 *
 * "Hello ${name}, did you know your email is ${email}"
 *
 *
 * "${id} ${email} ${firstName} ${lastName} ${name} ${fullName} ${phone} ${mobile} ${fax} ${date}"
 *
 * @author Gregory Green
 */
public class GenerateTextWithPropertiesCreator implements Creator<String>, Textable
{
    public static final String EMAIL_PLACEHOLDER = "email";
    public static final String ID_PLACEHOLDER = "id";
    public static final String FIRST_NAME_PLACEHOLDER = "firstName";
    public static final String LAST_NAME_PLACEHOLDER = "lastName";
    public static final String NAME_PLACEHOLDER = "name";
    public static final String FULL_NAME_PLACEHOLDER = "fullName";
    public static final String CUSTOMER_NAME_PLACEHOLDER = "customerName";
    public static final String PHONE_PLACEHOLDER = "phone";
    public static final String PHONE_NUMBER_PLACEHOLDER = "phoneNumber";
    public static final String NUMBER_PLACEHOLDER = "number";
    private static final String FAX_PLACEHOLDER = "fax";
    private static final String MOBILE_PLACEHOLDER = "mobile";
    private static final String DATE_PLACEHOLDER = "date";
    private static final String INTEGER_RANGE_PLACEHOLDER = "intRange";


    private final MappedTextFormatDecorator decorator;
    public GenerateTextWithPropertiesCreator(String template)
    {
        FullNameCreator fullNameCreator = new FullNameCreator();
        PhoneNumberCreator phoneNumberCreator = new PhoneNumberCreator();
        IdCreator idCreator = new IdCreator();
        DateTextCreator dateCreator = new DateTextCreator();

        Map<String,Textable> map =  Organizer.toMap(EMAIL_PLACEHOLDER,new EmailCreator(),
                ID_PLACEHOLDER,idCreator,
                NUMBER_PLACEHOLDER,idCreator,
                FIRST_NAME_PLACEHOLDER,new FirstNameCreator(),
                LAST_NAME_PLACEHOLDER,new LastNameCreator(),
                NAME_PLACEHOLDER,fullNameCreator,
                FULL_NAME_PLACEHOLDER,fullNameCreator,
                CUSTOMER_NAME_PLACEHOLDER,fullNameCreator,
                PHONE_PLACEHOLDER, phoneNumberCreator,
                PHONE_NUMBER_PLACEHOLDER, phoneNumberCreator,
                FAX_PLACEHOLDER, phoneNumberCreator,
                MOBILE_PLACEHOLDER, phoneNumberCreator,
                DATE_PLACEHOLDER, dateCreator,
                INTEGER_RANGE_PLACEHOLDER,new IntegerRangeTextCreator()
                );

        decorator = new MappedTextFormatDecorator(
               map,
                template);
    }

    /**
     * Factory method
     * @param template the template
     * @return the instance
     */
    public static GenerateTextWithPropertiesCreator withTemplate(String template)
    {
        return new GenerateTextWithPropertiesCreator(template);
    }

    /**
     * @return the create object
     */
    @Override
    public String create()
    {
        return decorator.getText();
    }

    /**
     * @return the text
     */
    @Override
    public String getText()
    {
        return create();
    }
}
