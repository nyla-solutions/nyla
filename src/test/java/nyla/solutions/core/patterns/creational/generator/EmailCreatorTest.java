package nyla.solutions.core.patterns.creational.generator;

import static org.junit.jupiter.api.Assertions.*;

import nyla.solutions.core.patterns.expression.IsEmailExpression;
import  org.junit.jupiter.api.*;


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