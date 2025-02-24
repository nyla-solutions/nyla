package nyla.solutions.core.patterns.expression;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrExpressionTest
{


    @Test
    void apply_WhenAllFalse_ThenReturnFalse()
    {
        OrExpression subject = new OrExpression(new TrueFalseBooleanExpression<String>(false),
                new TrueFalseBooleanExpression<String>(false),
                new TrueFalseBooleanExpression<String>(false));

        assertEquals(false,subject.apply("hello"));
    }

    @Test
    void apply_WhenOneTrue_ThenReturnFalse()
    {
        OrExpression subject = new OrExpression(new TrueFalseBooleanExpression<String>(true),
                new TrueFalseBooleanExpression<String>(true),
                new TrueFalseBooleanExpression<String>(false));

        assertEquals(true,subject.apply("hello"));
    }

}