package nyla.solutions.core.patterns.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsPhoneNumberExpressionTest
{
    @Test
    public void test_null_isfalse()
    {
        assertFalse(new IsPhoneNumberExpression().apply(null));
    }
    @Test
    public void test_empty_isfalse()
    {
        assertFalse(new IsPhoneNumberExpression().apply(""));
    }
    @Test
    public void test_invalid_isfalse()
    {
        assertFalse(new IsPhoneNumberExpression().apply("23-232"));
    }

    @Test
    public void test_valid()
    {
        IsPhoneNumberExpression subject = new IsPhoneNumberExpression();

        assertTrue(subject.apply("123-456-7890"));
        assertTrue(subject.apply("(123) 456-7890"));
        assertTrue(subject.apply("123 456 7890"));
        assertTrue(subject.apply("123.456.7890"));
        assertTrue(subject.apply("+91 (123) 456-7890"));
        assertTrue(subject.apply("4449685657"));

    }

}