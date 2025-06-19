package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.expression.IsPhoneNumberExpression;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneNumberCreatorTest
{
    private final PhoneNumberCreator subject = new PhoneNumberCreator();

    @Test
    void create()
    {

        String phone = subject.create();
        System.out.println(phone);
        assert(phone.matches("^.*-.*-.*$"));
        assertTrue(new IsPhoneNumberExpression().apply(phone));
    }

    @Test
    void startWith555_01 (){

        var phone = subject.create();
        assertThat(phone).startsWith("555-01");
    }
}