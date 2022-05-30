package nyla.solutions.core.patterns.expression;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrueFalseBooleanExpressionTest
{

    @Test
    void GivenTrueWhenCreated_Then_True()
    {
        assertEquals(true,new TrueFalseBooleanExpression<String>(true).apply("hello"));
    }

    @Test
    public void test_true_false_expression()
    {
        TrueFalseBooleanExpression<String> expression
                = new TrueFalseBooleanExpression<>();

        expression.setBoolean(true);

        assertTrue(expression.apply("null"));

        expression.setBoolean(false);
        assertFalse(expression.apply("null"));
    }
}