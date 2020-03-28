package nyla.solutions.core.patterns.expression;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrueFalseBooleanExpressionTest
{
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