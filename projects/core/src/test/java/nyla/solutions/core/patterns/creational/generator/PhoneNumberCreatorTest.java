package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.expression.IsPhoneNumberExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneNumberCreatorTest
{

    @Test
    void create()
    {
        PhoneNumberCreator subject = new PhoneNumberCreator();
        String phone = subject.create();
        System.out.println(phone);
        assert(phone.matches("^.*-.*-.*$"));
        assertTrue(new IsPhoneNumberExpression().apply(phone));
    }
}