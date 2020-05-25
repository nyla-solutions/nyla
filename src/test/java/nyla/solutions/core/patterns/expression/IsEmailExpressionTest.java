package nyla.solutions.core.patterns.expression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class IsEmailExpressionTest
{
    @Test
    public void test_create()
    {
        IsEmailExpression subject = new IsEmailExpression();
        assertFalse(subject.apply(null));
        assertFalse(subject.apply(""));
        assertFalse(subject.apply(" "));
        assertFalse(subject.apply(" sds sds !@.com"));
        assertTrue(subject.apply("g@g.com"));
        assertTrue(subject.apply("sdsds@232.edsu"));
        assertTrue(subject.apply("sdsds@232.edfsdfsu"));
        assertFalse(subject.apply("g.com"));
    }

}