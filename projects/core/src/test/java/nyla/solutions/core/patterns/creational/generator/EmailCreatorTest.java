package nyla.solutions.core.patterns.creational.generator;

import nyla.solutions.core.patterns.expression.IsEmailExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class EmailCreatorTest
{

    @Test
    public void test_create()
    {
        EmailCreator subject = new EmailCreator();
        String actual;
        IsEmailExpression isEmail = new IsEmailExpression();

        for (int i = 0; i < 10; i++)
        {
            actual = subject.create();

            System.out.println(actual);
            assertTrue(isEmail.apply(actual));
        }


    }

}